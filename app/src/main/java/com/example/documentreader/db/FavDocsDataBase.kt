package com.example.documentreader.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavDocs::class], version = 1)
abstract class FavDocsDataBase(): RoomDatabase() {
    abstract fun getDao(): FavDocsDao
}