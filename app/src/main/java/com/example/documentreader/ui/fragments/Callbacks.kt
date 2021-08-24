package com.example.documentreader.ui.fragments

import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.documentreader.db.FavDocs
import com.example.documentreader.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

object Callbacks {

    fun likePressed(
        lifecycleOwner: LifecycleOwner,
        docViewModel: MainViewModel
    ): (element: FavDocs, forDelete: Boolean) -> Unit {
        return { element, delete ->
            lifecycleOwner.lifecycleScope.launch {
                if (!delete) {
                    Timber.e("add")
                    docViewModel.addElement(element)
                } else {
                    docViewModel.deleteElement(element)
                    Timber.e("delete")
                }
            }
        }

    }

    fun goToReadActivity(launcher: ActivityResultLauncher<String>): (uri:String) -> Unit{
        return { uri ->
            launcher.launch(uri)
        }
    }
}