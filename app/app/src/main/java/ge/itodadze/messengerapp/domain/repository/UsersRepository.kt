package ge.itodadze.messengerapp.domain.repository

import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.callback.RepositoryCallback

interface UsersRepository {
    fun get(nickname: String?, callback: RepositoryCallback<User>?)
    fun add(user: User?, callback: RepositoryCallback<User>?)
}