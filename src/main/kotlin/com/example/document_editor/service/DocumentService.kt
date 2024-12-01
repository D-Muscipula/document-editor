package com.example.document_editor.service

import com.example.document_editor.model.Document
import com.example.document_editor.repository.DocumentRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class DocumentService(private val documentRepository: DocumentRepository) {

    fun findAll(): List<Document> = documentRepository.findAll()

    fun findByIdAndTitle(id: UUID, title: String): Document? {
        var document = documentRepository.findById(id).orElse(null)
        if (document != null && document.title != title) {
            document = null
        }
        return document
    }

    fun save(title: String): Document {
        val document = Document(title = title)
        return documentRepository.save(document)
    }

    fun saveDocument(document: Document) {
        documentRepository.save(document)
    }

    fun deleteById(id: UUID) = documentRepository.deleteById(id)

    fun existsById(id: UUID): Boolean = documentRepository.existsById(id)
}