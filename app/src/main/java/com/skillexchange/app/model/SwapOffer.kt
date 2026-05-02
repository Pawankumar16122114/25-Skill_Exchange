package com.skillexchange.app.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

enum class OfferStatus { PENDING, ACCEPTED, DECLINED, CANCELLED }

data class SwapOffer(
    @DocumentId
    val id: String = "",
    val postId: String = "",
    val postAuthorId: String = "",
    val offererId: String = "",
    val offererName: String = "",
    val offererAvatarUrl: String = "",
    val skillOffered: String = "",
    val hoursOffered: Int = 1,  // 1 hour = 1 Skill Point
    val message: String = "",
    val status: String = OfferStatus.PENDING.name,
    @ServerTimestamp
    val timestamp: Date? = null
)
