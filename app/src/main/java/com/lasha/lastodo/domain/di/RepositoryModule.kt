package com.lasha.lastodo.domain.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lasha.lastodo.data.repository.RepositoryImpl
import com.lasha.lastodo.domain.db.TodosDao
import com.lasha.lastodo.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesRoomRepositry(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore,todosDao: TodosDao): Repository{
        return RepositoryImpl(todosDao, firebaseAuth, firestore)
    }
}