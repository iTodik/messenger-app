package ge.itodadze.messengerapp.domain.repository

import ge.itodadze.messengerapp.data.models.User

interface UsersRepository {
    fun requestUsername(user: User)
    fun requestUsernamePassword(user: User)
    fun add(user: User): Boolean
}