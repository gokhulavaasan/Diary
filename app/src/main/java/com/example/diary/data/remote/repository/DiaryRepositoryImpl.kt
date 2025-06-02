package com.example.diary.data.remote.repository

import android.util.Log
import com.example.diary.data.remote.mappers.toDiary
import com.example.diary.domain.model.Diary
import com.example.diary.domain.repository.DiaryRepository
import com.example.diary.util.Resource
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class DiaryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : DiaryRepository {

    override suspend fun getDiaries(): Flow<Resource<List<Diary>>> {
        return callbackFlow {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                trySend(Resource.Error(message = "User Not Logged IN"))
                return@callbackFlow
            } else {
                try {
                    /*val snapshot = firestore
                        .collection("users")
                        .document(userId)
                        .collection("diaries")
                        .orderBy("date", Query.Direction.DESCENDING)
                        //                        .whereEqualTo("ownerId", userId)
                        .get()
                        .await()*/
                    val listenerRegistration = firestore
                        .collection("users")
                        .document(userId)
                        .collection("diaries")
                        .orderBy("date", Query.Direction.DESCENDING)
                        .addSnapshotListener { snapShot, error ->
                            if (error != null) {
                                Log.e("FirebaseQuery", "Error Fetching Data", error)
                                trySend(Resource.Error(message = error.message.toString()))
                                return@addSnapshotListener
                            } else if (snapShot != null) {
                                val diaries = snapShot.documents.mapNotNull { doc ->
                                    doc.toObject(DiaryDto::class.java)?.toDiary(doc.id)
                                }
                                trySend(Resource.Success(diaries))
                            }
                        }
                    awaitClose {
                        listenerRegistration.remove()
                    }
                    /* val diaries = snapshot.documents.mapNotNull { doc ->
                         doc.toObject(DiaryDto::class.java)?.toDiary(doc.id)
                     }*/
//                    Log.d("API_RESPONSE", diaries.toString())
                    /*emit(Resource.Success(diaries))*/
                } catch (e: Exception) {
                    Log.e("FirebaseQuery", "Error Fetching Data", e)
                    trySend(Resource.Error(message = e.message.toString()))
                    return@callbackFlow
                }
            }
            return@callbackFlow
        }
    }


    override suspend fun insertDiary(diary: Diary): Flow<Resource<Diary>> {
        return flow {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                emit(Resource.Error(message = "User Not Logged IN"))
                return@flow
            } else {
                try {
                    val userDiariesRef = firestore
                        .collection("users")
                        .document(userId)
                        .collection("diaries")
                    val newDiaryRef = userDiariesRef.document()

                    val updatedDiary = diary.copy(
                        id = newDiaryRef.id,
                        ownerId = userId
                    )
                    newDiaryRef.set(updatedDiary)
                    emit(Resource.Success(diary))
                    return@flow
                } catch (e: Exception) {
                    Log.i("diaryInsertError", e.message.toString())
                    emit(Resource.Error(message = e.message.toString()))
                }
            }
        }
    }

    override suspend fun updateDiary(diary: Diary): Flow<Resource<Diary>> {
        return flow {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                emit(Resource.Error(message = "User Not Logged IN"))
                return@flow
            } else {
                try {
                    firestore.collection("users")
                        .document(userId)
                        .collection("diaries")
                        .document(diary.id)
                        .update(
                            mapOf(
                                "title" to diary.title,
                                "description" to diary.description,
                                "mood" to diary.mood,
                                "date" to diary.date,
                                "images" to diary.images
                            )
                        )
                        .await()
                    Log.i("diaryUpdateFirebase", "successfully updated")
                    emit(Resource.Success(diary))
                    return@flow
                } catch (e: Exception) {
                    Log.i("diaryUpdateError", e.message.toString())
                    emit(Resource.Error(message = e.message.toString()))
                }
            }
        }
    }

    override suspend fun deleteDiary(diaryId: String): Flow<Resource<Boolean>> {
        return flow {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                emit(Resource.Error(message = "User Not Logged IN"))
                return@flow
            } else {
                try {
                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .collection("diaries")
                        .document(diaryId)
                        .delete()
                        .await()
                    Log.d("Firestore", "Diary with ID $diaryId successfully deleted.")
                    emit(Resource.Success(true))
                } catch (e: Exception) {
                    Log.e("Firestore", "Error deleting diary", e)
                    emit(Resource.Error(message = e.message.toString()))
                }
            }
        }
    }

    override suspend fun getDiary(id: String): Flow<Resource<Diary>> {
        return flow {
            emit(Resource.Loading(true))
            val userId = auth.currentUser?.uid
            if (userId == null) {
                emit(Resource.Error(message = "User Not Logged IN"))
                return@flow
            } else {
                try {
                    val snapshot = firestore
                        .collection("users")
                        .document(userId)
                        .collection("diaries")
                        .document(id)
                        .get()
                        .await()
                    val diary = snapshot.toObject(DiaryDto::class.java)?.toDiary(id)
                    emit(Resource.Success(diary))
                    return@flow
                } catch (e: Exception) {
                    Log.i("diaryFetchError", e.message.toString())
                    emit(Resource.Error(message = e.message.toString()))
                }
            }

        }
    }
}

