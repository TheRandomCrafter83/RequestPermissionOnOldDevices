package com.coderzf1.myapplication

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.coderzf1.myapplication.databinding.FragmentLoadImageBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoadImageFragment : Fragment() {

    private var _binding: FragmentLoadImageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var chooseImage:ActivityResultLauncher<PickVisualMediaRequest>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { hasPermission ->
            if (hasPermission) chooseImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        chooseImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri ->
            binding.imageview.setImageURI(uri)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoadImageBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            checkAndRequestPermission()
        }
    }

    private fun checkAndRequestPermission(){
        if (Build.VERSION.SDK_INT >= 30){
            chooseImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            return
        }
        if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            chooseImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            return
        }
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}