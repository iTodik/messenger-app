package ge.itodadze.messengerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.viewmodel.callback.SignUpCallbackHandler
import ge.itodadze.messengerapp.viewmodel.callback.SignUpCheckExistsCallbackHandler
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener

class SignUpViewModel(val usersRepository: UsersRepository): ViewModel() {

    fun trySignUp(nickname: String?, passwordHash: String?, profession: String?) {
        usersRepository.get(nickname, SignUpCheckExistsCallbackHandler(
            object: CallbackListener {
                override fun onSuccess() {
                    usersRepository.add(
                        User(nickname=nickname, passwordHash=passwordHash, profession=profession),
                    SignUpCallbackHandler(object: CallbackListener {
                        override fun onSuccess() {
                            // Sign Up Done
                        }

                        override fun onFailure(message: String?) {
                            // Could Not Sign Up
                        }
                    })
                    )
                }

                override fun onFailure(message: String?) {
                    // User Already Exists
                }

            }
        ))
    }

    companion object {
        fun getSignUpViewModelFactory(): SignUpViewModelFactory {
            return SignUpViewModelFactory()
        }
    }

}

@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(UsersFirebaseRepository()) as T
    }
}