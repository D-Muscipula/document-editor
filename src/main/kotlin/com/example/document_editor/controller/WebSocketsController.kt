package com.example.document_editor.controller

import com.example.document_editor.dto.Message
import com.example.document_editor.dto.Messages
import com.example.document_editor.dto.OutputMessage
import com.example.document_editor.model.Document
import com.example.document_editor.service.DocumentService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.*
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class WebSocketsController(private val documentService: DocumentService) {

    @MessageMapping("/update/{title}/{uuid}")
    @SendTo("/topic/deltas/{title}/{uuid}")
    fun delta(
        @Payload message: Message,
        @DestinationVariable title: String,
        @DestinationVariable uuid: String
    ): OutputMessage {
        println("uuid: $uuid")

        // Добавляем логику для обновления документа в базе данных
        //val delta: String? = message.delta as? String
        val objectMapper = jacksonObjectMapper().registerKotlinModule()

        // Преобразование объекта в строку JSON
        val jsonString: String = objectMapper.writeValueAsString(message)
        val documentFromDb = documentService.findByIdAndTitle(UUID.fromString(uuid), title)
        if (documentFromDb?.contentDelta == null) {
            val messages = Messages()
            messages.messages.add(message)
            val jsonStr: String = objectMapper.writeValueAsString(messages)
            val document = Document(id = UUID.fromString(uuid),
                title = title,
                contentDelta = jsonStr)
            documentService.saveDocument(document)
        } else {
            val messages: Messages = objectMapper.readValue(documentFromDb.contentDelta!!)
            messages.messages.add(message)
            val jsonStr: String = objectMapper.writeValueAsString(messages)
            val document = Document(
                id = UUID.fromString(uuid),
                title = title,
                contentDelta = jsonStr
            )
            documentService.saveDocument(document)
        }
        return OutputMessage(message.delta)
    }

    @GetMapping("/documents")
    fun getDocumentsPage(model: Model): String {
        val myUUID = UUID.randomUUID().toString()
        val myName = "John Doe"
        model.addAttribute("uuid", myUUID)
        model.addAttribute("name", myName)
        return "document.html"
    }

    @GetMapping("/document/{title}/{id}")
    fun getDocumentByTitleAndId(@PathVariable title: String, @PathVariable id: UUID): Document? {
        val document = documentService.findByIdAndTitle(id, title)
        return document
    }
}