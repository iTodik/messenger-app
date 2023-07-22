package ge.itodadze.messengerapp.domain.repository

import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler
import ge.itodadze.messengerapp.viewmodel.models.Chat
import ge.itodadze.messengerapp.viewmodel.models.User

interface ChatsRepository {

    fun getUsersChatsByNickname(nickname: String?, handler: CallbackHandler<Chat>)
}