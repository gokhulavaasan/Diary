package com.example.diary.di

import android.content.Context
import androidx.room.Room
import com.example.diary.data.local.database.ImagesDatabase
import com.example.diary.data.remote.repository.DiaryRepositoryImpl
import com.example.diary.domain.repository.DiaryRepository
import com.example.diary.domain.usecases.DiaryUseCases
import com.example.diary.util.Constants.IMAGES_DATABASE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth


    @Singleton
    @Provides
    fun providersDiaryRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
    ): DiaryRepository {
        return DiaryRepositoryImpl(
            firestore = firestore,
            auth = auth
        )
    }

    @Singleton
    @Provides
    fun providesDiaryUseCases(
        diaryRepository: DiaryRepository,
    ): DiaryUseCases {
        return DiaryUseCases(
            diaryRepository = diaryRepository
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): ImagesDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ImagesDatabase::class.java,
            name = IMAGES_DATABASE
        ).build()
    }

    @Singleton
    @Provides
    fun provideFirstDao(database: ImagesDatabase) = database.imageToUploadDao()

    @Singleton
    @Provides
    fun provideSecondDao(database: ImagesDatabase) = database.imageToDeleteDao()


}