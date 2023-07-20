package ge.itodadze.messengerapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.launch


/////////////////// NEEDS FINISHING AFTER ADDING CHAT REPOSITORY

class FrontPageViewModel(private val logInManager: LogInManager): ViewModel() {

    private val _logId = MutableLiveData<String?>()
    val logId: LiveData<String?>
        get() = _logId

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
            LogInManager(context.getSharedPreferences(LogInManager.FILE_NAME, Context.MODE_PRIVATE))
        ) as T
    }
}