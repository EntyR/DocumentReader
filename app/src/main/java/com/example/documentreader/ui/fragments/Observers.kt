package com.example.documentreader.ui.fragments

import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.Observer
import com.anggrayudi.storage.file.extension
import com.example.documentreader.adapters.DocListAdapter
import com.example.documentreader.db.FavDocs
import com.example.documentreader.other.DocUtil
import com.example.documentreader.other.TimeUtil
import com.example.documentreader.other.getDocFormatFromExtension
import com.example.documentreader.repositories.SharedStorageDocument

class SetupRecyclerViewObserver(val favDocks: List<FavDocs>, val adapter: DocListAdapter): Observer<List<DocumentFile>> {
    override fun onChanged(t: List<DocumentFile>?) {
        val list = t?.map { doc ->
            val dockey = favDocks.find {
                it.uri == doc.uri.toString()
            }?.key
            SharedStorageDocument(
                dockey,
                doc.name ?: "no-name",
                getDocFormatFromExtension(doc.extension),
                TimeUtil.getDateStringFromMillis(doc.lastModified()),
                doc.uri.toString(),
                dockey != null


            )
        }
        adapter.submitList(list)

    }

}
class SetupFavoriteRecyclerViewObserver(val adapter: DocListAdapter): Observer<Pair<List<FavDocs>,List<DocumentFile>>> {
    override fun onChanged(t: Pair<List<FavDocs>,List<DocumentFile>>?) {
        val a = DocUtil.getAllFavorite(t?.first?: listOf(), t?.second?: listOf())
        val list = a.map { doc ->
            val dockey = t?.first?.find {
                it.uri == doc.uri.toString()
            }?.key
            SharedStorageDocument(
                dockey,
                doc.name ?: "no-name",
                getDocFormatFromExtension(doc.extension),
                TimeUtil.getDateStringFromMillis(doc.lastModified()),
                doc.uri.toString(),
                dockey != null


            )
        }
        adapter.submitList(list)

    }

}

