package ge.itodadze.messengerapp.viewmodel

import android.content.Context

import androidx.lifecycle.*
import com.google.firebase.database.ValueEventListener
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.domain.repository.ChatsFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.ChatsRepository
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.viewmodel.callback.AddMessageCallbackHandler
import ge.itodadze.messengerapp.viewmodel.callback.ChatEventCallbackHandler

import ge.itodadze.messengerapp.viewmodel.callback.GetChatCallbackHandler
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListener
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.Chat
import ge.itodadze.messengerapp.viewmodel.models.Message
import kotlinx.coroutines.launch
import java.util.*

class ChatViewModel(private val logInManager: LogInManager,
                    private val usersRepository: UsersRepository,
                    private val chatsRepository: ChatsRepository
): ViewModel(){

    private val _logId = MutableLiveData<String?>()
    val logId: LiveData<String?>
        get() = _logId

    private val _messages = MutableLiveData<MutableList<Message>>()
    val messages: LiveData<MutableList<Message>>
        get() = _messages

    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String>
        get() = _failure

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


    fun get(chat_id: String) {
        chatsRepository.getChat(chat_id, GetChatCallbackHandler(
            object:CallbackListenerWithResult<Chat>{
                override fun onSuccess(result: Chat) {
                    viewModelScope.launch {
                        _messages.value = result.messages
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

    fun listenChat(chat_id: String) {
        eventListener = chatsRepository.listenToChat(chat_id,
            ChatEventCallbackHandler(object : CallbackListenerWithResult<Chat>{
            override fun onSuccess(result: Chat) {
                _messages.value = result.messages
            }

            override fun onFailure(message: String?) {
                viewModelScope.launch{
                    _failure.value = message
                }
            }
        }))
    }

    fun stopListenChat(chat_id: String) {
        chatsRepository.stopListenToChat(chat_id, eventListener, null)
    }

    fun sendText(chat_id: String, text: String?, sender: String?, receiver: String?) {
        chatsRepository.addMessage(chat_id,
            Message(chat_id, text, sender, receiver, Date(System.currentTimeMillis())),
            AddMessageCallbackHandler(object: CallbackListener{
                override fun onSuccess() {
                }
                override fun onFailure(message: String?) {
                    viewModelScope.launch{
                        _failure.value = message
                    }
                }
            }))
    }

    companion object {
        fun getChatViewModelFactory(context: Context): ChatViewModelFactory {
            return ChatViewModelFactory(context)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class ChatViewModelFactory(private val context: Context): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(
            LogInManager(context.getSharedPreferences(LogInManager.FILE_NAME, Context.MODE_PRIVATE)),
            UsersFirebaseRepository(context.resources.getString(R.string.db_location)),
            ChatsFirebaseRepository(context.resources.getString(R.string.db_location))
        ) as T
    }
}
