package com.smy3infotech.divyaangdisha.Activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.smy3infotech.divyaangdisha.AdaptersAndModels.ProfileResponse
import com.smy3infotech.divyaangdisha.Config.Preferences
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.Fragments.CategoriesFragment
import com.smy3infotech.divyaangdisha.Fragments.HomeFragment
import com.smy3infotech.divyaangdisha.Fragments.ProfileFragment
import com.smy3infotech.divyaangdisha.Fragments.SaleFragment
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdisha.databinding.ActivityDashBoardBinding
import me.ibrahimsn.lib.SmoothBottomBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardActivity : AppCompatActivity(), View.OnClickListener  {

    val binding: ActivityDashBoardBinding by lazy {
        ActivityDashBoardBinding.inflate(layoutInflater)
    }

    private lateinit var bottomBar: SmoothBottomBar

    //fragments
    private val homeFragment = HomeFragment()
    private val categoriesFragment = CategoriesFragment()
    private val saleFragment = SaleFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //login
        Preferences.saveStringValue(applicationContext, Preferences.LOGINCHECK, "Login")


        bottomMenu()
        getProfileApi()

        binding.imgNotification.setOnClickListener(this)
        binding.imgWhatsapp.setOnClickListener(this)
        binding.imgChat.setOnClickListener(this)

        val intent=getIntent().getStringExtra("isNotification")
        if(intent=="1")
        {
            startActivity(Intent(this@DashBoardActivity, NotificationActivity::class.java))
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val intent=getIntent().getStringExtra("isNotification")
        if(intent=="1")
        {
            startActivity(Intent(this@DashBoardActivity, NotificationActivity::class.java))
        }
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgNotification -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)

                startActivity(Intent(this@DashBoardActivity, NotificationActivity::class.java))
            }

            R.id.imgWhatsapp -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+9441085061&text=Hi%20there"))
                startActivity(browserIntent)
            }

            R.id.imgChat -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)

                startActivity(Intent(this@DashBoardActivity, ChatListActivity::class.java))
            }

        }
    }

    //bottom menu
    private fun bottomMenu() {
        replaceFragment(homeFragment)

        bottomBar = findViewById(R.id.bottomBar)

        bottomBar.setOnItemSelectedListener { index ->
            when (index) {
                0 -> {
                    replaceFragment(homeFragment)
                    true
                }
                1 -> {
                    replaceFragment(categoriesFragment)
                    true
                }
                2 -> {
                    replaceFragment(saleFragment)
                    true
                }
                3 -> {
                    replaceFragment(profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(this@DashBoardActivity , Preferences.userId, "")
        Log.e("userId_",userId.toString())
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getProfileApi(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        binding.txtName.text = "Hello, "+ rsp.data?.name.toString()
//                        if (!rsp.data?.image.equals("")){
//                            Glide.with(profilepic).load(rsp.data?.image).into(profilepic)
//                        }
                    }
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
            }
        })
    }

    override fun onBackPressed() {
        exitDialog()
    }

    private fun exitDialog(){
        val dialogBuilder = AlertDialog.Builder(this@DashBoardActivity)
        dialogBuilder.setTitle("Exit")
        dialogBuilder.setMessage("Are you sure want to exit this app?")
        dialogBuilder.setPositiveButton("OK", { dialog, whichButton ->
            finishAffinity()
            dialog.dismiss()
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->
            dialog.dismiss()
        })
        val b = dialogBuilder.create()
        b.show()
    }

}