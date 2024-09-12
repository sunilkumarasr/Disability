package com.royalit.disability.Activitys

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.util.Patterns
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.royalit.disability.AdaptersAndModels.Citys.CitysModel
import com.royalit.disability.AdaptersAndModels.ProfileResponse
import com.royalit.disability.AdaptersAndModels.State.StateModel
import com.royalit.disability.AdaptersAndModels.UpdateProfileResponse
import com.royalit.disability.Config.Preferences
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.Retrofit.RetrofitClient
import com.royalit.disability.databinding.ActivityEditProfileBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    val requestPermissions = registerForActivityResult(RequestMultiplePermissions()) { results ->
        var permission = false;
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            (
                    ContextCompat.checkSelfPermission(
                        applicationContext,
                        READ_MEDIA_IMAGES
                    ) == PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(
                                applicationContext,
                                READ_MEDIA_VIDEO
                            ) == PERMISSION_GRANTED
                    )
        ) {
            permission = true
            // Full access on Android 13 (API level 33) or higher
        } else if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
            ContextCompat.checkSelfPermission(
                applicationContext,
                READ_MEDIA_VISUAL_USER_SELECTED
            ) == PERMISSION_GRANTED
        ) {
            permission = true
            // Partial access on Android 14 (API level 34) or higher
        } else if (ContextCompat.checkSelfPermission(
                applicationContext,
                READ_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED
        ) {
            permission = true
            // Full access up to Android 12 (API level 32)
        } else {
            permission = false
        }
        if (permission) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        } else {
            ViewController.showToast(this@EditProfileActivity, "Accept permissions")
        }
    }

    //image selection
    private val IMAGE_PICK_CODE = 1000
    private var selectedImageUri: Uri? = null

    var stateId: String = ""
    var cityId: String = ""

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

        binding.cardChoose.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
            } else {
                requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
            }
        }

        binding.cardUpdate.setOnClickListener {
            updateProfileApi()
        }

    }

    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(this@EditProfileActivity, Preferences.userId, "")
        Log.e("userId_", userId.toString())

        ViewController.showLoading(this@EditProfileActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getProfileApi(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        //state and city
                        stateId = rsp.data?.state.toString()
                        cityId = rsp.data?.city.toString()
                        stateList()
                        citysList()

                        binding.nameEdit.setText(rsp.data?.name)
                        binding.emailEdit.setText(rsp.data?.email)
                        binding.mobileEdit.setText(rsp.data?.phone)
                        binding.txtLocation.setText(rsp.data?.location)
                        if (!rsp.data?.image.equals("")) {
                            Glide.with(binding.profileImage).load(rsp.data?.image)
                                .into(binding.profileImage)
                        }
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

    private fun updateProfileApi() {
        val userId = Preferences.loadStringValue(this@EditProfileActivity, Preferences.userId, "")

        val name = binding.nameEdit.text?.trim().toString()
        val email = binding.emailEdit.text?.trim().toString()
        val mobile = binding.mobileEdit.text?.trim().toString()
        val location = binding.txtLocation.text?.trim().toString()

        // Validate inputs
        if (name.isEmpty()) {
            ViewController.showToast(applicationContext, "Enter name")
            return
        }
        if (email.isEmpty()) {
            ViewController.showToast(applicationContext, "Enter email")
            return
        }
        if (mobile.isEmpty()) {
            ViewController.showToast(applicationContext, "Enter mobile")
            return
        }
        if (location.isEmpty()) {
            ViewController.showToast(applicationContext, "Enter location")
            return
        }
        if (!validateMobileNumber(mobile)) {
            ViewController.showToast(applicationContext, "Enter Valid mobile number")
            return
        }

        // Prepare form data
        val userId_ = RequestBody.create(MultipartBody.FORM, userId.toString())
        val name_ = RequestBody.create(MultipartBody.FORM, name)
        val email_ = RequestBody.create(MultipartBody.FORM, email)
        val mobile_ = RequestBody.create(MultipartBody.FORM, mobile)

        val body: MultipartBody.Part
        if (selectedImageUri != null) {
            val file = File(getRealPathFromURI(selectedImageUri!!))
            val requestFile = RequestBody.create(MultipartBody.FORM, file)
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        } else {
            //send empty image
            body = createEmptyImagePart()
        }

        ViewController.showLoading(this@EditProfileActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.updateProfileApi(userId_, name_, email_, mobile_, body)
            .enqueue(object : Callback<UpdateProfileResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileResponse>,
                    response: Response<UpdateProfileResponse>
                ) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val addResponse = response.body()
                        if (addResponse != null) {
                            startActivity(
                                Intent(this@EditProfileActivity, DashBoardActivity::class.java)
                            )
                        }
                    } else {
                        ViewController.showToast(applicationContext, "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.showToast(applicationContext, "Try again: ${t.message}")
                    Log.e("Tryagain:_ ", t.message.toString())
                }
            })

    }


    private fun getRealPathFromURI(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        return ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //single image selection
        if (data != null) {
            selectedImageUri = data.data!!
        }
        val file = File(getRealPathFromURI(selectedImageUri!!))
        binding.txtFileName.text = file.name
    }


    //update profile
    private fun createEmptyImagePart(): MultipartBody.Part {
        // Create an empty RequestBody
        val requestFile = RequestBody.create(MultipartBody.FORM, ByteArray(0))
        return MultipartBody.Part.createFormData("image", "", requestFile)
    }

    private fun validateMobileNumber(mobile: String): Boolean {
        val mobilePattern = "^[6-9][0-9]{9}\$"
        return Patterns.PHONE.matcher(mobile).matches() && mobile.matches(Regex(mobilePattern))
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
                    ViewController.showToast(this@EditProfileActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<StateModel>>, t: Throwable) {
                Log.e("citys_error", t.message.toString())
            }
        })
    }

    private fun stateDataSet(state: List<StateModel>) {

        for (i in state.indices) {
            if (state[i].id.equals(stateId)) {
                binding.spinnerState.text = state[i].state
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
                    ViewController.showToast(this@EditProfileActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<CitysModel>>, t: Throwable) {
                Log.e("citys_error", t.message.toString())
            }
        })
    }

    private fun CityDataSet(citys: List<CitysModel>) {
        for (i in citys.indices) {
            if (citys[i].id.equals(cityId)) {
                binding.spinnerCity.text = citys[i].city
            }
        }
    }

}