package ge.itodadze.messengerapp.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ge.itodadze.messengerapp.databinding.ActivityProfileBinding
import ge.itodadze.messengerapp.viewmodel.ProfileViewModel

class ProfileActivity: AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModel.getProfileViewModelFactory(applicationContext)
    }

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerObservers()

        registerListeners()
    }

    private fun registerObservers() {

    }

    private fun registerListeners() {

    }

}