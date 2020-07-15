package com.example.NoteAppcleanArach.framework.presentaion.notedetails.state

import android.os.Parcelable
import com.example.NoteAppcleanArach.businees.doman.model.Note
import kotlinx.android.parcel.Parcelize


@Parcelize
data class NoteDetailsViewState(
    var note:Note? =null,
    var isUpdatePending:Boolean? = null
):Parcelable