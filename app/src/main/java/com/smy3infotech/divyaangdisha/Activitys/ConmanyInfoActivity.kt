package com.smy3infotech.divyaangdisha.Activitys

import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.smy3infotech.divyaangdisha.AdaptersAndModels.AboutusResponse
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdisha.databinding.ActivityAboutUsBinding
import com.smy3infotech.divyaangdisha.databinding.ActivityConmanyInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConmanyInfoActivity : AppCompatActivity() {

    val binding: ActivityConmanyInfoBinding by lazy {
        ActivityConmanyInfoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Company Info"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

       // companyInfoApi()

    }

    private fun companyInfoApi() {
        ViewController.showLoading(this@ConmanyInfoActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.aboutusApi().enqueue(object : Callback<AboutusResponse> {
            override fun onResponse(call: Call<AboutusResponse>, response: Response<AboutusResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        binding.txtNote.text = Html.fromHtml(rsp.description, Html.FROM_HTML_MODE_LEGACY)
                    }
                } else {
                    ViewController.showToast(applicationContext, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<AboutusResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(applicationContext, "Try again: ${t.message}")
            }
        })
    }

}