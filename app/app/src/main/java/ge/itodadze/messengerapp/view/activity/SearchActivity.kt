package ge.itodadze.messengerapp.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ge.itodadze.messengerapp.databinding.ActivityChatBinding
import ge.itodadze.messengerapp.viewmodel.SearchViewModel

class SearchActivity: AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.getSearchViewModelFactory(applicationContext)
    }

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}