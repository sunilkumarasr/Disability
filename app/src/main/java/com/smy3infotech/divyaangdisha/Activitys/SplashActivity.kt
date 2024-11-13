package com.smy3infotech.divyaangdisha.Activitys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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

        askNotificationPermission()
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Handler(Looper.getMainLooper()).postDelayed({
            }, 1500)
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }


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
            val notificationMessage = intent.getStringExtra("notification_message")

            if (loginCheck.equals("Login")) {
                if (notificationMessage != null) {
                    startActivity(Intent(this@SplashActivity, NotificationActivity::class.java))
                }else{
                    startActivity(Intent(this@SplashActivity, DashBoardActivity::class.java))
                }
            }else{
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }

        }




    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.isNotEmpty()) {
                Handler(Looper.getMainLooper()).postDelayed({
                }, 1500)
            }
        }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Handler(Looper.getMainLooper()).postDelayed({

                }, 1500)
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // Display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
                ActivityCompat.requestPermissions(
                    this@SplashActivity,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    200
                );
            } else {
                // Directly ask for the permission
                ActivityCompat.requestPermissions(
                    this@SplashActivity,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    200
                );
                //requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Handler(Looper.getMainLooper()).postDelayed({
        }, 1500)
    }


}