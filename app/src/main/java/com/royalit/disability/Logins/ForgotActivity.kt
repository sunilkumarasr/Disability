package com.royalit.disability.Logins

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.disability.R
import com.royalit.disability.databinding.ActivityForgotBinding
import com.royalit.disability.databinding.ActivityLoginBinding

class ForgotActivity : AppCompatActivity() {

    val binding: ActivityForgotBinding by lazy {
        ActivityForgotBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inits()
    }


    private fun inits() {
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        binding.cardForgot.setOnClickListener {
            startActivity(Intent(this@ForgotActivity, OTPActivity::class.java))
        }

    }

}