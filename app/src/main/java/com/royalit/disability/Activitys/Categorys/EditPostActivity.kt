package com.royalit.disability.Activitys.Categorys

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.royalit.disability.Activitys.DashBoardActivity
import com.royalit.disability.AdaptersAndModels.AddPostResponse
import com.royalit.disability.AdaptersAndModels.EditPostImagesListAdapter
import com.royalit.disability.AdaptersAndModels.PostItemDetailsModel
import com.royalit.disability.Config.Preferences
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.Retrofit.RetrofitClient
import com.royalit.disability.databinding.ActivityEditPostBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditPostActivity : AppCompatActivity() {

    val binding: ActivityEditPostBinding by lazy {
        ActivityEditPostBinding.inflate(layoutInflater)
    }

    lateinit var post_id:String

    //single image selection
    private val IMAGE_PICK_CODE = 1000
    private var selectedImageUri: Uri? = null

    //multi Images selection
    private val REQUEST_CODE_SELECT_IMAGES = 2000
    val imageUris = mutableListOf<Uri>()

    var categoriesId: String = ""
    var lat: String = ""
    var long: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        post_id= intent.getStringExtra("post_id").toString()

        Log.e("post_id",post_id)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Edit Post"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        categoriesItemsDetailsApi()

        binding.cardImages.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        binding.cardMultiImages.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGES)
        }
        binding.addMoreImages.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGES)
        }

        binding.cardSubmit.setOnClickListener {
            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                updatePostApi()
            }
        }
    }

    private fun categoriesItemsDetailsApi() {
        ViewController.showLoading(this@EditPostActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.categoriesItemsDetailsApi(post_id).enqueue(object :
            Callback<PostItemDetailsModel> {
            override fun onResponse(call: Call<PostItemDetailsModel>, response: Response<PostItemDetailsModel>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        if (rsp.status.equals("success")) {
                            postDataSet(rsp)
                        }
                    }
                } else {
                    ViewController.showToast(this@EditPostActivity, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<PostItemDetailsModel>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(applicationContext, "Try again: ${t.message}")
            }
        })
    }
    private fun postDataSet(postDetails: PostItemDetailsModel) {
        categoriesId = postDetails.data?.product?.categoryId ?: ""
        binding.cityEdit.setText(postDetails.data?.product?.city ?: "")
        binding.titleEdit.setText(postDetails.data?.product?.title ?: "")
        binding.DesctiptionEdit.setText(postDetails.data?.product?.description ?: "")
        binding.phoneNumberEdit.setText(postDetails.data?.product?.mobile ?: "")
        binding.emailEdit.setText(postDetails.data?.product?.mail ?: "")
        binding.addressEdit.setText(postDetails.data?.product?.address ?: "")
        binding.aboutCompanyEdit.setText(postDetails.data?.product?.about ?: "")
        binding.servicesListEdit.setText(postDetails.data?.product?.services ?: "")
        binding.mapLocationEdit.setText(postDetails.data?.product?.location ?: "")

        lat = postDetails.data?.product?.latitude ?: ""
        long = postDetails.data?.product?.longitude ?: ""

        // Load cover image
        Glide.with(binding.imgCover)
            .load(RetrofitClient.Image_Path + postDetails.data?.product?.image)
            .placeholder(R.drawable.home_bannes)
            .error(R.drawable.home_bannes)
            .into(binding.imgCover)

        // Debug the images list
        binding.txtFileName2.text = postDetails.data?.images?.size.toString()+ " - Images"

        postDetails.data?.images?.let { Log.e("img_", it.toString()) }
        if (!postDetails.data?.images.isNullOrEmpty()) {
            val layoutManager = GridLayoutManager(this@EditPostActivity, 3)
            binding.recyclerviewImages.layoutManager = layoutManager
            var galleryimages = postDetails.data?.images?.toMutableList() ?: mutableListOf()
            binding.recyclerviewImages.adapter = EditPostImagesListAdapter(galleryimages) { item ->
                // Handle image item click
            }
            binding.recyclerviewImages.visibility = View.VISIBLE
            binding.txtNoImages.visibility = View.GONE
        } else {
            binding.recyclerviewImages.visibility = View.GONE
            binding.txtNoImages.visibility = View.VISIBLE
        }
    }



    //update Post Api
    private fun updatePostApi() {
        val userId = Preferences.loadStringValue(this@EditPostActivity, Preferences.userId, "")
        Log.e("userId_",userId.toString())

        val city_ =binding.cityEdit.text?.trim().toString()
        val title_ =binding.titleEdit.text?.trim().toString()
        val desctiption_ =binding.DesctiptionEdit.text?.trim().toString()
        val phoneNumber_ =binding.phoneNumberEdit.text?.trim().toString()
        val email_ =binding.emailEdit.text?.trim().toString()
        val address_ =binding.addressEdit.text?.trim().toString()
        val aboutCompany_ =binding.aboutCompanyEdit.text?.trim().toString()
        val servicesList_ =binding.servicesListEdit.text?.trim().toString()
        val mapLocation_ =binding.mapLocationEdit.text?.trim().toString()

        if(city_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter city")
            return
        }
        if(title_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter title")
            return
        }
        if(desctiption_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter desctiption")
            return
        }
        if(phoneNumber_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter phone Number")
            return
        }
        if(email_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter email")
            return
        }
        if(address_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter address")
            return
        }
        if(aboutCompany_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter aboutCompany")
            return
        }
        if(servicesList_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter services List")
            return
        }
        if(mapLocation_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter map Location")
            return
        }
        if (!validateEmail(email_)) {
            ViewController.showToast(applicationContext, "Enter Valid email")
            return
        }
        if (!validateMobileNumber(phoneNumber_)) {
            ViewController.showToast(applicationContext, "Enter Valid mobile number")
            return
        }


        val city = RequestBody.create(MultipartBody.FORM, city_)
        val category_id = RequestBody.create(MultipartBody.FORM, categoriesId)
        val title = RequestBody.create(MultipartBody.FORM, title_)
        val description = RequestBody.create(MultipartBody.FORM, desctiption_)
        val mobile = RequestBody.create(MultipartBody.FORM, phoneNumber_)
        val mail = RequestBody.create(MultipartBody.FORM, email_)
        val address = RequestBody.create(MultipartBody.FORM, address_)
        val about = RequestBody.create(MultipartBody.FORM, aboutCompany_)
        val services = RequestBody.create(MultipartBody.FORM, servicesList_)
        val location = RequestBody.create(MultipartBody.FORM, mapLocation_)
        val created_by = RequestBody.create(MultipartBody.FORM, userId.toString())
        val post_id_ = RequestBody.create(MultipartBody.FORM, post_id)
        val lat_ = RequestBody.create(MultipartBody.FORM, lat)
        val long_ = RequestBody.create(MultipartBody.FORM, long)


        //cover image
        val body: MultipartBody.Part
        if (selectedImageUri != null) {
            val file = File(getRealPathFromURI(selectedImageUri!!))
            val requestFile = RequestBody.create(MultipartBody.FORM, file)
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        } else {
            //send empty image
            body = coverEmptyImagePart()
        }

        //gallery
        val additionalImages = mutableListOf<MultipartBody.Part>()
        if (imageUris.size==0 || imageUris==null){
            additionalImages.clear()
        }else{
            for (uri in imageUris) {
                val file = File(getRealPathFromURI(uri))
                val requestFile = RequestBody.create(MultipartBody.FORM, file)
                val part = MultipartBody.Part.createFormData("additional_images[]", file.name, requestFile)
                additionalImages.add(part)
            }
        }

        val apiInterface = RetrofitClient.apiInterface
        apiInterface.updatePostApi(city, category_id, title, description, mobile, mail, address, about, services, location, created_by, post_id_, lat_, long_, body, additionalImages).enqueue(object :
            Callback<AddPostResponse> {
            override fun onResponse(call: Call<AddPostResponse>, response: Response<AddPostResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val addResponse = response.body()
                    if (addResponse != null && addResponse.status.equals("success")) {
                        startActivity(Intent(this@EditPostActivity, DashBoardActivity::class.java))
                    } else {
                        ViewController.showToast(applicationContext, "Failed")
                    }
                } else {
                    ViewController.showToast(applicationContext, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<AddPostResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(applicationContext, "Try again: ${t.message}")
                Log.e("Tryagain:_ ", t.message.toString())
            }
        })

    }

    private fun validateMobileNumber(mobile: String): Boolean {
        val mobilePattern = "^[6-9][0-9]{9}\$"
        return Patterns.PHONE.matcher(mobile).matches() && mobile.matches(Regex(mobilePattern))
    }
    private fun validateEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(Regex(emailPattern))
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        return ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //single image selection
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            val file = File(getRealPathFromURI(selectedImageUri!!))
            //binding.txtFileName.text = file.name
            Glide.with(this)
                .load(selectedImageUri)
                .placeholder(R.drawable.dummy_profile)
                .error(R.drawable.dummy_profile)
                .into(binding.imgCover)
        }

        //multi image selection
        if (requestCode == REQUEST_CODE_SELECT_IMAGES && resultCode == Activity.RESULT_OK) {
            val clipData = data?.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    imageUris.add(imageUri)
                }
            } else {
                // Single image was selected
                val imageUri = data?.data
                if (imageUri != null) {
                    imageUris.add(imageUri)
                }
            }
            binding.txtFileName2.text = imageUris.size.toString()+ " - Images"
        }
    }

    private fun coverEmptyImagePart(): MultipartBody.Part {
        // Create an empty RequestBody
        val requestFile = RequestBody.create(MultipartBody.FORM, ByteArray(0))
        return MultipartBody.Part.createFormData("image", "", requestFile)
    }

}