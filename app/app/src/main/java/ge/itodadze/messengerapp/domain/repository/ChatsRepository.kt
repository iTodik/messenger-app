package ge.itodadze.messengerapp.domain.repository

import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler
import ge.itodadze.messengerapp.viewmodel.models.Chat

interface ChatsRepository {

    fun getUsersLastChats(user_id: String?, handler: CallbackHandler<List<Chat>>?)

}