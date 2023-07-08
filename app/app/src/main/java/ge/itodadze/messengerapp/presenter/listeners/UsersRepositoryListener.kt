package ge.itodadze.messengerapp.presenter.listeners

import ge.itodadze.messengerapp.data.models.User

interface UsersRepositoryListener {
    fun onUsername(user: User?)
    fun onUsernamePassword(user: User?)
}