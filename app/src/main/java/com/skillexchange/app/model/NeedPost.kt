package com.skillexchange.app.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

enum class PostStatus { OPEN, IN_PROGRESS, COMPLETED, CANCELLED }

data class NeedPost(
    @DocumentId
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorAvatarUrl: String = "",
    val title: String = "",
    val description: String = "",
    val skillRequired: String = "",
    val status: String = PostStatus.OPEN.name,
    val offerCount: Int = 0,
    @ServerTimestamp
    val timestamp: Date? = null
)
