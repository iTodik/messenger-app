package ge.itodadze.messengerapp.view.activity

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.databinding.ActivityMainBinding
import ge.itodadze.messengerapp.view.adapter.ViewPagerAdapter
import ge.itodadze.messengerapp.view.fragment.FrontPageFragment
import ge.itodadze.messengerapp.view.fragment.ProfileFragment
import ge.itodadze.messengerapp.viewmodel.FrontPageViewModel
import ge.itodadze.messengerapp.viewmodel.ProfileViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewPager: ViewPager2

    private lateinit var fragment1: FrontPageFragment

    private lateinit var fragment2: ProfileFragment

    private val frontPageViewModel: FrontPageViewModel by viewModels {
        FrontPageViewModel.getFrontPageViewModelFactory(applicationContext)
    }

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModel.getProfileViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragment1 = FrontPageFragment(this, frontPageViewModel)
        fragment2 = ProfileFragment(this, profileViewModel)
        viewPager = binding.viewPager
        val adapter = ViewPagerAdapter(this, arrayListOf(fragment1, fragment2))
        viewPager.adapter = adapter

        registerObservers()

        registerListeners()
    }

    private fun registerObservers() {

    }

    private fun registerListeners() {

        binding.bottomNavigationView.setOnItemSelectedListener{
           if(it.itemId==R.id.front_page_icon) {
               viewPager.currentItem = 0
           }
           if(it.itemId==R.id.profile_icon){
               viewPager.currentItem = 1
           }
           true
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ProfileFragment.READ_EXTERNAL_STORAGE_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fragment2.choosePhoto()
            }
        }
    }

}