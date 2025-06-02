package com.example.diary.presentation.write

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.data.local.database.ImageToDeleteDao
import com.example.diary.data.local.database.ImageToUploadDao
import com.example.diary.data.local.database.entity.ImageToDelete
import com.example.diary.data.local.database.entity.ImageToUpload
import com.example.diary.domain.model.Diary
import com.example.diary.domain.model.GalleryImage
import com.example.diary.domain.model.GalleryState
import com.example.diary.domain.model.Mood
import com.example.diary.domain.usecases.DiaryUseCases
import com.example.diary.util.Constants.diaryId
import com.example.diary.util.Resource
import com.example.diary.util.fetchImagesFromFirebase
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val diaryUseCases: DiaryUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val imageToUploadDao: ImageToUploadDao,
    private val imageToDeleteDao: ImageToDeleteDao,
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    val galleryState = GalleryState()

    init {
        getDiaryArgument()
        fetchSelectedDiary()
    }

    private fun fetchSelectedDiary() {
        if (uiState.selectedDiaryId != null) {
            viewModelScope.launch {
                diaryUseCases
                    .getDiary(uiState.selectedDiaryId.toString())
                    .collect() { result ->
                        when (result) {
                            is Resource.Error -> {}
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                val diary = result.data
                                if (diary != null) {
                                    uiState = uiState.copy(
                                        selectedDiary = diary,
                                        title = diary.title,
                                        description = diary.description,
                                        mood = Mood.valueOf(diary.mood),
                                    )

                                }
                                fetchImagesFromFirebase(
                                    remoteImagePaths = diary?.images ?: emptyList(),
                                    onImageDownloaded = { downloadedImage ->
                                        galleryState.addImage(
                                            GalleryImage(
                                                image = downloadedImage,
                                                remoteImagePath = extractImagePath(downloadedImage.toString())
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
            }
        }

    }

    private fun extractImagePath(fullImageUrl: String): String {
        val chunks = fullImageUrl.split("%2F")
        val imageName = chunks[2].split("?").first()
        return "images/${com.google.firebase.ktx.Firebase.auth.currentUser?.uid}/$imageName"
    }

    private fun getDiaryArgument() {
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(
                key = diaryId
            )
        )
    }

    private fun setSelectedDiary(diary: Diary) {
        uiState = uiState.copy(selectedDiary = diary)
    }

    fun setTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun setDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    private fun setMood(mood: Mood) {
        uiState = uiState.copy(mood = mood)
    }

    fun updateDateTime(timeStamp: Timestamp) {
        uiState = uiState.copy(updatedDateTime = timeStamp)
    }

    fun upsertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            if (uiState.selectedDiaryId != null) {
                updateDiary(diary, onSuccess, onError)
            } else {
                insertDiary(diary, onSuccess, onError)
            }
        }
    }

    fun deleteDiary(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            diaryUseCases.deleteDiary(uiState.selectedDiaryId.toString()).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            onError(result.message.toString())
                        }
                    }

                    is Resource.Loading<*> -> {}
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            uiState.selectedDiary?.let {
                                deleteImagesFromFirebase(images = it.images)
                            }
                            onSuccess()
                        }
                    }
                }
            }

        }
    }

    suspend fun insertDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        diaryUseCases.insertDiary(diary).collect { result ->
            when (result) {
                is Resource.Error -> {
                    withContext(Dispatchers.Main) {
                        onError(result.message.toString())
                    }
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    uploadImageToFirebase()
                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                }
            }
        }
    }

    suspend fun updateDiary(
        diary: Diary,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        diaryUseCases.updateDiary(diary).collect { result ->
            when (result) {
                is Resource.Error -> {
                    withContext(Dispatchers.Main) {
                        onError(result.message.toString())
                    }
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    uploadImageToFirebase()
                    withContext(Dispatchers.Main) {
                        deleteImagesFromFirebase()
                        onSuccess()
                    }

                }
            }
        }
    }

    private fun deleteImagesFromFirebase(images: List<String>? = null) {
        val storage = FirebaseStorage.getInstance().reference
        if (images != null) {
            images.forEach { remotePath ->
                storage.child(remotePath).delete()
                    .addOnFailureListener {
                        viewModelScope.launch(Dispatchers.IO) {
                            imageToDeleteDao.addImageToDelete(
                                ImageToDelete(remoteImagePath = remotePath)
                            )
                        }
                    }
            }
        } else {
            galleryState.imagesToBeDeleted.map { it.remoteImagePath }.forEach { remotePath ->
                storage.child(remotePath).delete()
                    .addOnFailureListener {
                        viewModelScope.launch(Dispatchers.IO) {
                            imageToDeleteDao.addImageToDelete(
                                ImageToDelete(remoteImagePath = remotePath)
                            )
                        }
                    }
            }
        }
    }

    fun addImage(
        image: Uri,
        imageType: String,
    ) {
        val remoteImagePath =
            "images/${Firebase.auth.currentUser?.uid}/${image.lastPathSegment}" +
                    "-${System.currentTimeMillis()}.${imageType}"
        galleryState.addImage(
            galleryImage = GalleryImage(
                image = image,
                remoteImagePath = remoteImagePath
            )
        )
    }

    private fun uploadImageToFirebase() {
        val storage = FirebaseStorage.getInstance().reference
        galleryState.images.forEach { galleryImage ->
            val imagePath = storage.child(galleryImage.remoteImagePath)
            imagePath.putFile(galleryImage.image)
                .addOnProgressListener {
                    val sessionUri = it.uploadSessionUri
                    if (sessionUri != null) {
                        Log.d("storageUpload", sessionUri.toString())
                        viewModelScope.launch(Dispatchers.IO) {
                            imageToUploadDao.addImageToUpload(
                                ImageToUpload(
                                    remoteImagePath = galleryImage.remoteImagePath,
                                    imageUri = galleryImage.image.toString(),
                                    sessionUri = sessionUri.toString()
                                )
                            )
                        }
                    }
                }
        }
    }
}


data class UiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,
    val updatedDateTime: Timestamp? = null,
)