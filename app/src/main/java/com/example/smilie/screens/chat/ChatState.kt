package com.example.smilie.screens.chat

class Message(var text: String, var sentBy: String) {

    companion object {
        var SENT_BY_ME = "me"
        var SENT_BY_BOT = "bot"
    }
}