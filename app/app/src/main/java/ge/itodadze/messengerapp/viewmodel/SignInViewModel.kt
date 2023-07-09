package ge.itodadze.messengerapp.viewmodel

import androidx.lifecycle.ViewModel
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.viewmodel.callback.SignInCallback
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener

class SignInViewModel(private val usersRepository: UsersRepository): ViewModel() {

    fun trySignIn(nickname: String?, passwordHash: String?) {
        usersRepository.get(nickname,
            SignInCallback(
                User(nickname=nickname, passwordHash=passwordHash),
                object : CallbackListener {
                    override fun onSuccess() {
                        // successful sign in
                    }

                    override fun onFailure(message: String?) {
                        // unsuccessful sign in
                    }
                }))
    }

}