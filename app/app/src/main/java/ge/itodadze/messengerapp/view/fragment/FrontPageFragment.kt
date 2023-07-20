package ge.itodadze.messengerapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ge.itodadze.messengerapp.databinding.FragmentFrontPageBinding
import ge.itodadze.messengerapp.view.activity.SignInActivity
import ge.itodadze.messengerapp.viewmodel.FrontPageViewModel




/////////////////// NEEDS FINISHING AFTER ADDING CHAT REPOSITORY

class FrontPageFragment(private val parent: AppCompatActivity,
                        private val viewModel: FrontPageViewModel
) : Fragment(){

    private var binding: FragmentFrontPageBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFrontPageBinding.inflate(inflater)

        viewModel.logId.observe(parent){
            if (it == null) {
                val intent = Intent(parent.applicationContext, SignInActivity::class.java)
                startActivity(intent)
            } else {
                // front page stuff
            }
        }

        return binding?.root
    }

}