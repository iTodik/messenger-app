package ge.itodadze.messengerapp.presenter

import ge.itodadze.messengerapp.data.models.User
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.presenter.listeners.UsersRepositoryListener

class SigningService(val usersRepository: UsersRepository): UsersRepositoryListener {

    fun signUp(user: User) {
        if (user.nickname == null || user.passwordHash == null
            || user.profession == null) return
        usersRepository.requestUsername(user)
    }

    fun signIn(user: User) {
        if (user.nickname == null || user.passwordHash == null) return
        usersRepository.requestUsernamePassword(user)
    }

    override fun onUsername(user: User?) {
        if (user != null) {
            usersRepository.add(user)
        } else {
            // error could not sign up
        }
    }

    override fun onUsernamePassword(user: User?) {
        if (user != null) {
            // after sign in
        } else {
            // error could not sign up
        }
    }


}