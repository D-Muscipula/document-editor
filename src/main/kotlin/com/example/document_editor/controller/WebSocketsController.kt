package com.example.document_editor.controller

import com.example.document_editor.dto.Message
import com.example.document_editor.dto.OutputMessage
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

@Controller
class WebSocketsController {
    @MessageMapping("/update/{document-name}/{uuid}")
    @SendTo("/topic/deltas/{document-name}/{uuid}")
    @Throws(Exception::class)
    fun delta(@Payload message: Message, @DestinationVariable uuid: String): OutputMessage {
        println("uuid: $uuid")
        return OutputMessage(message.delta)
    }

    @GetMapping("/documents")
    fun getDocumentsPage(model: Model): String {
        // Возвращает имя шаблона, которое будет использоваться для рендеринга HTML
        val myUUID = UUID.randomUUID().toString()
        val myName = "John Doe"

        model.addAttribute("uuid", myUUID)
        model.addAttribute("name", myName)

        return "index.html"
    }

}