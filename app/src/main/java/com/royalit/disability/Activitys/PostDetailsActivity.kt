package com.royalit.disability.Activitys

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.databinding.ActivityAboutUsBinding
import com.royalit.disability.databinding.ActivityPostViewBinding

class PostDetailsActivity : AppCompatActivity() {

    val binding: ActivityPostViewBinding by lazy {
        ActivityPostViewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Post Details"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

    }



}