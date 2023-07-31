package ge.itodadze.messengerapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ge.itodadze.messengerapp.databinding.FragmentFrontPageBinding
import ge.itodadze.messengerapp.view.activity.ChatActivity
import ge.itodadze.messengerapp.view.activity.SignInActivity
import ge.itodadze.messengerapp.view.adapter.FrontPageChatsAdapter
import ge.itodadze.messengerapp.view.adapter.SearchChatListener
import ge.itodadze.messengerapp.view.model.ViewChat
import ge.itodadze.messengerapp.viewmodel.FrontPageViewModel


class FrontPageFragment(private val parent: AppCompatActivity,
                        private val viewModel: FrontPageViewModel
) : Fragment(), SearchChatListener{

    private var binding: FragmentFrontPageBinding? = null
    lateinit var adapter: FrontPageChatsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFrontPageBinding.inflate(inflater)
        Log.d("binding", binding.toString())


        viewModel.logId.observe(parent){
            if (it == null) {
                val intent = Intent(parent.applicationContext, SignInActivity::class.java)
                startActivity(intent)
            } else {

                registerAdapter()

                registerObservers(it)

                registerListeners(it)

                renderInitial(it)
            }
        }

        return binding?.root
    }

    private fun registerAdapter() {
        adapter = FrontPageChatsAdapter(parent.applicationContext, emptyList(), this)
        binding?.frontPageChats?.adapter = adapter
        binding?.frontPageChats?.layoutManager = LinearLayoutManager(parent.applicationContext)
    }

    private fun renderInitial(id: String) {
        viewModel.getLastChats(id, "")
    }

    private fun registerListeners(id: String) {
        binding?.searchText?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                viewModel.getLastChats(id, p0.toString())
            }
        })


    }

    private fun registerObservers(id: String) {

        viewModel.lastChats.observe(parent) {
            (binding?.frontPageChats?.adapter as FrontPageChatsAdapter).update(it)
        }

        viewModel.loading.observe(parent) {
            if (it == false) {
                binding?.loader?.visibility = View.GONE
                binding?.frontPageChats?.visibility = View.VISIBLE
            } else {
                binding?.loader?.visibility = View.VISIBLE
                binding?.frontPageChats?.visibility = View.GONE
            }
        }

        viewModel.openChat.observe(parent) {

            val intent = Intent(parent.applicationContext, ChatActivity::class.java)
            intent.putExtra(ChatActivity.CHAT_ID, it.first)
            intent.putExtra(ChatActivity.PARTNER, it.second)
            startActivity(intent)
        }

    }

    override fun userClicked(user: ViewChat) {
        viewModel.openChat(user.chat_id!!, user.viewUser!!.nickname!!)
    }

}