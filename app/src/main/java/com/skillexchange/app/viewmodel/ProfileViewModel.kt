package com.skillexchange.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillexchange.app.model.NeedPost
import com.skillexchange.app.model.User
import com.skillexchange.app.repository.PostRepository
import com.skillexchange.app.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: User) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel : ViewModel() {
    private val userRepo = UserRepository()
    private val postRepo = PostRepository()

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _saveState = MutableStateFlow<Boolean?>(null)
    val saveState: StateFlow<Boolean?> = _saveState

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            userRepo.observeCurrentUser().collect { user ->
                _uiState.value = if (user != null) {
                    ProfileUiState.Success(user)
                } else {
                    ProfileUiState.Error("User not found")
                }
            }
        }
    }

    fun updateProfile(name: String, bio: String, skills: List<String>) {
        val currentUser = (_uiState.value as? ProfileUiState.Success)?.user ?: return
        viewModelScope.launch {
            val updatedUser = currentUser.copy(name = name, bio = bio, skills = skills)
            val result = userRepo.updateUser(updatedUser)
            _saveState.value = result.isSuccess
        }
    }

    fun logout() = userRepo.logout()

    fun getCurrentUserId() = userRepo.currentUserId
}
