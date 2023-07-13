package ge.itodadze.messengerapp.view.activity

import android.content.Intent
import androidx.activity.viewModels
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ge.itodadze.messengerapp.databinding.ActivitySignInBinding
import ge.itodadze.messengerapp.utils.PasswordHasher
import ge.itodadze.messengerapp.viewmodel.SignInViewModel

class SignInActivity: AppCompatActivity() {

    private val viewModel: SignInViewModel by viewModels {
        SignInViewModel.getSignInViewModelFactory(applicationContext)
    }

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerObservers()

        registerListeners()

    }

    private fun registerObservers() {
        viewModel.failure.observe(this){
            if (it != null) Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.signedInNickname.observe(this){
            // now we are signed in
        }
    }

    private fun registerListeners() {
        binding.signInButton.setOnClickListener {
            viewModel.trySignIn(binding.nickname.text.toString(),
                PasswordHasher.sha1(binding.password.text.toString()))
        }

        binding.signUpButton.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}