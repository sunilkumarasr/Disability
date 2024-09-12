package com.royalit.disability.Activitys.JobAlerts

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.databinding.ActivityJobAlertDetailsBinding

class JobAlertDetailsActivity : AppCompatActivity() {

    val binding: ActivityJobAlertDetailsBinding by lazy {
        ActivityJobAlertDetailsBinding.inflate(layoutInflater)
    }

    lateinit var title:String
    lateinit var post_date:String
    lateinit var last_date:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        title= intent.getStringExtra("title").toString()
        post_date= intent.getStringExtra("post_date").toString()
        last_date= intent.getStringExtra("last_date").toString()

        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Job Alerts Details"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        JobAlertDetailsApi()

    }

    private fun JobAlertDetailsApi() {

        binding.txtSub.text = title
        binding.txtPostDate.text = post_date
        binding.txtLastDate.text = last_date
    }

}