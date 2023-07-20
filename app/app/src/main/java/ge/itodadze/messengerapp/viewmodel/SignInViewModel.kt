package ge.itodadze.messengerapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.viewmodel.callback.SignInCallbackHandler
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import kotlinx.coroutines.launch

class SignInViewModel(private val logInManager: LogInManager,
                      private val usersRepository: UsersRepository): ViewModel() {

    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String>
        get() = _failure

    private val _signedInId = MutableLiveData<String>()
    val signedInId: LiveData<String>
        get() = _signedInId

    init {
        val isLogged: Boolean = logInManager.isCurrentlyLogged()
        if (isLogged) {
            _signedInId.value = logInManager.getLoggedUserId()
        }
    }

    fun trySignIn(nickname: String?, passwordHash: String?) {
        usersRepository.getByNickname(nickname,
            SignInCallbackHandler(
                User(nickname=nickname, passwordHash=passwordHash),
                object : CallbackListenerWithResult<User> {
                    override fun onSuccess(result: User) {
                        logInManager.logIn(result.identifier!!)
                        viewModelScope.launch{
                            _signedInId.value = result.identifier
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

    companion object {
        fun getSignInViewModelFactory(context: Context): SignInViewModelFactory {
            return SignInViewModelFactory(context)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class SignInViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignInViewModel(
            LogInManager(context.getSharedPreferences(LogInManager.FILE_NAME, Context.MODE_PRIVATE)),
            UsersFirebaseRepository(context.resources.getString(R.string.db_location))) as T
    }
}