//using this for development after remove it
val diaries1 = listOf(
    Diary(
        id = "2CuyXHZ1jLbbSIKdF8QJ",
        ownerId = "6f8d7a79-40e5-4b99-b134-7f7d5f5e8e8f",
        mood = "Neutral",
        title = "Work Success",
        description = "Finished an important project",
        images = listOf("https://picsum.photos/id/237/200/300"),
        date = Timestamp(1748038950, 619000000)
    ),
    Diary(
        id = "lxzVhpbAnYSPig45zvJI",
        ownerId = "DZigB2wamXSIt3NZkM9IYsWL0263",
        mood = "Angry",
        title = "Today’s Joy",
        description = "Had a great day at the park.",
        images = listOf(
            "https://picsum.photos/id/237/200/300",
            "https://picsum.photos/id/237/200/300",
            "https://picsum.photos/id/237/200/300"
        ),
        date = Timestamp(1748038683, 839000000)
    ),
    Diary(
        id = "6f8d7a79-40e5-4b99-b134-7f7d5f5e8e8f",
        ownerId = "DZigB2wamXSIt3NZkM9IYsWL0263",
        mood = "Happy",
        title = "A Fun Day!",
        description = "Had a great time at the beach today!",
        images = listOf("https://picsum.photos/id/237/200/300"),
        date = Timestamp(1742327894, 281000000)
    ),

    // 5 new diary entries with same ownerId
    Diary(
        id = "a1b2c3d4e5f6g7h8i9j0",
        ownerId = "DZigB2wamXSIt3NZkM9IYsWL0263",
        mood = "Bored",
        title = "New Adventure",
        description = "Planning a hiking trip next weekend!",
        images = listOf("https://picsum.photos/id/238/200/300"),
        date = Timestamp(1749000000, 100000000)
    ),
    Diary(
        id = "z9y8x7w6v5u4t3s2r1q0",
        ownerId = "DZigB2wamXSIt3NZkM9IYsWL0263",
        mood = "Calm",
        title = "Chill Day",
        description = "Spent the day reading and relaxing at home.",
        images = emptyList(),
        date = Timestamp(1748500000, 500000000)
    ),
    Diary(
        id = "m1n2b3v4c5x6z7l8k9j0",
        ownerId = "DZigB2wamXSIt3NZkM9IYsWL0263",
        mood = "Depressed",
        title = "Work Goals",
        description = "Started learning a new programming language.",
        images = listOf("https://picsum.photos/id/239/200/300"),
        date = Timestamp(1747800000, 900000000)
    ),
    Diary(
        id = "p9o8i7u6y5t4r3e2w1q0",
        ownerId = "DZigB2wamXSIt3NZkM9IYsWL0263",
        mood = "Disappointed",
        title = "Family Time",
        description = "Had a lovely dinner with family.",
        images = listOf("https://picsum.photos/id/240/200/300"),
        date = Timestamp(1748200000, 800000000)
    ),
    Diary(
        id = "f1g2h3j4k5l6m7n8o9p0",
        ownerId = "DZigB2wamXSIt3NZkM9IYsWL0263",
        mood = "Humorous",
        title = "Reflecting",
        description = "Spent some time journaling and reflecting on the week.",
        images = emptyList(),
        date = Timestamp(1748600000, 700000000)
    )
)