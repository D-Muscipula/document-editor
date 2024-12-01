package com.example.document_editor.model

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
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
    var lastModifiedDate: LocalDateTime = creationDate,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var contentDelta: String? = null
)