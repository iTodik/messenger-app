package ge.itodadze.messengerapp.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.Manifest
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.databinding.ActivityProfileBinding
import ge.itodadze.messengerapp.viewmodel.ProfileViewModel

class ProfileActivity: AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModel.getProfileViewModelFactory(applicationContext)
    }

    private lateinit var binding: ActivityProfileBinding

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val imgUri = result.data?.data
            Toast.makeText(applicationContext, imgUri.toString(), Toast.LENGTH_SHORT).show()
            binding.avatar.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(applicationContext).load(imgUri)
                .apply(RequestOptions.circleCropTransform()).into(binding.avatar)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // just for testing, later write how we get current user id
        val id: String = "5cfffe03-a42d-4042-b5f1-6d4fd5ac4dbaAlbatross"

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
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            galleryLauncher.launch(intent)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto()
            }
        }
    }

    companion object {
        private const val READ_EXTERNAL_STORAGE_CODE = 209
    }

}