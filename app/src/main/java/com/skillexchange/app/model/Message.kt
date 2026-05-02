package com.skillexchange.app.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Message(
    @DocumentId
    val id: String = "",
    val swapId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)
