package ge.itodadze.messengerapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.domain.repository.ChatsFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.ChatsRepository
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.view.model.ViewChat
import ge.itodadze.messengerapp.view.model.ViewUser
import ge.itodadze.messengerapp.viewmodel.callback.GetLastChatsCallbackHandler
import ge.itodadze.messengerapp.viewmodel.callback.GetUserCallbackHandler
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.Chat
import ge.itodadze.messengerapp.viewmodel.models.User
import kotlinx.coroutines.launch



class FrontPageViewModel(private val logInManager: LogInManager,
                         private val usersRepository: UsersRepository,
                         private val chatsRepository: ChatsRepository
): ViewModel() {

    private val _logId = MutableLiveData<String?>()
    val logId: LiveData<String?>
        get() = _logId

    private val _lastChats = MutableLiveData<List<ViewChat>>()
    val lastChats: LiveData<List<ViewChat>>
        get() = _lastChats

    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String>
        get() = _failure

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

    fun getLastChats(user_id:String?){
        chatsRepository.getUsersLastChats(user_id, GetLastChatsCallbackHandler(
            object: CallbackListenerWithResult<List<Chat>>{
                override fun onSuccess(result: List<Chat>) {
                    val list:MutableList<ViewChat> = arrayListOf()
                    for (elem in result){
                        var userView: ViewUser? = null
                        usersRepository.get(user_id, GetUserCallbackHandler(
                            object : CallbackListenerWithResult<User>{
                                override fun onSuccess(resultUser: User) {
                                    userView = ViewUser(resultUser.nickname, resultUser.profession, resultUser.imgUri)
                                }

                                override fun onFailure(message: String?) {
                                    viewModelScope.launch{
                                        _failure.value = message
                                    }
                                }
                            }
                        ))
                        list.add(ViewChat(userView, elem.messages?.get(elem.messages.size-1)))

                    }

                    _lastChats.value = list
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