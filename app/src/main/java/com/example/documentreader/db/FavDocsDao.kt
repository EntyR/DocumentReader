package com.example.documentreader.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FavDocsDao {

    @Query("SELECT * FROM fav_doc WHERE uri = :uriString")
    fun findDoc(uriString: String): FavDocs

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoc(doc: FavDocs)

    @Delete
    suspend fun deleteDoc(doc: FavDocs)

    @Delete
    suspend fun deleteDocs(dco: List<FavDocs>)

    @Query("SELECT * FROM fav_doc")
    fun getAllDocs(): LiveData<List<FavDocs>>

    @Query("SELECT * FROM fav_doc")
    fun getAllDocsList(): List<FavDocs>
}