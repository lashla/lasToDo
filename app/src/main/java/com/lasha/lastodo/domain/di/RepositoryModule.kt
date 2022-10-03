package com.lasha.lastodo.domain.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.lasha.lastodo.data.repository.RepositoryImpl
import com.lasha.lastodo.data.db.TodosDao
import com.lasha.lastodo.data.remote.RemoteService
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
    fun providesRoomRepositry(remoteService: RemoteService, todosDao: TodosDao): Repository{
        return RepositoryImpl(todosDao, remoteService)
    }
}