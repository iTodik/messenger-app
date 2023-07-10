package ge.itodadze.messengerapp.viewmodel

import androidx.lifecycle.*
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.viewmodel.callback.SignUpCallbackHandler
import ge.itodadze.messengerapp.viewmodel.callback.SignUpCheckExistsCallbackHandler
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener
import kotlinx.coroutines.launch

class SignUpViewModel(val usersRepository: UsersRepository): ViewModel() {

    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String>
        get() = _failure

    private val _signedUpNickname = MutableLiveData<String>()
    val signedUpNickname: LiveData<String>
        get() = _signedUpNickname

    fun trySignUp(nickname: String?, passwordHash: String?, profession: String?) {
        usersRepository.get(nickname, SignUpCheckExistsCallbackHandler(
            object: CallbackListener {
                override fun onSuccess() {
                    usersRepository.add(
                        User(nickname=nickname, passwordHash=passwordHash, profession=profession),
                    SignUpCallbackHandler(object: CallbackListener {
                        override fun onSuccess() {
                            viewModelScope.launch{
                                _signedUpNickname.value = nickname
                            }
                        }

                        override fun onFailure(message: String?) {
                            viewModelScope.launch{
                                _failure.value = message
                            }
                        }
                    })
                    )
                }

                override fun onFailure(message: String?) {
                    viewModelScope.launch{
                        _failure.value = message
                    }
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