package com.skillexchange.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillexchange.app.model.Message
import com.skillexchange.app.model.Swap
import com.skillexchange.app.repository.ChatRepository
import com.skillexchange.app.repository.SwapRepository
import com.skillexchange.app.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val chatRepo = ChatRepository()
    private val swapRepo = SwapRepository()
    private val userRepo = UserRepository()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _swap = MutableStateFlow<Swap?>(null)
    val swap: StateFlow<Swap?> = _swap

    private val _confirmState = MutableStateFlow<String?>(null)
    val confirmState: StateFlow<String?> = _confirmState

    val currentUserId: String? get() = userRepo.currentUserId

    fun loadMessages(swapId: String) {
        viewModelScope.launch {
            chatRepo.observeMessages(swapId).collect { _messages.value = it }
        }
    }

    fun observeSwap(swapId: String) {
        viewModelScope.launch {
            swapRepo.observeSwap(swapId).collect { _swap.value = it }
        }
    }

    fun sendMessage(swapId: String, text: String, senderName: String) {
        val uid = currentUserId ?: return
        val message = Message(
            swapId = swapId,
            senderId = uid,
            senderName = senderName,
            text = text
        )
        viewModelScope.launch {
            chatRepo.sendMessage(swapId, message)
        }
    }

    fun confirmSwap(swapId: String, isRequester: Boolean) {
        viewModelScope.launch {
            val result = swapRepo.confirmSwap(swapId, isRequester)
            _confirmState.value = if (result.isSuccess) "confirmed" else "error"
        }
    }
}
