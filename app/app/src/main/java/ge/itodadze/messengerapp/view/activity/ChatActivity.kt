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
                val intent = Intent(applicationContext, SignInActivity::class.java)
                startActivity(intent)
            } else if (intent.extras?.getString(CHAT_ID) == null
                || intent.extras?.getString(PARTNER) == null) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            } else {
                val chatId: String = intent.extras!!.getString(CHAT_ID)!!
                val conversationPartner: String = intent.extras!!.getString(PARTNER)!!

                registerObservers(it)

                registerListeners(it, chatId, conversationPartner)

                renderInitial(it, chatId)
            }

        }

    }

    private fun renderInitial(user: String, chatId: String) {
        viewModel.get(chatId)
    }

    private fun registerObservers(user: String) {
        viewModel.messages.observe(this){

        }
    }

    private fun registerListeners(user: String, chatId: String, partner: String) {
        binding.sendText.setOnClickListener {
            viewModel.sendText(chatId, binding.inputText.text.toString(), user, partner)
            binding.inputText.setText(EMPTY_TEXT)
        }
    }

    companion object {
        const val CHAT_ID: String = "chat_id"
        const val PARTNER: String = "partner"
        private const val EMPTY_TEXT: String = ""
    }

}


