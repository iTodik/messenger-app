package ge.itodadze.messengerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.viewmodel.callback.SignInCallbackHandler
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener

class SignInViewModel(private val usersRepository: UsersRepository): ViewModel() {

    fun trySignIn(nickname: String?, passwordHash: String?) {
        usersRepository.get(nickname,
            SignInCallbackHandler(
                User(nickname=nickname, passwordHash=passwordHash),
                object : CallbackListener {
                    override fun onSuccess() {
                        // successful sign in
                    }

                    override fun onFailure(message: String?) {
                        // unsuccessful sign in
                    }
                })
        )
    }

    companion object {
        fun getSignInViewModelFactory(): SignInViewModelFactory {
            return SignInViewModelFactory()
        }
    }

}

@Suppress("UNCHECKED_CAST")
class SignInViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignInViewModel(UsersFirebaseRepository()) as T
    }
}