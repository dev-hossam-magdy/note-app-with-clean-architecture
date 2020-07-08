package com.example.NoteAppcleanArach.businees.doman.state


interface StateEvent {

    fun errorInfo(): String

    fun eventName(): String

    fun shouldDisplayProgressBar(): Boolean
}