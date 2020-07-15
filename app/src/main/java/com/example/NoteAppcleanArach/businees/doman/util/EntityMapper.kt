package com.example.NoteAppcleanArach.businees.doman.util


interface EntityMapper <Entity,DomainModel>{

    fun mapFromEntity(entity: Entity) :DomainModel
    fun mapToEntity(domainName: DomainModel):Entity
}