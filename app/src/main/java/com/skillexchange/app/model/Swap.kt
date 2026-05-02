package com.skillexchange.app.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Swap(
    @DocumentId
    val id: String = "",
    val postId: String = "",
    val offerId: String = "",
    val requesterId: String = "",
    val requesterName: String = "",
    val offererId: String = "",
    val offererName: String = "",
    val skillExchanged: String = "",
    val hoursExchanged: Int = 1,
    val confirmedByRequester: Boolean = false,
    val confirmedByOfferer: Boolean = false,
    val isCompleted: Boolean = false,
    @ServerTimestamp
    val createdAt: Date? = null,
    val completedAt: Date? = null
)
