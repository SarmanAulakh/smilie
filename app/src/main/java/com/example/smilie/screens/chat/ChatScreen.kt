package com.example.smilie.screens.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.smilie.model.Metric
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val openaiToken = "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"

// Adapted from https://github.com/easy-tuto/Android_ChatGPT/tree/main
@Composable
fun ChatScreen(
    metrics: ArrayList<Metric>?
) {
    val editText = remember { mutableStateOf("") }
    val messageList: MutableList<Message> = remember { mutableStateListOf() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "RESOURCES CHAT",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.Bold,
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f),
                contentPadding = PaddingValues(16.dp)
            ) {
                itemsIndexed(messageList) { _, message ->
                    MessageBubble(message)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){
                EditTextField(editText)
                IconButton(onClick = {
                    sendMessage(messageList, editText)
                }) {
                    Icon(imageVector = Icons.Rounded.Send, contentDescription = "")
                }
            }
        }
    }
}

fun sendMessage(messageList: MutableList<Message>, editText: MutableState<String>) {
    if (editText.value.isNotEmpty()) {
        val userMessage = Message(editText.value.trim(), Message.SENT_BY_ME)
        messageList.add(userMessage)
        editText.value = ""

        CoroutineScope(Dispatchers.IO).launch {
            var prompt = ""

            // Retrieves past 10 messages as memory for conversation
            for (i in maxOf(messageList.size - 10, 0) until messageList.size) {
                prompt += if (messageList[i].sentBy == Message.SENT_BY_ME) {
                    "user: "
                } else {
                    "system: "
                }
                prompt += messageList[i].text + "\n "
            }
            Log.d("ChatGPT Prompt", prompt)

            var chatbotMessage = Message("Typing... ", Message.SENT_BY_BOT)
            messageList.add(chatbotMessage)

            val openai = OpenAI(openaiToken)
            val completionRequest = CompletionRequest(
                model = ModelId("text-davinci-003"),
                prompt = prompt,
                maxTokens = 250,
                echo = false
            )
            val completion = openai.completion(completionRequest)
            // Removes the typing... message
            if (messageList.size > 0) {
                messageList.removeLast()
            }

            Log.d("ChatGPT before", completion.choices[0].text)
            val completionRes = completion.choices[0].text.substringAfter(":").trim()
            chatbotMessage = Message(completionRes, Message.SENT_BY_BOT)
            messageList.add(chatbotMessage)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextField(editText: MutableState<String>) {
    TextField(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        value = editText.value,
        onValueChange = { editText.value = it },
        placeholder = { Text(text = "Ask questions about metrics") },
        singleLine = false
    )
}

@Composable
fun MessageBubble(message: Message) {
    val bubbleColor = if (message.sentBy == Message.SENT_BY_ME) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    val textColor = MaterialTheme.colorScheme.onBackground

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = if (message.sentBy == Message.SENT_BY_ME) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(8.dp)
                .background(
                    color = bubbleColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
        ) {
            Text(
                text = message.text,
                color = textColor,
            )
        }
    }
}