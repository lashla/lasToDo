package com.lasha.lastodo.domain.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.lasha.lastodo.domain.db.TodoDatabase
import com.lasha.lastodo.domain.db.TodosDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): TodoDatabase{
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            TodoDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTodosDao(todoDatabase: TodoDatabase): TodosDao{
        return todoDatabase.todosDao()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

}