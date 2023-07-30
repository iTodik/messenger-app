package ge.itodadze.messengerapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ge.itodadze.messengerapp.databinding.ActivitySearchBinding
import ge.itodadze.messengerapp.view.adapter.SearchListener
import ge.itodadze.messengerapp.view.adapter.SearchUsersAdapter
import ge.itodadze.messengerapp.view.model.ViewUser
import ge.itodadze.messengerapp.viewmodel.SearchViewModel

class SearchActivity: AppCompatActivity(), SearchListener {

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.getSearchViewModelFactory(this)
    }

    private lateinit var binding: ActivitySearchBinding

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerAdapters()

        registerObservers()

        registerListeners()
    }

    private fun registerAdapters() {
        binding.users.adapter = SearchUsersAdapter(this, emptyList(), this)
        binding.users.layoutManager = LinearLayoutManager(this)
        startSearch("")
    }

    private fun registerObservers() {
        viewModel.failure.observe(this){
            if (it != null) Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            endSearch()
        }
        viewModel.users.observe(this){
            (binding.users.adapter as SearchUsersAdapter).update(it)
            endSearch()
        }
        viewModel.chatOpened.observe(this){
            val intent = Intent(applicationContext, ChatActivity::class.java)
            intent.putExtra(ChatActivity.CHAT_ID, it.first)
            intent.putExtra(ChatActivity.PARTNER, it.second)
            startActivity(intent)
        }
    }

    private fun registerListeners() {

        binding.searchText.addTextChangedListener(object: TextWatcher {
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
                            startSearch(userInput)
                        }
                    }
                }.start()
            }
        })
        binding.back.setOnClickListener{
            finish()
        }
    }

    private fun startSearch(input: String) {
        binding.loader.visibility = View.VISIBLE
        binding.users.visibility = View.GONE
        viewModel.searchUsers(input)
    }

    private fun endSearch() {
        binding.loader.visibility = View.GONE
        binding.users.visibility = View.VISIBLE
    }

    companion object {
        const val STOPPED_TYPING_TIME: Long = 1500L
        const val SEARCH_MIN_CHAR: Int = 3
    }

    override fun userClicked(user: ViewUser) {
        viewModel.openChatWith(user)
    }

}