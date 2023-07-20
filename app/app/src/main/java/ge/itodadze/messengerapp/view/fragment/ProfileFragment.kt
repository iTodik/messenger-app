package ge.itodadze.messengerapp.view.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.Manifest
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ge.itodadze.messengerapp.R
import ge.itodadze.messengerapp.databinding.FragmentProfileBinding
import ge.itodadze.messengerapp.view.activity.SignInActivity
import ge.itodadze.messengerapp.viewmodel.ProfileViewModel

class ProfileFragment(private val parent: AppCompatActivity,
                      private val viewModel: ProfileViewModel): Fragment() {

    private var binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)

        viewModel.logId.observe(parent) {

            if (it == null) {
                val intent = Intent(parent.applicationContext, SignInActivity::class.java)
                startActivity(intent)
            } else {
                registerObservers()

                registerListeners(it)

                renderInitial(it)
            }

        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val imgUri = result.data?.data
            Toast.makeText(context, imgUri.toString(), Toast.LENGTH_SHORT).show()
            binding?.avatar?.scaleType = ImageView.ScaleType.CENTER_CROP
            binding?.avatar?.let {
                Glide.with(parent).load(imgUri)
                    .apply(RequestOptions.circleCropTransform()).into(it)
            }
        }
    }

    private fun registerObservers() {
        viewModel.failure.observe(parent){
            if (it != null) Toast.makeText(parent.applicationContext, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.user.observe(parent){
            if (it.imgUri == null) {
                binding?.avatar?.setImageResource(R.drawable.avatar_placeholder)
            } else {
                binding?.avatar?.let { it1 ->
                    Glide.with(parent)
                        .load(it.imgUri)
                        .into(it1)
                }
            }
            binding?.nickname?.setText(it.nickname)
            binding?.profession?.setText(it.profession)
        }
        viewModel.logId.observe(parent){
            if (it == null) {
                val intent = Intent(parent.applicationContext, SignInActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun registerListeners(id: String) {
        binding?.avatar?.setOnClickListener {
            choosePhoto()
        }
        binding?.update?.setOnClickListener {
            viewModel.tryUpdate(
                id,
                binding?.avatar?.drawable?.toBitmap(),
                binding?.nickname?.text.toString(),
                binding?.profession?.text.toString())
        }
        binding?.signOut?.setOnClickListener {
            viewModel.trySignOut(id)
        }
    }

    private fun renderInitial(id: String) {
        viewModel.get(id)
    }

    fun choosePhoto() {
        if (ContextCompat.checkSelfPermission(
                parent.applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            galleryLauncher.launch(intent)
        } else {
            ActivityCompat.requestPermissions(
                parent, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_CODE
            )
        }
    }

    companion object {
        const val READ_EXTERNAL_STORAGE_CODE = 209
    }

}