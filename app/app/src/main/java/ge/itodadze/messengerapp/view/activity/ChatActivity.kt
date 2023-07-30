package ge.itodadze.messengerapp.view.activity


import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.CreationExtras
import ge.itodadze.messengerapp.databinding.ActivityChatBinding
import ge.itodadze.messengerapp.databinding.ActivitySignUpBinding
import ge.itodadze.messengerapp.viewmodel.ChatViewModel



class ChatActivity : AppCompatActivity() {

    private val viewModel: ChatViewModel by viewModels {
        ChatViewModel.getChatViewModelFactory(applicationContext)
    }

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.logId.observe(this) {

            if (it == null) {
                val intent = Intent(parent.applicationContext, SignInActivity::class.java)
                startActivity(intent)
            } else {
                registerObservers()

                registerListeners(it)

                renderInitial(it)
            }

        }

    }

    private fun renderInitial(id: String) {
        //viewModel.get(id)  need chat_id here that i'll get from intent
    }

    private fun registerObservers() {


        viewModel.messages.observe(this){

        }


    }

    private fun registerListeners(id:String) {

    }



}


