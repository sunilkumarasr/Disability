package com.royalit.disability.Logins

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.royalit.disability.Activitys.DashBoardActivity
import com.royalit.disability.AdaptersAndModels.LoginRequest
import com.royalit.disability.AdaptersAndModels.LoginResponse
import com.royalit.disability.AdaptersAndModels.RegisterRequest
import com.royalit.disability.AdaptersAndModels.RegisterResponse
import com.royalit.disability.Config.ViewController
import com.royalit.disability.Retrofit.RetrofitClient
import com.royalit.disability.databinding.ActivityRegisterBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.cardLogin.setOnClickListener{
            registerApi()
        }

        binding.loginLinear.setOnClickListener {
            finish()
        }

    }


    private fun registerApi() {
        val name_=binding.nameEdit.text.toString()
        val email=binding.emailEdit.text.toString()
        val mobileNumber_=binding.mobileEdit.text?.trim().toString()
        val password_=binding.passwordEdit.text?.trim().toString()
        val Cpassword_=binding.CpasswordEdit.text?.trim().toString()

        if(name_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter name")
            return
        }
        if(email.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Email")
            return
        }
        if(mobileNumber_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter mobile number")
            return
        }
        if(password_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter password")
            return
        }
        if(Cpassword_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Conform password")
            return
        }
        if(password_!=Cpassword_){
            ViewController.showToast(applicationContext, "password and conform password not match")
            return
        }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
            return
        }else if (!validateMobileNumber(mobileNumber_)) {
            ViewController.showToast(applicationContext, "Enter Valid mobile number")
        }else if (!validateEmail(email)) {
            ViewController.showToast(applicationContext, "Enter Valid Email")
        }else{
            ViewController.showLoading(this@RegisterActivity)

            val apiInterface = RetrofitClient.apiInterface
            val registerRequest = RegisterRequest(name_,email,mobileNumber_,password_)

            apiInterface.registerApi(registerRequest).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null && loginResponse.status.equals("success")) {
                            ViewController.showToast(applicationContext, "success please Login")
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        } else {
                            ViewController.showToast(applicationContext, "Registration Failed")
                        }
                    } else {
                        ViewController.showToast(applicationContext, "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.showToast(applicationContext, "Try again: ${t.message}")
                }
            })

        }
    }


    private fun validateMobileNumber(mobile: String): Boolean {
        val mobilePattern = "^[6-9][0-9]{9}\$"
        return Patterns.PHONE.matcher(mobile).matches() && mobile.matches(Regex(mobilePattern))
    }


    private fun validateEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(Regex(emailPattern))
    }

}