package com.example.documentreader.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.documentreader.R
import com.example.documentreader.databinding.FileItemBinding
import com.example.documentreader.db.FavDocs
import com.example.documentreader.other.DocFormats
import com.example.documentreader.repositories.SharedStorageDocument
import com.example.documentreader.ui.viewmodel.MainViewModel

class DocListAdapter(
    private val list: MainViewModel,
    private val onLikePressedCallback: (favDoc: FavDocs, delete: Boolean) -> Unit,
    private val onItemPressedCallback: (uri: String) -> Unit
) : ListAdapter<SharedStorageDocument, DocListAdapter.DocViewHolder>(Companion) {

    inner class DocViewHolder(val binding: FileItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<SharedStorageDocument>() {
        override fun areItemsTheSame(
            oldItem: SharedStorageDocument,
            newItem: SharedStorageDocument
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: SharedStorageDocument,
            newItem: SharedStorageDocument
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocViewHolder {
        return DocViewHolder(
            FileItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DocViewHolder, position: Int) {
        val doc = currentList[position]

        holder.binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        Log.e("height", holder.binding.root.measuredHeight.toString())
        if (doc.inFavor) holder.binding.likeBtn.setImageResource(R.drawable.ic_favorite_full_24)
        holder.binding.fileName.text = doc.name
        holder.binding.docTypeImg.setImageResource(
            when (doc.format) {
                DocFormats.DOC -> R.drawable.ic_msword_24
                DocFormats.PDF -> R.drawable.ic_pdf_24
                DocFormats.TXT -> R.drawable.ic_txt_24
                DocFormats.PPT -> R.drawable.ic_powerpoint_24
            }
        )
        holder.binding.root.setOnClickListener{
            onItemPressedCallback(doc.uri)
        }

        holder.binding.likeBtn.setOnClickListener {
            val item = FavDocs(doc.uri)

            when (doc.inFavor) {
                false -> {
                    //add to favorite
                    holder.binding.likeBtn.setImageResource(R.drawable.ic_favorite_full_24)
                    onLikePressedCallback(item, false)
                    doc.inFavor = true
                }
                true -> {

                    //delete from favorite
                    val favDock = list.findDoc(doc.uri)
                    holder.binding.likeBtn.setImageResource(R.drawable.ic_favorite_border_24)
                    onLikePressedCallback(favDock, true)
                    doc.inFavor = false
                }
            }


        }

    }
}