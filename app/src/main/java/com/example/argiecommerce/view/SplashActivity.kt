package com.example.argiecommerce.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.argiecommerce.databinding.ActivitySplashBinding
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants.USER
import com.example.argiecommerce.utils.LoginUtils
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
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
                        signIn(user.email, user.password)
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

    private fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    Log.d("TEST", "signInWithEmail:success")
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}