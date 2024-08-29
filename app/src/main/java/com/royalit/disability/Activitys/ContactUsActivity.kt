package com.royalit.disability.Activitys

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.royalit.disability.AdaptersAndModels.ContactUsResponse
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.Retrofit.RetrofitClient
import com.royalit.disability.databinding.ActivityContactUsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsActivity : AppCompatActivity() {

    val binding: ActivityContactUsBinding by lazy {
        ActivityContactUsBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Contact Us"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        contactUsApi()

    }

    private fun contactUsApi() {
        ViewController.showLoading(this@ContactUsActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.contactUsApi().enqueue(object : Callback<ContactUsResponse> {
            override fun onResponse(call: Call<ContactUsResponse>, response: Response<ContactUsResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        binding.txtPhone.text = rsp.phone
                        binding.txtEmail.text = rsp.email
                        binding.txtAddress.text = rsp.address
                    }
                } else {
                    ViewController.showToast(applicationContext, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ContactUsResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(applicationContext, "Try again: ${t.message}")
            }
        })
    }


}