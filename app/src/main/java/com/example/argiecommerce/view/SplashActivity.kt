package com.example.argiecommerce.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.argiecommerce.databinding.ActivitySplashBinding
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.Constants.USER
import com.example.argiecommerce.utils.LoginUtils

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val thread = object : Thread(){
            override fun run() {
                try {
                    sleep(3500)
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                } finally {
                    val loginUtils = LoginUtils(applicationContext)
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    var user: User? = null

                    if (loginUtils.isLoggedIn()){
                        user = loginUtils.getUserInfo()
                        intent.putExtra(USER, user)
                    } else {
                        intent.putExtra(USER, user)
                    }

                    startActivity(intent)
                    finish()
                }
            }
        }
        thread.start()
    }
}