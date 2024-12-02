package com.example.document_editor.controller

import com.example.document_editor.model.Document
import com.example.document_editor.service.DocumentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@RequestMapping("/api/documents")
class DocumentController @Autowired constructor(
    private val documentService: DocumentService
) {

    @GetMapping("/{title}/{id}")
    fun getDocumentByTitleAndId(@PathVariable title: String, @PathVariable id: UUID, model: Model): String {
        val document = documentService.findByIdAndTitle(id, title)
        return if (document != null) {
            model.addAttribute("document", document)
            "document.html"
        } else {
            model.addAttribute("errorMessage", "Document not found")
            "document-not-found.html"
        }
    }


    @PostMapping("/{title}")
    fun createDocument(@PathVariable title: String): ResponseEntity<Document> {
        val createdDocument = documentService.save(title)
        return ResponseEntity.ok(createdDocument)
    }
}