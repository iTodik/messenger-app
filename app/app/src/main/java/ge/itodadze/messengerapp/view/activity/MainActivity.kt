package ge.itodadze.messengerapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.databinding.ActivityMainBinding
import ge.itodadze.messengerapp.databinding.ActivitySignInBinding
import ge.itodadze.messengerapp.utils.PasswordHasher

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerObservers()
        registerListeners()

    }

    private fun registerObservers() {

    }

    private fun registerListeners() {

    }
}