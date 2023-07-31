package ge.itodadze.messengerapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.google.firebase.database.ValueEventListener
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.domain.repository.ChatsFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.ChatsRepository
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.view.model.ViewChat
import ge.itodadze.messengerapp.view.model.ViewUser
import ge.itodadze.messengerapp.viewmodel.callback.*
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.Chat
import ge.itodadze.messengerapp.viewmodel.models.User
import ge.itodadze.messengerapp.viewmodel.models.UserChats
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

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _openChat = MutableLiveData<Pair<String, String>>()
    val openChat: LiveData<Pair<String, String>>
        get() = _openChat

    private var eventListener: ValueEventListener? = null

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

    fun getLastChats(userId: String?, filterNickname: String){
        viewModelScope.launch {
            _lastChats.value = emptyList()
            _loading.value = true
        }
         chatsRepository.getUsersChats(userId, GetUsersChatsCallbackHandler(
            object:CallbackListenerWithResult<UserChats>{
                override fun onSuccess(result: UserChats) {
                    if (result.chat_ids != null) {
                        val total: Int = result.chat_ids.size
                        var tracker = 0
                        for (chatAndPartner in result.chat_ids) {
                            usersRepository.get(chatAndPartner.partnerId,
                                GetUserCallbackHandler(object : CallbackListenerWithResult<User>{
                                    override fun onSuccess(result: User) {
                                        viewModelScope.launch{
                                            tracker += 1
                                            if (tracker == total) {
                                                _loading.value = false
                                            }
                                        }
                                        val user: User = result
                                        if (user.nickname != null && user.nickname.startsWith(filterNickname)) {
                                            chatsRepository.getChat(chatAndPartner.chatId,
                                                GetChatCallbackHandler(object :
                                                    CallbackListenerWithResult<Chat> {
                                                    override fun onSuccess(result: Chat) {
                                                        viewModelScope.launch {
                                                            val list =
                                                                _lastChats.value?.toMutableList()
                                                            if (result.messages != null && result.messages.size > 0) {
                                                                list?.add(
                                                                    ViewChat(
                                                                        result.identifier,
                                                                        ViewUser.fromUser(user),
                                                                        result.messages[result.messages.size - 1]
                                                                    )
                                                                )
                                                            } else {
                                                                list?.add(
                                                                    ViewChat(
                                                                        result.identifier,
                                                                        ViewUser.fromUser(user),
                                                                        null
                                                                    )
                                                                )
                                                            }
                                                            _lastChats.value = list
                                                        }
                                                    }

                                                    override fun onFailure(message: String?) {
                                                        viewModelScope.launch {
                                                            _failure.value = message
                                                        }
                                                    }
                                                })
                                            )
                                        }
                                    }
                                    override fun onFailure(message: String?) {
                                        viewModelScope.launch{
                                            tracker += 1
                                            _failure.value = message
                                        }
                                    }
                                }))
                        }
                    }
                }
                override fun onFailure(message: String?) {
                    viewModelScope.launch{
                        _failure.value = message
                        _loading.value = false
                    }
                }
            }
         ))

    }

    fun openChat(chatId: String, partnerNickname: String) {
        usersRepository.getByNickname(partnerNickname,
            GetUserCallbackHandler(object : CallbackListenerWithResult<User> {
                override fun onSuccess(result: User) {
                    _openChat.value = Pair(chatId, result.identifier!!)
                }
                override fun onFailure(message: String?) {
                    viewModelScope.launch{
                        _failure.value = message
                    }
                }
            }))
    }

    fun listenUserChats(userId: String) {
        eventListener = chatsRepository.listenToUserChats(userId,
            UserChatEventCallbackHandler(object : CallbackListenerWithResult<UserChats>{
                override fun onSuccess(result: UserChats) {
                    getLastChats(userId, "")
                }

                override fun onFailure(message: String?) {
                    viewModelScope.launch{
                        _failure.value = message
                    }
                }
            })
        )

    }

    fun stopListenChat(userId: String) {
        chatsRepository.stopListenToUserChats(userId, eventListener, null)
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