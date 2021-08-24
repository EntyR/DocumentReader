package com.example.documentreader.di

import android.content.Context
import androidx.room.Room
import com.example.documentreader.db.FavDocsDataBase
import com.example.documentreader.other.Constant.TABLE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFavDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
            app,
            FavDocsDataBase::class.java,
            TABLE_NAME
        )
            .allowMainThreadQueries()
            .build()
}