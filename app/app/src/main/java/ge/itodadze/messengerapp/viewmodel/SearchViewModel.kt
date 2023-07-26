package ge.itodadze.messengerapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchViewModel: ViewModel() {

    companion object {
        fun getSearchViewModelFactory(context: Context): SearchViewModelFactory {
            return SearchViewModelFactory(context)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel() as T
    }
}