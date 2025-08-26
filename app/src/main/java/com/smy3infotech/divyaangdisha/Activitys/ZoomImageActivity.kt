package com.smy3infotech.divyaangdisha.Activitys

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.databinding.ActivityImageZoomBinding

class ZoomImageActivity : AppCompatActivity() {

    val binding: ActivityImageZoomBinding by lazy {
        ActivityImageZoomBinding.inflate(layoutInflater)
    }

    var ImageUrl: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        ImageUrl = intent.getStringExtra("ImageUrl").toString()

        Glide.with(this)
            .load(ImageUrl)
            .into(binding.photoView)


        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

    }

}