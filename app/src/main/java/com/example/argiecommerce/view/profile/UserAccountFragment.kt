package com.example.argiecommerce.view.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentUserAccountBinding
import com.example.argiecommerce.model.User
import com.example.argiecommerce.model.UserApiResponse
import com.example.argiecommerce.utils.Constants
//import com.example.argiecommerce.utils.Constants.FIELD_REQUIRED
import com.example.argiecommerce.utils.GlideApp
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.viewmodel.UserInfoViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class UserAccountFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentUserAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }
    private val userInfoViewModel: UserInfoViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserInfoViewModel::class.java)
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(requireContext())
    }
    private var user: User? = null

    private var imageUri: Uri? = null
    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var alertDialog: AlertDialog
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAccountBinding.inflate(inflater, container, false)
        binding.toolbarLayout.titleToolbar.text = getString(R.string.personal_info)

        user = userViewModel.user

        showInfoData()
        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                imageUri = it.data?.data
                GlideApp.with(requireContext()).load(imageUri).into(binding.imageUser)
                uploadImage()
            }

        return binding.root
    }

    private fun showInfoData() {
        if (user?.avatar != null) showUserAvatar(user!!.avatar)
        if (user?.fullName != null) {
            binding.edtName.text = Editable.Factory.getInstance().newEditable(user?.fullName)
        }
        if (user?.phone != null) {
            binding.edtPhoneNumber.text = Editable.Factory.getInstance().newEditable(user?.phone)
        }
        if (user?.email != null) {
            binding.edtEmail.text = Editable.Factory.getInstance().newEditable(user?.email)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.tvUpdatePassword.setOnClickListener(this)
        binding.buttonSave.setOnClickListener(this)
        binding.imageEdit.setOnClickListener(this)
        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvUpdatePassword -> goToChangePasswordFragment()
            R.id.buttonSave -> saveInfoUserAccount()
            R.id.imageEdit -> changeAvatar()
        }
    }

    private fun changeAvatar() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        imageActivityResultLauncher.launch(intent)
    }

    private fun saveInfoUserAccount() {
        val newFullName = binding.edtName.text.toString().trim()
        val newPhone = binding.edtPhoneNumber.text.toString().trim()

        if (newFullName.isEmpty() || newPhone.isEmpty()) {
//            Snackbar.make(requireView(), FIELD_REQUIRED, Snackbar.LENGTH_SHORT).show()
            Snackbar.make(requireView(), getString(R.string.field_required), Snackbar.LENGTH_SHORT).show()
        } else {
            val userDto = UserApiResponse()
            userDto.phone = newPhone
            userDto.fullName = newFullName
            val token = loginUtils.getUserToken()

            userInfoViewModel.updateBasicInfo(token, user!!.id, userDto).observe(
                requireActivity(), { state -> processUserInfo(state) }
            )
        }
//        navController.navigate(R.id.action_userAccountFragment_to_profileFragment)
    }

    private fun goToChangePasswordFragment() {
        navController.navigate(R.id.action_userAccountFragment_to_changePasswordFragment)
    }

    private fun showUserAvatar(imageUrl: String) {
        GlideApp.with(requireContext()).load(imageUrl).into(binding.imageUser)
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_image.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    private fun fileToMultipartBodyPart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    private fun uploadImage() {
        if (imageUri != null) {
            val imageFile = uriToFile(requireContext(), imageUri!!)
            val image = fileToMultipartBodyPart(imageFile)
            val token = loginUtils.getUserToken()

            userInfoViewModel.changeUserAvatar(token, user!!.id, image).observe(
                requireActivity(), { state -> processUserAvatar(state) }
            )
        }
    }

    private fun processUserAvatar(state: ScreenState<UserApiResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    val userApiResponse = state.data
                    user!!.avatar = userApiResponse.avatar
                    loginUtils.saveUserInfo(user!!)
                    userViewModel.user = user
                    Snackbar.make(
                        requireView(),
                        getString(R.string.update_avatar),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    displayErrorSnackbar(state.message)
                }
            }
        }
    }

    private fun processUserInfo(state: ScreenState<UserApiResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                alertDialog = progressDialog.createAlertDialog(requireActivity())
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    val userApiResponse = state.data
                    user!!.fullName = userApiResponse.fullName
                    user!!.phone = userApiResponse.phone
                    loginUtils.saveUserInfo(user!!)
                    userViewModel.user = user
                    Snackbar.make(
                        requireView(),
                        getString(R.string.update_info),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    displayErrorSnackbar(state.message)
                }
            }
        }
    }

    private fun displayErrorSnackbar(errorMessage: String) {
        Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setAction(getString(R.string.retry_v2)) { dismiss() } }
            .show()
    }
}