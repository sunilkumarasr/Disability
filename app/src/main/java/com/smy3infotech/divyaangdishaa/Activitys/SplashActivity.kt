package com.smy3infotech.divyaangdishaa.Activitys

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.smy3infotech.divyaangdishaa.Config.Preferences
import com.smy3infotech.divyaangdishaa.Config.ViewController
import com.smy3infotech.divyaangdishaa.Logins.LoginActivity
import com.smy3infotech.divyaangdishaa.R
import com.smy3infotech.divyaangdishaa.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.white), false)


        //clear location address
        Preferences.saveStringValue(applicationContext, Preferences.lat,
            ""
        )
        Preferences.saveStringValue(applicationContext, Preferences.longi,
            ""
        )
        Preferences.saveStringValue(applicationContext, Preferences.km,
            ""
        )
        Preferences.saveStringValue(applicationContext, Preferences.location,
            ""
        )


        binding.cardStart.setOnClickListener {
            val loginCheck = Preferences.loadStringValue(applicationContext, Preferences.LOGINCHECK, "")

            if (loginCheck.equals("Login")) {
                startActivity(Intent(this@SplashActivity, DashBoardActivity::class.java))
            }else{
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }

        }


    }

}