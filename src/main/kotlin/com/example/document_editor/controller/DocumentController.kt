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

    @GetMapping
    fun getAllDocuments(): ResponseEntity<List<Document>> {
        val documents = documentService.findAll()
        return ResponseEntity.ok(documents)
    }

    @GetMapping("/{title}/{id}")
    fun getDocumentByTitleAndId(@PathVariable title: String, @PathVariable id: UUID, model: Model): String {
        val document = documentService.findByIdAndTitle(id, title)
        model.addAttribute("document", document)
        return "document.html"
//        return if (document != null) {
//            ResponseEntity.ok(document)
//        } else {
//            ResponseEntity.notFound().build()
//        }
    }

    @PostMapping("/{title}")
    fun createDocument(@PathVariable title: String): ResponseEntity<Document> {
        val createdDocument = documentService.save(title)
        return ResponseEntity.ok(createdDocument)
    }

//    @PutMapping("/{id}")
//    fun updateDocument(
//        @PathVariable id: UUID,
//        @RequestBody updatedDocument: Document
//    ): ResponseEntity<Document> {
//        val document = documentService.findById(id)
//        return if (document != null) {
//            document.updateTitle(updatedDocument.title)
//            val savedDocument = documentService.save(document)
//            ResponseEntity.ok(savedDocument)
//        } else {
//            ResponseEntity.notFound().build()
//        }
//    }

    @DeleteMapping("/{id}")
    fun deleteDocument(@PathVariable id: UUID): ResponseEntity<Void> {
        return if (documentService.existsById(id)) {
            documentService.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}