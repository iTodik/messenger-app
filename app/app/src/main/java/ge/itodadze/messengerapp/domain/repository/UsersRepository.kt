package ge.itodadze.messengerapp.domain.repository

import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.callback.CallbackHandler

interface UsersRepository {
    fun get(id: String?, handler: CallbackHandler<User>?)
    fun getByNickname(nickname: String?, handler: CallbackHandler<User>?)
    fun add(user: User?, handler: CallbackHandler<User>?)
}