package com.example.documentreader.ui.viewmodel

import android.app.Application
import android.os.ParcelFileDescriptor
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anggrayudi.storage.file.DocumentFileCompat
import com.anggrayudi.storage.file.DocumentFileType
import com.anggrayudi.storage.file.search
import com.anggrayudi.storage.file.toRawFile
import com.example.documentreader.db.FavDocs
import com.example.documentreader.db.FavDocsDataBase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application, val dataBase: FavDocsDataBase
): AndroidViewModel(application) {


    val context = getApplication<Application>().applicationContext
    private val _fileList = MutableLiveData<List<DocumentFile>>()
    val fileList: LiveData<List<DocumentFile>> = _fileList



    val favDocksLiveData: LiveData<List<FavDocs>> = dataBase.getDao().getAllDocs()

    val favDocList = dataBase.getDao().getAllDocsList()

    fun getFavDocksList() = dataBase.getDao().getAllDocs()


    fun getAllDocs(){

        val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf")!!

        val root = DocumentFileCompat.getRootDocumentFile(context, "primary", true)
        Log.e("root", root?.uri.toString())
        val pdfArray = root?.search(true, DocumentFileType.FILE, arrayOf(mime))

        Log.e("pdfArray", pdfArray?.size.toString())
//        pdfArray?.filter { documentFile ->
//
//            favDocks?.value?.any {
//                it.uri == documentFile.uri.toString()
//            }?: false
//        }

        _fileList.value = pdfArray


    }

    fun findDoc(uri: String) = dataBase.getDao().findDoc(uri)


    fun check(){
        ParcelFileDescriptor.open(fileList.value!![0].toRawFile(context), ParcelFileDescriptor.MODE_READ_ONLY)
    }



    suspend fun addElement(favDocs: FavDocs){
        Log.e("addElement", "add")
        dataBase.getDao().insertDoc(favDocs)
    }

    suspend fun deleteElement(favDocs: FavDocs){
        dataBase.getDao().deleteDoc(favDocs)
    }





}