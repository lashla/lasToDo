package com.lasha.lastodo.domain.di

import com.lasha.lastodo.data.repository.RoomRepositoryImpl
import com.lasha.lastodo.domain.db.TodosDao
import com.lasha.lastodo.domain.repository.RoomRepository
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
    fun providesRoomRepositry(todosDao: TodosDao): RoomRepository{
        return RoomRepositoryImpl(todosDao)
    }
}