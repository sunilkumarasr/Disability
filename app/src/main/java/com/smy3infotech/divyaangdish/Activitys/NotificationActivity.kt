package com.smy3infotech.divyaangdish.Activitys

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.smy3infotech.divyaangdish.AdaptersAndModels.Notifications.NotificationAdapter
import com.smy3infotech.divyaangdish.AdaptersAndModels.Notifications.NotificationModel
import com.smy3infotech.divyaangdish.Config.ViewController
import com.smy3infotech.divyaangdish.R
import com.smy3infotech.divyaangdish.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdish.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    val binding: ActivityNotificationBinding by lazy {
        ActivityNotificationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Notifications"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }



        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            NotificationsListApi()
        }
    }

    private fun NotificationsListApi() {
        if(!ViewController.noInterNetConnectivity(this@NotificationActivity)){
            ViewController.showToast(this@NotificationActivity, "Please check your connection ")
            return
        }else {
            ViewController.showLoading(this@NotificationActivity)
            val apiInterface = RetrofitClient.apiInterface
            apiInterface.NotificationsListApi().enqueue(object : retrofit2.Callback<List<NotificationModel>> {
                override fun onResponse(
                    call: retrofit2.Call<List<NotificationModel>>,
                    response: retrofit2.Response<List<NotificationModel>>
                ) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val rsp = response.body()
                        if (rsp != null) {
                            val joblist = response.body()
                            if (joblist != null) {
                                NotificationDataSet(joblist)
                            }
                        }
                    } else {
                        ViewController.showToast(
                            this@NotificationActivity,
                            "Error: ${response.code()}"
                        )
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<NotificationModel>>, t: Throwable) {
                    Log.e("cat_error", t.message.toString())
                    ViewController.hideLoading()
                    ViewController.showToast(this@NotificationActivity, "Try again: ${t.message}")
                }
            })
        }

    }
    private fun NotificationDataSet(joblist: List<NotificationModel>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@NotificationActivity)
        binding.recyclerview.adapter = NotificationAdapter(joblist) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
        }
    }

}