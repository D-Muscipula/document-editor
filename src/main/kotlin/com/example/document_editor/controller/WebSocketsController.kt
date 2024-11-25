package com.example.document_editor.controller

import com.example.document_editor.model.OutputMessage
import com.example.document_editor.model.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.util.HtmlUtils

@Controller
class WebSocketsController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    @Throws(Exception::class)
    fun greeting(message: Message): OutputMessage {
        println("is ok")
        return OutputMessage(message.name)
    }
}