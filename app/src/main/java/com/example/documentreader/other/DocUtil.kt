package com.example.documentreader.other

import androidx.documentfile.provider.DocumentFile
import com.example.documentreader.db.FavDocs

object DocUtil {
    fun getDocFormat(string: String): DocFormats? {
        return when (string) {
        "pdf" -> DocFormats.PDF
        "docx" -> DocFormats.DOC
        "ppt" -> DocFormats.PPT
        "pptx" -> DocFormats.PPT
        "txt" -> DocFormats.TXT
        else -> null
        }
    }

    fun DocumentFile.compareUri(fav: List<FavDocs>?) = fav?.any {
        it.uri == this.uri.toString()
    }
    fun updateFavorite(favList: List<FavDocs>, allDocsList: List<DocumentFile>): List<FavDocs> {
        return favList.filter { favDoc->
            !allDocsList.any {
                it.uri.toString() == favDoc.uri
            }
        }
    }

    fun getAllFavorite(favList: List<FavDocs>, allDocsList: List<DocumentFile>): List<DocumentFile> {
        return allDocsList.filter { allDocs->
            favList.any {
                it.uri == allDocs.uri.toString()
            }
        }
    }

}