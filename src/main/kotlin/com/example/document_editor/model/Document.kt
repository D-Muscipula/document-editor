package com.example.document_editor.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "documents")
data class Document(

    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var title: String,

    @Column
    val creationDate: LocalDateTime = LocalDateTime.now(),

    @Column
    var lastModifiedDate: LocalDateTime = creationDate
)
