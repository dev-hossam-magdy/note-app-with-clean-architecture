package com.example.NoteAppcleanArach.businees.doman.model

import com.example.NoteAppcleanArach.businees.doman.util.getCurrentTimeStamp
import java.util.*
import kotlin.collections.ArrayList


fun createNote(id: String?, title: String, body: String?): Note =
    Note(
        id = id ?: UUID.randomUUID().toString(),
        title = title,
        body = body ?: "",
        createdAt = getCurrentTimeStamp(),
        updatedAt = getCurrentTimeStamp()
    )

fun createNoteList(numberOfNotes: Int): List<Note> {
    val list = ArrayList<Note>()
    for (i in 0..numberOfNotes)
        list.add(
            createNote(null, UUID.randomUUID().toString(), UUID.randomUUID().toString())
        )
    return list
}

