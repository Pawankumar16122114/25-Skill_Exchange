package com.skillexchange.app.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.skillexchange.app.model.Swap
import com.skillexchange.app.model.SwapOffer
import com.skillexchange.app.util.SkillPointCalculator
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SwapRepository {
    private val db = FirebaseFirestore.getInstance()
    private val offersCollection = db.collection("swapOffers")
    private val swapsCollection = db.collection("swaps")
    private val usersCollection = db.collection("users")

    suspend fun makeOffer(offer: SwapOffer): Result<String> {
        return try {
            val ref = offersCollection.add(offer).await()
            Result.success(ref.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun observeOffersForPost(postId: String): Flow<List<SwapOffer>> = callbackFlow {
        val listener = offersCollection
            .whereEqualTo("postId", postId)
            .addSnapshotListener { snap, _ ->
                trySend(snap?.toObjects(SwapOffer::class.java) ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    suspend fun acceptOffer(offer: SwapOffer): Result<String> {
        return try {
            // Create a Swap document
            val swap = Swap(
                postId = offer.postId,
                offerId = offer.id,
                requesterId = offer.postAuthorId,
                offererId = offer.offererId,
                offererName = offer.offererName,
                skillExchanged = offer.skillOffered,
                hoursExchanged = offer.hoursOffered
            )
            val swapRef = swapsCollection.add(swap).await()
            // Update offer status
            offersCollection.document(offer.id).update("status", "ACCEPTED").await()
            Result.success(swapRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun observeSwap(swapId: String): Flow<Swap?> = callbackFlow {
        val listener = swapsCollection.document(swapId).addSnapshotListener { snap, _ ->
            trySend(snap?.toObject(Swap::class.java))
        }
        awaitClose { listener.remove() }
    }

    suspend fun confirmSwap(swapId: String, isRequester: Boolean): Result<Unit> {
        return try {
            val field = if (isRequester) "confirmedByRequester" else "confirmedByOfferer"
            swapsCollection.document(swapId).update(field, true).await()

            // Check if both confirmed
            val swapDoc = swapsCollection.document(swapId).get().await()
            val swap = swapDoc.toObject(Swap::class.java)
            if (swap?.confirmedByRequester == true && swap.confirmedByOfferer == true) {
                // Complete the swap
                swapsCollection.document(swapId).update(
                    mapOf(
                        "isCompleted" to true,
                        "completedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                    )
                ).await()

                // Transfer skill points: offerer receives, requester already gave implicitly
                val pointsToTransfer = swap.hoursExchanged.toLong()
                usersCollection.document(swap.offererId)
                    .update("skillPoints", com.google.firebase.firestore.FieldValue.increment(pointsToTransfer))
                    .await()

                // Increment trust scores for both parties
                incrementUserTrustAndSwapCount(swap.requesterId)
                incrementUserTrustAndSwapCount(swap.offererId)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun incrementUserTrustAndSwapCount(uid: String) {
        val userDoc = usersCollection.document(uid).get().await()
        val currentScore = userDoc.getDouble("trustScore")?.toFloat() ?: 0f
        val currentSwaps = userDoc.getLong("completedSwaps")?.toInt() ?: 0
        val newScore = SkillPointCalculator.calculateNewTrustScore(currentScore, currentSwaps)
        usersCollection.document(uid).update(
            mapOf(
                "trustScore" to newScore,
                "completedSwaps" to currentSwaps + 1
            )
        ).await()
    }

    fun observeUserSwaps(userId: String): Flow<List<Swap>> = callbackFlow {
        val listener = swapsCollection
            .whereEqualTo("requesterId", userId)
            .addSnapshotListener { snap, _ ->
                trySend(snap?.toObjects(Swap::class.java) ?: emptyList())
            }
        awaitClose { listener.remove() }
    }
}
