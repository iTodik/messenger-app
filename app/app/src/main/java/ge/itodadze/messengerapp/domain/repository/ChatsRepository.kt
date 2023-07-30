package ge.itodadze.messengerapp.domain.repository

import ge.itodadze.messengerapp.view.model.ViewChat
import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler
import ge.itodadze.messengerapp.viewmodel.models.Chat
import ge.itodadze.messengerapp.viewmodel.models.Message

interface ChatsRepository {

    fun getUsersChats(user_id: String?, handler: CallbackHandler<MutableList<ViewChat>>?)

    fun getChat(chat_id: String?, handler: CallbackHandler<Chat>?)

    fun addChat(chat: Chat?, first_id: String?, second_id: String?, handler: CallbackHandler<Chat>?)

    fun addMessage(chat_id: String?, message: Message?, handler: CallbackHandler<Message>?)

}