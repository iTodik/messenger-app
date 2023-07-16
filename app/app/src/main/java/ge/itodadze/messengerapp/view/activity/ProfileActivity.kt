package ge.itodadze.messengerapp.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.Manifest
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.databinding.ActivityProfileBinding
import ge.itodadze.messengerapp.viewmodel.ProfileViewModel

class ProfileActivity: AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModel.getProfileViewModelFactory(applicationContext)
    }

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // just for testing, later write how we get current user id
        val id: String = "ac442cfd-04f5-4b61-aa3b-debf8c4411deSalomey"

        registerObservers()

        registerListeners(id)

        renderInitial(id)
    }

    private fun registerObservers() {
        viewModel.failure.observe(this){
            if (it != null) Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.user.observe(this){
            if (it.imgUri == null) {
                binding.avatar.setImageResource(R.drawable.avatar_placeholder)
            } else {
                Glide.with(this)
                    .load(it.imgUri)
                    .into(binding.avatar)
            }
            binding.nickname.setText(it.nickname)
            binding.profession.setText(it.profession)
        }
    }

    private fun registerListeners(id: String) {
        binding.avatar.setOnClickListener {
            choosePhoto()
        }
        binding.update.setOnClickListener {
            viewModel.tryUpdate(
                id,
                binding.avatar.drawable.toBitmap(),
                binding.nickname.text.toString(),
                binding.profession.text.toString())
        }
        binding.signOut.setOnClickListener {
            viewModel.trySignOut(id)
        }
    }

    private fun renderInitial(id: String) {
        viewModel.get(id)
    }

    private fun choosePhoto() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val imgUri = result.data?.data
                    Glide.with(this)
                        .load(imgUri)
                        .into(binding.avatar)
                }
            }.launch(intent)
        }
    }

}