package com.example.argiecommerce.view.loginRegister

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.FragmentAuthenticationBinding
import com.example.argiecommerce.viewmodel.UserViewModel

class AuthenticationFragment : Fragment(), View.OnClickListener {

    companion object {
        private const val TAG = "AuthenticationFragment"
    }

    private lateinit var binding: FragmentAuthenticationBinding

    private lateinit var navController: NavController

    private lateinit var correctOtpCode: String
    private var clickCount = 0

    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false)

        binding.toolbarLayout.titleToolbar.text = getString(R.string.otp_authentication)
       correctOtpCode = "123456"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.btnContinue.setOnClickListener(this)
        binding.reSend.setOnClickListener(this)
        binding.toolbarLayout.imgBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnContinue -> checkOtpCode()
            R.id.reSend -> resendOtpCode()
        }
    }

    private fun resendOtpCode() {
        clickCount = clickCount + 1
        getAnotherOtpCode()
        if(clickCount >= 3){
            binding.reSend.isClickable = false
            binding.numberOfClicks.visibility = View.VISIBLE
        }
    }

    private fun getAnotherOtpCode() {
        binding.reSend.isEnabled = false
        binding.countDownTimer.visibility = View.VISIBLE
        countDownTimer(binding.countDownTimer)
    }

    private fun checkOtpCode() {
        val otpEntered = binding.otpCode.text.toString()

        if(!otpEntered.equals(correctOtpCode)){
            binding.otpCode.setError("Mã không chính xác")
            binding.otpCode.requestFocus()
        } else {
            navController.navigate(R.id.action_authenticationFragment_to_changePasswordFragment)
        }

    }

    fun countDownTimer(textView: TextView){
        object : CountDownTimer(60000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                textView.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                Log.d(TAG, "onFinish: Done")
                binding.reSend.isEnabled = true
                binding.countDownTimer.visibility = View.INVISIBLE
            }
        }.start()
    }

}