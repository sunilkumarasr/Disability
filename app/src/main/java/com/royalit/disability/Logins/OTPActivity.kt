package com.royalit.disability.Logins

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.disability.Activitys.DashBoardActivity
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.databinding.ActivityOtpactivityBinding

class OTPActivity : AppCompatActivity() {

    val binding: ActivityOtpactivityBinding by lazy {
        ActivityOtpactivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }


    private fun inits() {
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

    }


}