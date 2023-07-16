package ge.itodadze.messengerapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.domain.repository.UsersFirebaseRepository
import ge.itodadze.messengerapp.domain.repository.UsersRepository

class ProfileViewModel(private val usersRepository: UsersRepository): ViewModel() {

    companion object {
        fun getProfileViewModelFactory(context: Context): ProfileViewModelFactory {
            return ProfileViewModelFactory(context)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(UsersFirebaseRepository(context.resources.getString(R.string.db_location))) as T
    }
}