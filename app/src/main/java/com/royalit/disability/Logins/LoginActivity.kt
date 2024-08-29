package com.royalit.disability.Logins

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.royalit.disability.Activitys.DashBoardActivity
import com.royalit.disability.AdaptersAndModels.LoginRequest
import com.royalit.disability.AdaptersAndModels.LoginResponse
import com.royalit.disability.Config.Preferences
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.Retrofit.ApiInterface
import com.royalit.disability.Retrofit.RetrofitClient
import com.royalit.disability.Retrofit.RetrofitClient.apiInterface
import com.royalit.disability.databinding.ActivityLoginBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.txtForgot.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotActivity::class.java))
        }

        binding.registerLinear.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.cardLogin.setOnClickListener {
            loginApi()
        }

    }


    private fun loginApi() {
        val email=binding.emailEdit.text?.trim().toString()
        val password_=binding.passwordEdit.text?.trim().toString()

        if(email.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Email")
            return
        }
        if(password_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter password")
            return
        }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
            return
        }else if (!validateEmail(email)) {
            ViewController.showToast(applicationContext, "Enter Valid email")
        }else{
            ViewController.showLoading(this@LoginActivity)

            val apiInterface = RetrofitClient.apiInterface
            val loginRequest = LoginRequest(email, password_)

            apiInterface.loginApi(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null && loginResponse.status.equals("success")) {
                            Preferences.saveStringValue(applicationContext, Preferences.userId,
                                loginResponse.user?.id.toString()
                            )
                            startActivity(Intent(this@LoginActivity, DashBoardActivity::class.java))
                            finish()
                        } else {
                            ViewController.showToast(applicationContext, "Login Failed")
                        }
                    } else {
                        ViewController.showToast(applicationContext, "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.showToast(applicationContext, "Try again: ${t.message}")
                }
            })

        }
    }


    private fun validateEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(Regex(emailPattern))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


}