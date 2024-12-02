package com.example.document_editor.controller

import com.example.document_editor.dto.Message
import com.example.document_editor.dto.OutputMessage
import com.example.document_editor.model.Document
import com.example.document_editor.service.DocumentService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.*


@RestController
class WebSocketsController(private val documentService: DocumentService) {

    @MessageMapping("/update/{title}/{uuid}")
    @SendTo("/topic/deltas/{title}/{uuid}")
    fun delta(
        @Payload message: Message,
        @DestinationVariable title: String,
        @DestinationVariable uuid: String
    ): OutputMessage {

        val objectMapper = jacksonObjectMapper().registerKotlinModule()
        val jsonString: String = objectMapper.writeValueAsString(message)

        val document = Document(
            id = UUID.fromString(uuid),
            title = title,
            contentDelta = jsonString,
            lastModifiedDate = LocalDateTime.now(),
        )
        documentService.saveDocument(document)

        return OutputMessage(message.delta)
    }

}