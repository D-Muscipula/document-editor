package com.example.document_editor.controller

import com.example.document_editor.dto.Message
import com.example.document_editor.dto.OutputMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebSocketsController {
    @MessageMapping("/update")
    @SendTo("/topic/deltas")
    @Throws(Exception::class)
    fun delta(message: Message): OutputMessage {
        println("is ok")
        return OutputMessage(message.delta)
    }

    @GetMapping("/documents")
    fun getDocumentsPage(): String {
        // Возвращает имя шаблона, которое будет использоваться для рендеринга HTML
        return "index.html"
    }

}