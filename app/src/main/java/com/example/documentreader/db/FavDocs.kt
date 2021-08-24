package com.example.documentreader.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_doc")
data class FavDocs(
    var uri: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var key: Int? = null
}