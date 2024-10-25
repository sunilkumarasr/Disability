package com.smy3infotech.divyaangdisha.Activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.smy3infotech.divyaangdisha.Config.Preferences
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.Logins.LoginActivity
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.databinding.ActivitySplashBinding

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