package com.example.NoteAppcleanArach.businees.doman.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    val id:String,
    val title:String,
    val body:String,
    val createdAt:String,
    val updatedAt:String
):Parcelable