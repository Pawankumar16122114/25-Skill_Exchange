package com.skillexchange.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.skillexchange.app.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    val currentUserId: String? get() = auth.currentUser?.uid
    val isLoggedIn: Boolean get() = auth.currentUser != null

    suspend fun register(email: String, password: String, name: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user!!.uid
            val user = User(
                uid = uid,
                name = name,
                email = email,
                skillPoints = 10
            )
            usersCollection.document(uid).set(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() = auth.signOut()

    fun observeCurrentUser(): Flow<User?> = callbackFlow {
        val uid = currentUserId ?: run { trySend(null); close(); return@callbackFlow }
        val listener = usersCollection.document(uid).addSnapshotListener { snap, _ ->
            trySend(snap?.toObject(User::class.java))
        }
        awaitClose { listener.remove() }
    }

    suspend fun getUser(uid: String): User? {
        return try {
            usersCollection.document(uid).get().await().toObject(User::class.java)
        } catch (e: Exception) { null }
    }

    suspend fun updateUser(user: User): Result<Unit> {
        return try {
            usersCollection.document(user.uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateSkillPoints(uid: String, delta: Int): Result<Unit> {
        return try {
            usersCollection.document(uid)
                .update("skillPoints", com.google.firebase.firestore.FieldValue.increment(delta.toLong()))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementTrustScore(uid: String, newScore: Float, newSwapCount: Int): Result<Unit> {
        return try {
            usersCollection.document(uid).update(
                mapOf(
                    "trustScore" to newScore,
                    "completedSwaps" to newSwapCount
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
