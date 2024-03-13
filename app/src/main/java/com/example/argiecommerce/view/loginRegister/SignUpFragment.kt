package com.example.argiecommerce.view.loginRegister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentSignUpBinding
import com.example.argiecommerce.utils.Validation
import com.google.android.material.snackbar.Snackbar

class SignUpFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.tvLogin.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvLogin -> goToLoginFragment()
            R.id.btnSignUp -> createAccount()
        }
    }

    private fun createAccount() {
        val name = binding.edtUserName.text.toString()
        val phone = binding.edtUserPhone.text.toString()
        val email = binding.edtUserEmail.text.toString()
        val password = binding.edtUserPassword.text.toString()

        if (name.isEmpty()){
            binding.edtUserName.setError("Yêu cầu nhập tên")
            binding.edtUserName.requestFocus()
            return
        }

        if(!Validation.isValidName(name)){
            binding.edtUserName.setError("Tên phải tối thiểu 3 ký tự")
            binding.edtUserName.requestFocus()
            return
        }

        if (phone.isEmpty()){
            binding.edtUserPhone.setError("Yêu cầu nhập số điện thoại")
            binding.edtUserPhone.requestFocus()
            return
        }

        if (!Validation.isValidPhone(phone)){
            binding.edtUserPhone.setError("Số điện thoại không hợp lệ")
            binding.edtUserPhone.requestFocus()
            return
        }

        if (email.isEmpty()){
            binding.edtUserEmail.setError("Yêu cầu nhập email")
            binding.edtUserEmail.requestFocus()
            return
        }

        if (!Validation.isValidEmail(email)){
            binding.edtUserEmail.setError("Email không đúng định dạng")
            binding.edtUserEmail.requestFocus()
            return
        }

        if (password.isEmpty()){
            binding.edtUserPassword.setError("Yêu cầu nhập mật khẩu")
            binding.edtUserPassword.requestFocus()
            return
        }

        if (!Validation.isValidPassword(password)){
            binding.edtUserPassword.setError("Mật khẩu phải tối thiểu 8 ký tự")
            binding.edtUserPassword.requestFocus()
            return
        }

        Snackbar.make(requireView(), "Tạo tài khoản thành công", Snackbar.LENGTH_SHORT).show()
    }

    private fun goToLoginFragment() {
        navController.navigate(R.id.action_signUpFragment_to_loginFragment)
    }


}