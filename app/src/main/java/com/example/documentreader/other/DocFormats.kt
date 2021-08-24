package com.example.documentreader.other

enum class DocFormats {
    TXT,
    DOC,
    PDF,
    PPT

}
fun getDocFormatFromExtension(extension: String): DocFormats{
    return when (extension){
        "pdf" -> DocFormats.PDF
        "txt" -> DocFormats.TXT
        "docx" -> DocFormats.DOC
        "ppt" -> DocFormats.PPT
        else -> DocFormats.TXT
    }


}
