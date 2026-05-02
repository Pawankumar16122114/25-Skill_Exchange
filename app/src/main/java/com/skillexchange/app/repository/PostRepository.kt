package com.skillexchange.app.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.skillexchange.app.model.NeedPost
import com.skillexchange.app.model.PostStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PostRepository {
    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("needPosts")

    fun observePosts(skillFilter: String? = null): Flow<List<NeedPost>> = callbackFlow {
        var query: Query = postsCollection
            .whereEqualTo("status", PostStatus.OPEN.name)

        if (!skillFilter.isNullOrBlank() && skillFilter != "All") {
            query = postsCollection
                .whereEqualTo("skillRequired", skillFilter)
                .whereEqualTo("status", PostStatus.OPEN.name)
        }

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                error.printStackTrace()
                trySend(emptyList())
                return@addSnapshotListener
            }
            
            val posts = snapshot?.toObjects(NeedPost::class.java) ?: emptyList()
            val sortedPosts = posts.sortedByDescending { it.timestamp }
            trySend(sortedPosts)
        }
        awaitClose { listener.remove() }
    }

    suspend fun createPost(post: NeedPost): Result<String> {
        return try {
            val ref = postsCollection.add(post).await()
            Result.success(ref.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPost(postId: String): NeedPost? {
        return try {
            postsCollection.document(postId).get().await().toObject(NeedPost::class.java)
        } catch (e: Exception) { null }
    }

    suspend fun updatePostStatus(postId: String, status: PostStatus): Result<Unit> {
        return try {
            postsCollection.document(postId).update("status", status.name).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementOfferCount(postId: String): Result<Unit> {
        return try {
            postsCollection.document(postId)
                .update("offerCount", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
