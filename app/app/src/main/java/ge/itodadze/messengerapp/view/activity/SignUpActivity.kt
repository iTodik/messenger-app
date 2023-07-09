package ge.itodadze.messengerapp.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ge.itodadze.messengerapp.databinding.SignUpBinding
import ge.itodadze.messengerapp.viewmodel.SignUpViewModel

class SignUpActivity: AppCompatActivity() {
    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModel.getSignUpViewModelFactory()
    }

    private lateinit var binding: SignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}