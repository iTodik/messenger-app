package ge.itodadze.messengerapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ge.itodadze.messengerapp.databinding.FragmentFrontPageBinding
import ge.itodadze.messengerapp.view.activity.SignInActivity
import ge.itodadze.messengerapp.view.adapter.FrontPageChatsAdapter
import ge.itodadze.messengerapp.view.model.ViewChat
import ge.itodadze.messengerapp.view.model.ViewUser
import ge.itodadze.messengerapp.viewmodel.FrontPageViewModel
import ge.itodadze.messengerapp.viewmodel.models.Message
import java.sql.Date


class FrontPageFragment(private val parent: AppCompatActivity,
                        private val viewModel: FrontPageViewModel
) : Fragment(){

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

                registerObservers()

                registerListeners(it)

                renderInitial(it)
            }
        }

        return binding?.root
    }

    private fun registerAdapter() {




        adapter = FrontPageChatsAdapter(parent.applicationContext, emptyList())
        Log.d("binding", binding.toString())
        binding?.frontPageChats?.adapter = adapter
    }

    private fun renderInitial(id: String) {
        // viewModel.getLastChats(id)
    }

    private fun registerListeners(id: String) {

    }

    private fun registerObservers() {

    }

}