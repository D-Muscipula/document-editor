package com.example.document_editor.dto

data class Message(var delta: Any?)

data class Messages(
    var messages: MutableList<Message> = mutableListOf()
)