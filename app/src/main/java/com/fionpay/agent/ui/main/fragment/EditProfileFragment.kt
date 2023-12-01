package com.fionpay.agent.ui.main.fragment

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.fionpay.agent.R
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.FragmentEditProfileBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.di.DaggerEditProfileFragmentComponent
import com.fionpay.agent.ui.main.di.EditProfileFragmentModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.PhotoUtils
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.fionpay.agent.utils.toRequestBody
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(R.layout.fragment_edit_profile) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private var currentAttachmentPath: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initialization() {
        // Define a file where the captured image will be saved
        getAgentProfile()
        // Capture the image when needed
        mDataBinding.imageCard.setOnClickListener {
            openBottomDialog()
        }

        mDataBinding.topHeader.txtHeader.text = getString(R.string.edit_profile)
        mDataBinding.topHeader.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }

        mDataBinding.btnUpdate.setOnClickListener {
            val name = mDataBinding.etFirstName.text.toString()
            if (currentAttachmentPath.isNotEmpty()) {
                val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "profile_image", "file.jpeg", RequestBody.create(
                        "image/jpeg".toMediaTypeOrNull(), File(currentAttachmentPath)
                    )
                )

                updateAgentProfile(toRequestBody(name), filePart)
            } else {
                updateAgentWithoutProfile(toRequestBody(name))
            }


        }
    }

    private fun getAgentProfile() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.getAgentProfile(viewModel.getUserId().toString())
            viewModel.getAgentProfileResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        mDataBinding.etFirstName.setText(it.data?.fullName)

                        Glide.with(this)
                            .load(it.data?.profileImageUrl)
                            .error(R.drawable.demo_user_image)
                            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)) // Optional caching strategy
                            .into(mDataBinding.userImage)
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            Snackbar.make(requireView(), getString(R.string.no_network), Snackbar.LENGTH_LONG).show()
        }

    }

    private fun updateAgentProfile(fullName: RequestBody, filePart: MultipartBody.Part) {
        if (networkHelper.isNetworkConnected()) {
            viewModel.updateAgentProfile(fullName, filePart, viewModel.getUserId().toString())
            viewModel.updateAgentProfileResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        mDataBinding.etFirstName.setText(it.data?.fullName)
                        viewModel.setFullName(it.data?.fullName)
                        viewModel.setProfileImage(it.data?.profileImageUrl)
                        Navigation.findNavController(requireView())
                            .navigate(R.id.navigation_settingFragment)
                        Glide.with(this)
                            .load(it.data?.profileImageUrl)
                            .error(R.drawable.demo_user_image)
                            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)) // Optional caching strategy
                            .into(mDataBinding.userImage)
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            Snackbar.make(requireView(), getString(R.string.no_network), Snackbar.LENGTH_LONG).show()
        }

    }

    private fun updateAgentWithoutProfile(fullName: RequestBody) {
        if (networkHelper.isNetworkConnected()) {
            viewModel.updateAgentWithoutProfile(fullName, viewModel.getUserId().toString())
            viewModel.updateAgentWithoutProfileResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        mDataBinding.etFirstName.setText(it.data?.fullName)
                        viewModel.setFullName(it.data?.fullName)
                        Navigation.findNavController(requireView())
                            .navigate(R.id.navigation_settingFragment)
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            Snackbar.make(requireView(), getString(R.string.no_network), Snackbar.LENGTH_LONG).show()
        }

    }

    private fun initializeDagger() {
        DaggerEditProfileFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .editProfileFragmentModule(EditProfileFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun openBottomDialog() {
        val dialog = BottomSheetDialog(mActivity)

        val view = layoutInflater.inflate(R.layout.photo_layout, null)

        val takePhoto = view.findViewById<Button>(R.id.take_photo)
        val choosePhoto = view.findViewById<Button>(R.id.choose_photo)
        val closeBtn = view.findViewById<Button>(R.id.closeBtn)

        takePhoto.setOnClickListener {
            checkPermissionAndOpenCamera()
            dialog.dismiss()
        }

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        choosePhoto.setOnClickListener {
            openGallery()
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {

            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            openCamera()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                showMessage("Camera Permission Needed to Scan the Code")
            }
        }

    private fun openGallery() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.type = "image/*"
        pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
        pickIntent.action = Intent.ACTION_OPEN_DOCUMENT
        launchGalleryAttachmentActivity.launch(pickIntent)
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.resolveActivity(requireActivity().packageManager)
        val INVENTORY_IMAGE_NAME = "InventoryImg_${System.currentTimeMillis()}.jpeg"
        val photoFile: File = PhotoUtils.createCapturedPhoto(
            requireActivity(), takePictureIntent, INVENTORY_IMAGE_NAME
        )
        currentAttachmentPath = photoFile.absolutePath

        launchCameraAttachmentActivity.launch(takePictureIntent)

    }

    private var launchCameraAttachmentActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (currentAttachmentPath.isNotEmpty()) {

                    setCameraAttachment(currentAttachmentPath)
                } else {
                    Toast.makeText(activity, "Path not found", Toast.LENGTH_LONG).show()
                }
            }
        }

    private var launchGalleryAttachmentActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val INVENTORY_DOC_NAME = "InventoryImg_${System.currentTimeMillis()}.png"
                    val uri = result!!.data?.data
                    uri?.let {
                        val image = PhotoUtils.savedFileImage(
                            requireContext(),
                            uri,
                            INVENTORY_DOC_NAME
                        )
                        currentAttachmentPath = image
                        Toast.makeText(requireContext(), "${image}", Toast.LENGTH_LONG).show()
                        setCameraAttachment(
                            image
                        )
                    }
                }
            }
        }

    private fun setCameraAttachment(imagePath: String) {

        Glide.with(requireActivity())
            .load(imagePath)
            .error(R.drawable.demo_user_image)
            .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(mDataBinding.userImage)
    }

}