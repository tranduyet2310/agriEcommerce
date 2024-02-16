package com.example.argiecommerce.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        val text = "<font color='#ff8800'>Argi</font><font color='#3BD98C'>Mart</font>"
//        binding.tvAppName.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)

        val thread = object : Thread(){
            override fun run() {
                try {
                    sleep(3500)
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                } finally {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        thread.start()
    }
}