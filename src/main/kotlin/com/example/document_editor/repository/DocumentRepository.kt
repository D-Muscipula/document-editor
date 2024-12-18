package com.example.document_editor.repository

import com.example.document_editor.model.Document
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DocumentRepository : JpaRepository<Document, UUID>