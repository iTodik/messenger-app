package ge.itodadze.messengerapp.view.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import ge.itodadze.messengerapp.databinding.ActivitySearchBinding
import ge.itodadze.messengerapp.viewmodel.SearchViewModel

class SearchActivity: AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.getSearchViewModelFactory(this)
    }

    private lateinit var binding: ActivitySearchBinding

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerObservers()

        registerListeners()
    }

    private fun registerObservers() {
        viewModel.failure.observe(this){
            if (it != null) Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.users.observe(this){
            // update adapter
        }
    }

    private fun registerListeners() {
        binding.searchText.addTextChangedListener{object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                timer?.cancel()
            }
            override fun afterTextChanged(p0: Editable?) {
                timer = object : CountDownTimer(STOPPED_TYPING_TIME, STOPPED_TYPING_TIME) {
                    override fun onTick(millisUntilFinished: Long) {
                    }
                    override fun onFinish() {
                        val userInput = p0.toString()
                        if (userInput.isEmpty() || userInput.length >= SEARCH_MIN_CHAR) {
                            viewModel.searchUsers(userInput)
                        }
                    }
                }.start()
            }
        }}
    }

    companion object {
        const val STOPPED_TYPING_TIME: Long = 1500
        const val SEARCH_MIN_CHAR: Int = 3
    }

}