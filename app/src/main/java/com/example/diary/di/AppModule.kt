package com.example.diary.di

import com.example.diary.data.remote.repository.DiaryRepositoryImpl
import com.example.diary.domain.repository.DiaryRepository
import com.example.diary.domain.usecases.DiaryUseCases
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}