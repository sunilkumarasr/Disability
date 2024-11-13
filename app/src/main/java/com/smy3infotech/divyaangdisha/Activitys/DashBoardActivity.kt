package com.smy3infotech.divyaangdisha.Activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener   {

    val binding: ActivityDashBoardBinding by lazy {
        ActivityDashBoardBinding.inflate(layoutInflater)
    }

    lateinit var navView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var profilepic: ImageView
    lateinit var profile_name: TextView

    private lateinit var bottomNavigationView: BottomNavigationView

    //fragments
    private val homeFragment = HomeFragment()
    private val categoriesFragment = CategoriesFragment()
    private val saleFragment = SaleFragment()
    private val profileFragment = ProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.blue),
            false
        )

        //login
        Preferences.saveStringValue(applicationContext, Preferences.LOGINCHECK, "Login")

        //side menu
        drawerLayout = binding.drawerLayout
        navView = binding.navView

        navView.setNavigationItemSelectedListener(this)
        val headerView: View = binding.navView.getHeaderView(0)
        profile_name = headerView.findViewById(R.id.text_profile)
        profilepic = headerView.findViewById(R.id.imageView_home)

        binding.imgMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }


        bottomMenu()
        getProfileApi()

        binding.imgNotification.setOnClickListener(this)
        binding.imgWhatsapp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgNotification -> {
                startActivity(Intent(this@DashBoardActivity, NotificationActivity::class.java))
            }

            R.id.imgWhatsapp -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+9441085061&text=Hi%20there"))
                startActivity(browserIntent)
            }

        }
    }

    //bottom menu
    private fun bottomMenu() {
        replaceFragment(homeFragment)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        // Set listener for item selection
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->

            menuItem.isChecked = true

            when (menuItem.itemId) {
                R.id.home -> {
                    replaceFragment(homeFragment)
                    true
                }

                R.id.categories -> {
                    replaceFragment(categoriesFragment)
                    true
                }

                R.id.sale -> {
                    replaceFragment(saleFragment)
                    true
                }

                R.id.profile -> {
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
                        profile_name.text = rsp.data?.name.toString()
                        if (!rsp.data?.image.equals("")){
                            Glide.with(profilepic).load(rsp.data?.image).into(profilepic)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
            }
        })
    }

    //side menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        drawerLayout.closeDrawers()

        val id = item.itemId

        when (id) {
            R.id.nav_company ->{
                startActivity(Intent(this@DashBoardActivity, ConmanyInfoActivity::class.java))
            }
            
            R.id.nav_contactUs ->{
                startActivity(Intent(this@DashBoardActivity, ContactUsActivity::class.java))
            }

            R.id.nav_terms ->{
                startActivity(Intent(this@DashBoardActivity, TermsAndConditionsActivity::class.java))
            }

            R.id.nav_aboutUs ->{
                startActivity(Intent(this@DashBoardActivity, AboutUsActivity::class.java))
            }

            R.id.nav_PrivacyPolicy ->{
                startActivity(Intent(this@DashBoardActivity, PrivacyPolicyActivity::class.java))
            }
        }
        return true
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
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