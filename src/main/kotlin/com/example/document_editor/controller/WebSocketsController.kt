package com.example.document_editor.controller

import com.example.document_editor.model.Message
import com.example.document_editor.model.OutputMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class WebSocketsController {
    @MessageMapping("/update")
    @SendTo("/topic/deltas")
    @Throws(Exception::class)
    fun delta(message: Message): OutputMessage {
        println("is ok")
        return OutputMessage(message.name)
    }
}