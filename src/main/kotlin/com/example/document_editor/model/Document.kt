package com.example.document_editor.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "documents")
data class Document(

    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    val creationDate: LocalDateTime = LocalDateTime.now(),

    @Column
    var lastModifiedDate: LocalDateTime = creationDate // Инициализация датой создания
) {
    // Сеттер для title обновляет и дату последнего редактирования
    fun updateTitle(newTitle: String) {
        this.title = newTitle
        this.lastModifiedDate = LocalDateTime.now()
    }
}
