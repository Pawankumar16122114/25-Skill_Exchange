package com.skillexchange.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillexchange.app.model.NeedPost
import com.skillexchange.app.model.SwapOffer
import com.skillexchange.app.repository.PostRepository
import com.skillexchange.app.repository.SwapRepository
import com.skillexchange.app.repository.UserRepository
import com.skillexchange.app.util.SkillPointCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PostState {
    object Idle : PostState()
    object Loading : PostState()
    data class Success(val message: String) : PostState()
    data class Error(val message: String) : PostState()
}

class SwapViewModel : ViewModel() {
    private val userRepo = UserRepository()
    private val postRepo = PostRepository()
    private val swapRepo = SwapRepository()

    private val _postState = MutableStateFlow<PostState>(PostState.Idle)
    val postState: StateFlow<PostState> = _postState

    private val _offerState = MutableStateFlow<PostState>(PostState.Idle)
    val offerState: StateFlow<PostState> = _offerState

    fun createNeedPost(title: String, description: String, skillRequired: String) {
        viewModelScope.launch {
            _postState.value = PostState.Loading
            val uid = userRepo.currentUserId ?: run {
                _postState.value = PostState.Error("Not logged in")
                return@launch
            }
            val user = userRepo.getUser(uid)
            val post = NeedPost(
                authorId = uid,
                authorName = user?.name ?: "Unknown",
                authorAvatarUrl = user?.avatarUrl ?: "",
                title = title,
                description = description,
                skillRequired = skillRequired
            )
            val result = postRepo.createPost(post)
            _postState.value = if (result.isSuccess) {
                PostState.Success("Need posted successfully!")
            } else {
                PostState.Error("Failed to post need")
            }
        }
    }

    fun makeSwapOffer(
        post: NeedPost,
        skillOffered: String,
        hoursOffered: Int,
        message: String
    ) {
        viewModelScope.launch {
            _offerState.value = PostState.Loading
            val uid = userRepo.currentUserId ?: run {
                _offerState.value = PostState.Error("Not logged in")
                return@launch
            }
            val user = userRepo.getUser(uid) ?: run {
                _offerState.value = PostState.Error("User not found")
                return@launch
            }
            if (!SkillPointCalculator.canMakeOffer(user.skillPoints, hoursOffered)) {
                _offerState.value = PostState.Error("Not enough Skill Points! You have ${user.skillPoints} pts.")
                return@launch
            }
            val offer = SwapOffer(
                postId = post.id,
                postAuthorId = post.authorId,
                offererId = uid,
                offererName = user.name,
                offererAvatarUrl = user.avatarUrl,
                skillOffered = skillOffered,
                hoursOffered = hoursOffered,
                message = message
            )
            val result = swapRepo.makeOffer(offer)
            if (result.isSuccess) {
                // Deduct points from offerer
                userRepo.updateSkillPoints(uid, -hoursOffered)
                postRepo.incrementOfferCount(post.id)
                _offerState.value = PostState.Success("Offer sent!")
            } else {
                _offerState.value = PostState.Error("Failed to send offer")
            }
        }
    }

    fun resetPostState() { _postState.value = PostState.Idle }
    fun resetOfferState() { _offerState.value = PostState.Idle }
}
