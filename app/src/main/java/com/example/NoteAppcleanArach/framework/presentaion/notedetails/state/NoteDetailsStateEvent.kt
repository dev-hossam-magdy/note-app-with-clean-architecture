package com.example.NoteAppcleanArach.framework.presentaion.notedetails.state

import com.example.NoteAppcleanArach.businees.doman.state.StateEvent
import com.example.NoteAppcleanArach.businees.doman.state.StateMessage

sealed class NoteDetailsStateEvent :StateEvent{

    class UpdatingNoteEvent:NoteDetailsStateEvent(){
        override fun errorInfo(): String ="Error: UpdatingNoteEvent "

        override fun eventName(): String = "UpdatingNoteEvent"

        override fun shouldDisplayProgressBar():Boolean = true
    }

    class DeleteNoteEvent:NoteDetailsStateEvent(){
        override fun errorInfo(): String ="Error: DeleteNoteEvent"

        override fun eventName(): String= "DeleteNoteEvent"
        override fun shouldDisplayProgressBar(): Boolean = true
    }

    data class CreateStateMessageEvent(
        val stateMessage:StateMessage
    ):NoteDetailsStateEvent(){
        override fun errorInfo(): String = "Error: CreateStateMessageEvent"

        override fun eventName(): String = "CreateStateMessageEvent"

        override fun shouldDisplayProgressBar(): Boolean = false
    }
}