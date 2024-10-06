package com.smy3infotech.divyaangdisha.Logins

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Citys.CityAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Citys.CitysModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.RegisterRequest
import com.smy3infotech.divyaangdisha.AdaptersAndModels.RegisterResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.State.StateAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.State.StateModel
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.LocationBottomSheetFragment
import com.smy3infotech.divyaangdisha.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdisha.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    var stateName: String = ""
    var stateId: String = ""
    var cityName: String = ""

    var lat: String = ""
    var longi: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        stateList()
        citysList()

        binding.txtLocation.setOnClickListener {
            bottomPopup()
        }

        binding.cardLogin.setOnClickListener{
            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                registerApi()
            }
        }

        binding.loginLinear.setOnClickListener {
            finish()
        }

    }

    private fun registerApi() {
        val name_=binding.nameEdit.text.toString()
        val email=binding.emailEdit.text?.trim().toString()
        val mobileNumber_=binding.mobileEdit.text?.trim().toString()
        val location_=binding.txtLocation.text?.trim().toString()
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
        if(location_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Location")
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

        if (!validateMobileNumber(mobileNumber_)) {
            ViewController.showToast(applicationContext, "Enter Valid mobile number")
        }else if (!validateEmail(email)) {
            ViewController.showToast(applicationContext, "Enter Valid Email")
        }else{
            ViewController.showLoading(this@RegisterActivity)

            val apiInterface = RetrofitClient.apiInterface
            val registerRequest = RegisterRequest(name_,email,mobileNumber_,location_, stateName, cityName,  password_)

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

    private fun stateList() {
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.stateListApi().enqueue(object : retrofit2.Callback<List<StateModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<StateModel>>,
                response: retrofit2.Response<List<StateModel>>
            ) {
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val stateList = response.body()
                        if (stateList != null) {
                            stateDataSet(stateList)
                        }
                    } else {

                    }
                } else {
                    ViewController.showToast(this@RegisterActivity, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: retrofit2.Call<List<StateModel>>, t: Throwable) {
                Log.e("citys_error", t.message.toString())
            }
        })
    }
    private fun stateDataSet(state: List<StateModel>) {
        val adapter = StateAdapter(this@RegisterActivity, state)
        binding.spinnerState.adapter = adapter
        binding.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedState = parent?.getItemAtPosition(position) as StateModel
                stateName = selectedState.state
                stateId = selectedState.id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun citysList() {
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.cityApi(stateId).enqueue(object : retrofit2.Callback<List<CitysModel>> {
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
                    ViewController.showToast(this@RegisterActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<CitysModel>>, t: Throwable) {
                Log.e("citys_error", t.message.toString())
            }
        })
    }
    private fun CityDataSet(citys: List<CitysModel>) {
        val adapter = CityAdapter(this@RegisterActivity, citys)
        binding.spinnerCity.adapter = adapter
        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedCity = parent?.getItemAtPosition(position) as CitysModel
                cityName = selectedCity.city
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }
    
    private fun bottomPopup() {
        val bottomSheet = LocationBottomSheetFragment()

        // Set listener to get value from the bottom sheet
        bottomSheet.setOnItemClickListener(object : LocationBottomSheetFragment.OnItemClickListener {
            override fun onItemSelected(lat_value: String, longi_value: String) {
                // Handle the value received from the bottom sheet
                binding.txtLocation.setText(" $lat_value - $longi_value ")
                lat = lat_value
                longi = longi_value
                Toast.makeText(this@RegisterActivity, "Selected: $lat_value - $longi_value ", Toast.LENGTH_SHORT).show()
            }
        })

        bottomSheet.show(supportFragmentManager, "MyBottomSheetFragment")
    }

}