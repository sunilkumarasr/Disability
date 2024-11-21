package com.smy3infotech.divyaangdisha.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Notifications.NotificationAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Notifications.NotificationModel
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdisha.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
var isNotification=""
    val binding: ActivityNotificationBinding by lazy {
        ActivityNotificationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        isNotification= intent.getStringExtra("isNotification").toString()
        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Notifications"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener {
            if(isNotification.equals("1")){
                startActivity(Intent(applicationContext,DashBoardActivity::class.java))
            }

            finish()
        }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            NotificationsListApi()
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(isNotification.equals("1")){
                    startActivity(Intent(applicationContext, DashBoardActivity::class.java))
                }
                finish()
            }
        })
    }

    private fun NotificationsListApi() {
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
    private fun NotificationDataSet(joblist: List<NotificationModel>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@NotificationActivity)
        binding.recyclerview.adapter = NotificationAdapter(joblist) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
        }
    }

}