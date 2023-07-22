package ge.itodadze.messengerapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.domain.repository.ChatsFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.ChatsRepository
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.view.model.ViewUser
import kotlinx.coroutines.launch


/////////////////// NEEDS FINISHING AFTER ADDING CHAT REPOSITORY

class FrontPageViewModel(private val logInManager: LogInManager,
                         private val usersRepository: UsersRepository,
                         private val chatsRepository: ChatsRepository
): ViewModel() {

    private val _logId = MutableLiveData<String?>()
    val logId: LiveData<String?>
        get() = _logId

    private val _users = MutableLiveData<List<ViewUser>>()
    val users: LiveData<List<ViewUser>>
        get() = _users

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

    fun getChats(nickname: String?){
        // to do

    }

    companion object {
        fun getFrontPageViewModelFactory(context: Context): FrontPageViewModelFactory {
            return FrontPageViewModelFactory(context)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class FrontPageViewModelFactory(private val context: Context): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FrontPageViewModel(
            LogInManager(context.getSharedPreferences(LogInManager.FILE_NAME, Context.MODE_PRIVATE)),
            UsersFirebaseRepository(context.resources.getString(R.string.db_location)),
            ChatsFirebaseRepository(context.resources.getString(R.string.db_location))
        ) as T
    }
}