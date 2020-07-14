package com.example.NoteAppcleanArach.framework.presentaion.notelist.state

import android.os.Parcelable
import com.example.NoteAppcleanArach.businees.doman.model.Note
import com.example.NoteAppcleanArach.businees.doman.state.ViewState
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteListViewState (
    var noteList: List<Note>? = null,
    var newNote: Note? = null,
    var notePendingDelete: NotePendingDelete? = null,
    var searchingQuery: String? = null,
    var pageNumber: Int? = null,
    var isQueryExhausted:Boolean? = null,
    var filter: String? =null,
    var order:String? = null,
    var layoutManager: Parcelable? = null,
    var namOfCachedNotes:Int? = null
):ViewState , Parcelable{

    @Parcelize
    data class NotePendingDelete(
        var note:Note? = null,
        var listPosition:Int? = null
    ):Parcelable

}