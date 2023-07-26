package ge.itodadze.messengerapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.UsersRepository
import ge.itodadze.messengerapp.view.model.ViewUser
import ge.itodadze.messengerapp.viewmodel.callback.SearchUsersCallbackHandler
import ge.itodadze.messengerapp.viewmodel.listener.CallbackListenerWithResult
import ge.itodadze.messengerapp.viewmodel.models.User
import kotlinx.coroutines.launch

class SearchViewModel(private val usersRepository: UsersRepository): ViewModel() {

    private val _users = MutableLiveData<List<ViewUser>>()
    val users: LiveData<List<ViewUser>>
        get() = _users

    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String>
        get() = _failure

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
            UsersFirebaseRepository(context.resources.getString(R.string.db_location))
        ) as T
    }
}