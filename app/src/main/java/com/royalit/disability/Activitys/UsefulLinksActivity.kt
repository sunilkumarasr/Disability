package com.royalit.disability.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.disability.Activitys.Categorys.PostCategoriesDetailsActivity
import com.royalit.disability.AdaptersAndModels.UseFullLinks.UseFullLinksAdapter
import com.royalit.disability.AdaptersAndModels.UseFullLinks.UseFullLinksModel
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.Retrofit.RetrofitClient
import com.royalit.disability.databinding.ActivityUsefulLinksBinding

class UsefulLinksActivity : AppCompatActivity() {

    val binding: ActivityUsefulLinksBinding by lazy {
        ActivityUsefulLinksBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Useful Links"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        if (!ViewController.noInterNetConnectivity(this@UsefulLinksActivity)) {
            ViewController.showToast(this@UsefulLinksActivity, "Please check your connection ")
            return
        } else {
            useFullLinksApi()
        }
    }

    private fun useFullLinksApi() {

        ViewController.showLoading(this@UsefulLinksActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.useFullLinksApi()
            .enqueue(object : retrofit2.Callback<List<UseFullLinksModel>> {
                override fun onResponse(
                    call: retrofit2.Call<List<UseFullLinksModel>>,
                    response: retrofit2.Response<List<UseFullLinksModel>>
                ) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val rsp = response.body()
                        if (rsp != null) {
                            val useFullLinks = response.body()
                            if (useFullLinks != null) {
                                DataSet(useFullLinks)
                            }
                        } else {

                        }
                    } else {
                        ViewController.showToast(this@UsefulLinksActivity, "Error: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<List<UseFullLinksModel>>,
                    t: Throwable
                ) {
                    Log.e("cat_error", t.message.toString())
                    ViewController.hideLoading()
                    ViewController.showToast(this@UsefulLinksActivity, "Try again: ${t.message}")
                }
            })
    }
    private fun DataSet(useFullLinks: List<UseFullLinksModel>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@UsefulLinksActivity)
        binding.recyclerview.adapter = UseFullLinksAdapter(useFullLinks) { item ->
            // Handle item click
            startActivity(Intent(this@UsefulLinksActivity, UsefulLinksDetailsActivity::class.java).apply {
                putExtra("url",item.url)
            })
        }
    }

}