package ge.itodadze.messengerapp.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.*
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.view.model.ViewUser
import ge.itodadze.messengerapp.viewmodel.callback.GetUserCallbackHandler
import ge.itodadze.messengerapp.viewmodel.callback.UpdateUserCallbackHandler
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.User
import kotlinx.coroutines.launch

class ProfileViewModel(private val logInManager: LogInManager,
                       private val usersRepository: UsersRepository): ViewModel() {

    private val _logId = MutableLiveData<String?>()
    val logId: LiveData<String?>
        get() = _logId

    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String>
        get() = _failure

    private val _user = MutableLiveData<ViewUser>()
    val user: LiveData<ViewUser>
        get() = _user

    init {
        val isLogged: Boolean = logInManager.isCurrentlyLogged()
        viewModelScope.launch {
            if (isLogged) {
                viewModelScope.launch {
                    _logId.value = logInManager.getLoggedUserId()
                }
            } else {
                _logId.value = null
            }
        }
    }

    fun get(id: String) {
        usersRepository.get(id, GetUserCallbackHandler(
            object : CallbackListenerWithResult<User> {
                override fun onSuccess(result: User) {
                    viewModelScope.launch{
                        _user.value = ViewUser(result.nickname, result.profession, result.imgUri)
                    }
                }
                override fun onFailure(message: String?) {
                    viewModelScope.launch{
                        _failure.value = message
                    }
                }
            }
        ))
    }

    fun tryUpdate(userId: String?, img: Bitmap?, nickname: String?, profession: String?) {
        usersRepository.update(userId, nickname, profession, img, UpdateUserCallbackHandler(
            object : CallbackListener {
                override fun onSuccess() {
                    return
                }
                override fun onFailure(message: String?) {
                    viewModelScope.launch{
                        _failure.value = message
                    }
                }
            }))
    }

    fun trySignOut(userId: String?) {
        viewModelScope.launch {
            if (userId != null && _logId.value == userId) {
                logInManager.logOut()
            }
            _logId.value = null
        }
    }

    companion object {
        fun getProfileViewModelFactory(context: Context): ProfileViewModelFactory {
            return ProfileViewModelFactory(context)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(
            LogInManager(context.getSharedPreferences(LogInManager.FILE_NAME, Context.MODE_PRIVATE)),
            UsersFirebaseRepository(context.resources.getString(R.string.db_location))) as T
    }
}