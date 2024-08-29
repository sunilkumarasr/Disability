package com.royalit.disability.Activitys

import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.disability.AdaptersAndModels.AboutusResponse
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.Retrofit.RetrofitClient
import com.royalit.disability.databinding.ActivityAboutUsBinding
import com.royalit.disability.databinding.ActivityJobAlertDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobAlertDetailsActivity : AppCompatActivity() {

    val binding: ActivityJobAlertDetailsBinding by lazy {
        ActivityJobAlertDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "About us"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        JobAlertDetailsApi()

    }

    private fun JobAlertDetailsApi() {
//        ViewController.showLoading(this@JobAlertDetailsActivity)
//        val apiInterface = RetrofitClient.apiInterface
//        apiInterface.aboutusApi().enqueue(object : Callback<AboutusResponse> {
//            override fun onResponse(call: Call<AboutusResponse>, response: Response<AboutusResponse>) {
//                ViewController.hideLoading()
//                if (response.isSuccessful) {
//                    val rsp = response.body()
//                    if (rsp != null) {

    //                    }
//                } else {
//                    ViewController.showToast(applicationContext, "Error: ${response.code()}")
//                }
//            }
//            override fun onFailure(call: Call<AboutusResponse>, t: Throwable) {
//                ViewController.hideLoading()
//                ViewController.showToast(applicationContext, "Try again: ${t.message}")
//            }
//        })
    }

}