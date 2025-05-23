package com.example.diary.data.remote.repository

import android.util.Log
import com.example.diary.data.remote.mappers.toDiary
import com.example.diary.domain.model.Diary
import com.example.diary.domain.repository.DiaryRepository
import com.example.diary.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class DiaryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : DiaryRepository {

    override fun getDiaries(): Flow<Resource<List<Diary>>> {
        return flow {
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
                        .orderBy("date", Query.Direction.DESCENDING)
//                        .whereEqualTo("ownerId", userId)
                        .get()
                        .await()

                    val diaries = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(DiaryDto::class.java)?.toDiary(doc.id)
                    }
                    emit(Resource.Success(diaries))
                } catch (e: Exception) {
                    Log.e("FirebaseQuery", "Error Fetching Data", e)
                    emit(Resource.Error(message = e.message.toString()))
                    return@flow
                }
            }
            return@flow
        }
    }

    override suspend fun addDiary(entry: Diary): Resource<Unit> {
        TODO("Not yet implemented")
    }
}