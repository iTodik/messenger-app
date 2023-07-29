package ge.itodadze.messengerapp.domain.repository

import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler
import ge.itodadze.messengerapp.viewmodel.models.Chat
import ge.itodadze.messengerapp.viewmodel.models.Message

interface ChatsRepository {

    // fun getUsersLastChats(user_id: String?, handler: CallbackHandler<List<Chat>>?)

    fun getChat(chat_id: String?, handler: CallbackHandler<Chat>?)

    fun addChat(chat: Chat?, first_id: String?, second_id: String?, handler: CallbackHandler<Chat>?)

    fun addMessage(chat_id: String?, message: Message?, handler: CallbackHandler<Message>?)

}