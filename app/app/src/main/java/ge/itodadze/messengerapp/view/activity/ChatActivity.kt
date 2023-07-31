package ge.itodadze.messengerapp.view.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.databinding.ActivityChatBinding
import ge.itodadze.messengerapp.view.adapter.ChatAdapter
import ge.itodadze.messengerapp.view.decorator.ChatItemDecoration
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
                finish()
            } else {
                val chatId: String = intent.extras!!.getString(CHAT_ID)!!
                val conversationPartner: String = intent.extras!!.getString(PARTNER)!!

                registerAdapters(it)

                registerObservers()

                registerListeners(it, chatId, conversationPartner)

                renderInitial(chatId, conversationPartner)
            }

        }

    }

    private fun registerAdapters(user: String) {
        binding.messages.adapter = ChatAdapter(this, emptyList(), user)
        binding.messages.layoutManager = LinearLayoutManager(applicationContext)
        binding.messages.addItemDecoration(ChatItemDecoration(
            (binding.messages.adapter as ChatAdapter)::getTopSpacing))
    }

    private fun renderInitial(chatId: String, partner: String) {
        viewModel.get(chatId)
        viewModel.getConversationPartner(partner)
    }

    private fun registerObservers() {
        viewModel.failure.observe(this) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.messages.observe(this){
            (binding.messages.adapter as ChatAdapter).update(it)
        }

        viewModel.partner.observe(this){
            binding.collapsingNickname.text = it.nickname
            binding.toolbarNickname.text = it.nickname
            binding.collapsingProfession.text = it.profession
            binding.toolbarProfession.text = it.profession
            if (it.imgUri == null) {
                Glide.with(applicationContext).load(R.drawable.avatar_placeholder)
                    .apply(RequestOptions.circleCropTransform()).into(binding.collapsingAvatar)
            } else {
                Glide.with(applicationContext).load(it.imgUri)
                    .apply(RequestOptions.circleCropTransform()).into(binding.collapsingAvatar)
            }
        }
    }

    private fun registerListeners(user: String, chatId: String, partner: String) {
        binding.sendText.setOnClickListener {
            viewModel.sendText(chatId, binding.inputText.text.toString(), user, partner)
            binding.inputText.setText(EMPTY_TEXT)
        }

        viewModel.listenChat(chatId)

        binding.collapsingBack.setOnClickListener {
            viewModel.stopListenChat(chatId)
            finish()
        }

        binding.toolbarBack.setOnClickListener {
            viewModel.stopListenChat(chatId)
            finish()
        }

        binding.collapsing.addOnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset <= -binding.collapsing.totalScrollRange) {
                binding.toolbar.visibility = View.VISIBLE
                binding.initialCollapsing.visibility = View.GONE
                Glide.with(applicationContext).load(binding.collapsingAvatar.drawable)
                    .apply(RequestOptions.circleCropTransform()).into(binding.toolbarAvatar)
            } else {
                binding.initialCollapsing.visibility = View.VISIBLE
                binding.toolbar.visibility = View.GONE
            }
        }

    }

    companion object {
        const val CHAT_ID: String = "chat_id"
        const val PARTNER: String = "partner"
        private const val EMPTY_TEXT: String = ""
    }

}


