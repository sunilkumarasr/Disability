package com.royalit.disability.Activitys

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.disability.AdaptersAndModels.ProfileResponse
import com.royalit.disability.Config.Preferences
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.Retrofit.RetrofitClient
import com.royalit.disability.databinding.ActivityAboutUsBinding
import com.royalit.disability.databinding.ActivityEditProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()
        
    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Edit Profile"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        getProfileApi()

//        Glide.with(this)
//            .load(imageUrl)
//            .placeholder(R.drawable.placeholder) // Optional: A placeholder image
//            .error(R.drawable.error) // Optional: An error image
//            .into(imageView)

    }

    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(this@EditProfileActivity, Preferences.userId, "")
        Log.e("userId_",userId.toString())

        ViewController.showLoading(this@EditProfileActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getProfileApi(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        binding.nameEdit.setText(rsp.data?.name)
                        binding.emailEdit.setText(rsp.data?.email)
                        binding.mobileEdit.setText(rsp.data?.phone)
                    }
                } else {
                    ViewController.showToast(this@EditProfileActivity, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(this@EditProfileActivity, "Try again: ${t.message}")
            }
        })
    }
    

}