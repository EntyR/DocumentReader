package com.example.documentreader.other

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    fun getDateStringFromMillis(millis: Long): String{
        val formater = SimpleDateFormat("dd/mm/yyyy")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return formater.format(calendar.time)
    }
}