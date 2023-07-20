package ge.itodadze.messengerapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationBarView
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.databinding.ActivityMainBinding
import ge.itodadze.messengerapp.databinding.ActivitySignInBinding
import ge.itodadze.messengerapp.utils.PasswordHasher
import ge.itodadze.messengerapp.view.adapter.ViewPagerAdapter
import ge.itodadze.messengerapp.view.fragment.FrontPageFragment
import ge.itodadze.messengerapp.viewmodel.FrontPageViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // will replace with profile fragment after merging

        registerObservers()
        registerListeners()
        val fragment1 = FrontPageFragment(this, FrontPageViewModel())
        val fragment2 = FrontPageFragment(this, FrontPageViewModel())
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
}