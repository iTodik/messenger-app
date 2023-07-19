package ge.itodadze.messengerapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.viewmodel.FrontPageViewModel




/////////////////// NEEDS FINISHING AFTER ADDING CHAT REPOSITORY

class FrontPageFragment(private val parent: AppCompatActivity,
                        private val viewModel: FrontPageViewModel
) : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_front_page, container, false)
    }

    companion object {
        private const val READ_EXTERNAL_STORAGE_CODE = 209
    }


}