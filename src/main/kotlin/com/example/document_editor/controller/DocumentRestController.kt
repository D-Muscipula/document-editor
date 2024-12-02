package com.example.document_editor.controller

import com.example.document_editor.model.Document
import com.example.document_editor.service.DocumentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class DocumentRestController(private val documentService: DocumentService) {
    @GetMapping("/document/{title}/{id}")
    fun getDocumentByTitleAndId(@PathVariable title: String, @PathVariable id: UUID): Document? {
        val document = documentService.findByIdAndTitle(id, title)
        return document
    }
}