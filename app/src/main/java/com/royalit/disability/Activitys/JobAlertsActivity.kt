package com.royalit.disability.Activitys

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.databinding.ActivityAboutUsBinding
import com.royalit.disability.databinding.ActivityJobAlertsBinding

class JobAlertsActivity : AppCompatActivity() {

    val binding: ActivityJobAlertsBinding by lazy {
        ActivityJobAlertsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }



    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Job Alerts"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

    }
}