package ge.itodadze.messengerapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.domain.repository.ChatsFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.ChatsRepository
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.view.model.ViewUser
import ge.itodadze.messengerapp.viewmodel.callback.AddChatCallbackHandler
import ge.itodadze.messengerapp.viewmodel.callback.GetUserCallbackHandler
import ge.itodadze.messengerapp.viewmodel.callback.SearchUsersCallbackHandler
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.Chat
import ge.itodadze.messengerapp.viewmodel.models.User
import kotlinx.coroutines.launch

class SearchViewModel(private val logInManager: LogInManager,
                      private val usersRepository: UsersRepository,
                      private val chatsRepository: ChatsRepository): ViewModel() {

    private val _users = MutableLiveData<List<ViewUser>>()
    val users: LiveData<List<ViewUser>>
        get() = _users

    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String>
        get() = _failure

    private val _chatOpened = MutableLiveData<Pair<String, String>>()
    val chatOpened: LiveData<Pair<String, String>>
        get() = _chatOpened

    fun searchUsers(input: String) {
        usersRepository.getAll(SearchUsersCallbackHandler(
            object: CallbackListenerWithResult<List<User>>{
                override fun onSuccess(result: List<User>) {
                    val filtered: List<ViewUser> = result
                        .filter{user -> user.nickname!!.startsWith(input)}
                        .map{user -> ViewUser.fromUser(user) }
                    if (filtered.isEmpty()) {
                        onFailure("No users found with a given prefix")
                    } else {
                        viewModelScope.launch{
                            _users.value = filtered
                        }
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

    fun openChatWith(user: ViewUser) {
        val logId: String? = logInManager.getLoggedUserId()
        if (logId == null) {
            viewModelScope.launch{
                _failure.value = "log in before trying to chat with someone"
            }
        } else {
            usersRepository.getByNickname(user.nickname,
                GetUserCallbackHandler(object : CallbackListenerWithResult<User> {
                    override fun onSuccess(result: User) {
                        val partnerId: String = result.identifier!!
                        chatsRepository.addChat(Chat(ArrayList(), null), logId, partnerId,
                            AddChatCallbackHandler(object : CallbackListenerWithResult<Chat> {
                                override fun onSuccess(result: Chat) {
                                    viewModelScope.launch{
                                        _chatOpened.value = Pair(result.identifier!!, partnerId)
                                    }
                                }
                                override fun onFailure(message: String?) {
                                    viewModelScope.launch {
                                        _failure.value = message
                                    }
                                }
                            }))
                    }
                    override fun onFailure(message: String?) {
                        viewModelScope.launch {
                            _failure.value = "conversation partner:$message"
                        }
                    }
                }))
        }
    }

    companion object {
        fun getSearchViewModelFactory(context: Context): SearchViewModelFactory {
            return SearchViewModelFactory(context)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            LogInManager(context.getSharedPreferences(LogInManager.FILE_NAME, Context.MODE_PRIVATE)),
            UsersFirebaseRepository(context.resources.getString(R.string.db_location)),
            ChatsFirebaseRepository(context.resources.getString(R.string.db_location))
        ) as T
    }
}