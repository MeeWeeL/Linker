package domain.impl

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

class MessageManager {

    private val _messages = Channel<String?>()
    val messages: Flow<String?> get() = _messages.consumeAsFlow()

    fun sendMessage(text: String) {
        _messages.trySend(text)
    }
}