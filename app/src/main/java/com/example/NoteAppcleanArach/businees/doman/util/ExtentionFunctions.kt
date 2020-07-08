package com.example.NoteAppcleanArach.businees.doman.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun String.removeTimeFormStringDate():String =
    substring(0 , this.indexOf(" "))

fun Timestamp.convertFirebaseTimeStampToString():String{
    val simpleDateFormat = SimpleDateFormat()
    return simpleDateFormat.format(this.toDate())
}

fun String.ConvertStringToTimeStamp():Timestamp{
    val simpleDateFormat = SimpleDateFormat()
    return Timestamp(simpleDateFormat.parse(this))
}

fun getCurrentTimeStamp():String = SimpleDateFormat().format(Date())