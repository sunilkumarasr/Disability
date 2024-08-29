package com.royalit.disability.Activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.royalit.disability.AdaptersAndModels.CityAdapter
import com.royalit.disability.AdaptersAndModels.CitysModel
import com.royalit.disability.Config.Preferences
import com.royalit.disability.Config.ViewController
import com.royalit.disability.Fragments.CategoriesFragment
import com.royalit.disability.Fragments.HomeFragment
import com.royalit.disability.Fragments.ProfileFragment
import com.royalit.disability.Fragments.SaleFragment
import com.royalit.disability.R
import com.royalit.disability.Retrofit.RetrofitClient
import com.royalit.disability.databinding.ActivityDashBoardBinding

class DashBoardActivity : AppCompatActivity(), View.OnClickListener  {

    val binding: ActivityDashBoardBinding by lazy {
        ActivityDashBoardBinding.inflate(layoutInflater)
    }

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

        bottomMenu()

        citysList()

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


    private fun citysList() {
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.cityApi().enqueue(object : retrofit2.Callback<List<CitysModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<CitysModel>>,
                response: retrofit2.Response<List<CitysModel>>
            ) {
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val cityList = response.body()
                        if (cityList != null) {
                            CityDataSet(cityList)
                        }
                    } else {

                    }
                } else {
                    ViewController.showToast(this@DashBoardActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<CitysModel>>, t: Throwable) {
                Log.e("citys_error", t.message.toString())
            }
        })
    }
    private fun CityDataSet(citys: List<CitysModel>) {
        val adapter = CityAdapter(this@DashBoardActivity, citys)
        binding.citySpinner.adapter = adapter
    }

    private fun bottomMenu() {
        replaceFragment(homeFragment)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        // Set listener for item selection
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->

            menuItem.isChecked = true

            when (menuItem.itemId) {
                R.id.home -> {
                    replaceFragment(homeFragment)
                    binding.cardSearch.visibility = View.VISIBLE
                    true
                }

                R.id.categories -> {
                    replaceFragment(categoriesFragment)
                    binding.cardSearch.visibility = View.VISIBLE
                    true
                }

                R.id.sale -> {
                    replaceFragment(saleFragment)
                    binding.cardSearch.visibility = View.VISIBLE
                    true
                }

                R.id.profile -> {
                    replaceFragment(profileFragment)
                    binding.cardSearch.visibility = View.GONE
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

    override fun onBackPressed() {
         super.onBackPressed()
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