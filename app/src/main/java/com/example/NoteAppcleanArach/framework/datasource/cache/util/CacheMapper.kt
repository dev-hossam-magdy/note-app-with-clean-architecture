package com.example.NoteAppcleanArach.framework.datasource.cache.util

import com.example.NoteAppcleanArach.businees.doman.model.Note
import com.example.NoteAppcleanArach.businees.doman.util.EntityMapper
import com.example.NoteAppcleanArach.framework.datasource.cache.model.NoteCacheEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheMapper
@Inject
constructor() : EntityMapper<NoteCacheEntity, Note> {

    fun entityListToNoteList(entityList: List<NoteCacheEntity>): List<Note> =
        entityList.mapIndexed { index, noteCacheEntity -> mapFromEntity(noteCacheEntity) }


    fun noteListTosEntityList(noteList: List<Note>): List<NoteCacheEntity> =
        noteList.mapIndexed { index, note -> mapToEntity(note) }


    override fun mapFromEntity(entity: NoteCacheEntity): Note =
        Note(
            id = entity.id,
            body = entity.body,
            title = entity.title,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )

    override fun mapToEntity(domainName: Note): NoteCacheEntity =
        NoteCacheEntity(
            id = domainName.id,
            body = domainName.body,
            title = domainName.title,
            createdAt = domainName.createdAt,
            updatedAt = domainName.updatedAt
        )


}