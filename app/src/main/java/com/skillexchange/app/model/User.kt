package com.skillexchange.app.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    @DocumentId
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val bio: String = "",
    val skills: List<String> = emptyList(),
    val skillPoints: Int = 10, // Start with 10 free points
    val trustScore: Float = 0f,
    val completedSwaps: Int = 0,
    val avatarUrl: String = "",
    @ServerTimestamp
    val createdAt: Date? = null
)
