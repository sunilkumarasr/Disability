package com.royalit.disability.Activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.royalit.disability.Config.ViewController
import com.royalit.disability.Fragments.CategoriesFragment
import com.royalit.disability.Fragments.HomeFragment
import com.royalit.disability.Fragments.ProfileFragment
import com.royalit.disability.Fragments.SaleFragment
import com.royalit.disability.R
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
            ContextCompat.getColor(this, R.color.white),
            false
        )


        bottomMenu()

        binding.imgNotification.setOnClickListener(this)



    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgNotification -> {
                startActivity(Intent(this@DashBoardActivity, NotificationActivity::class.java))
            }
        }
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
        finishAffinity()
    }

}