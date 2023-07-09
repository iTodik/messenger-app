package ge.itodadze.messengerapp.view.activity

import androidx.activity.viewModels
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ge.itodadze.messengerapp.databinding.SignInBinding
import ge.itodadze.messengerapp.viewmodel.SignInViewModel

class SignInActivity: AppCompatActivity() {

    private val viewModel: SignInViewModel by viewModels {
        SignInViewModel.getSignInViewModelFactory()
    }

    private lateinit var binding: SignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}