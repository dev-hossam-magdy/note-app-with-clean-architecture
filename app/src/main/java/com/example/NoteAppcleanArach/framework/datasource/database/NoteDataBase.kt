package com.example.NoteAppcleanArach.framework.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.NoteAppcleanArach.framework.datasource.cache.model.NoteCacheEntity

@Database(entities = [NoteCacheEntity::class] , exportSchema = false, version = 1)
abstract class NoteDataBase :RoomDatabase(){
    abstract fun getNotesDao():NoteDao

    companion object{
        const val DATEBASE_NAME = "note_dp"
    }
}