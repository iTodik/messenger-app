package ge.itodadze.messengerapp.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ge.itodadze.messengerapp.databinding.ActivitySignUpBinding
import ge.itodadze.messengerapp.utils.PasswordHasher
import ge.itodadze.messengerapp.viewmodel.SignUpViewModel

class SignUpActivity: AppCompatActivity() {
    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModel.getSignUpViewModelFactory()
    }

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerObservers()

        registerListeners()

    }

    private fun registerObservers() {
        viewModel.failure.observe(this){
            if (it != null) Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.signedUpNickname.observe(this){
            // now we are signed up
        }
    }

    private fun registerListeners() {
        binding.signUpButton.setOnClickListener {
            viewModel.trySignUp(binding.nickname.text.toString(),
                PasswordHasher.sha1(binding.password.text.toString()),
                binding.profession.text.toString())
        }
    }
}