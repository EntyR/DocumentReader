package com.example.documentreader.repositories

import com.example.documentreader.other.DocFormats

data class SharedStorageDocument (
        var id: Int?,
        val name: String,
        val format: DocFormats,
        val date: String,
        val uri: String,
        var inFavor:Boolean
        )