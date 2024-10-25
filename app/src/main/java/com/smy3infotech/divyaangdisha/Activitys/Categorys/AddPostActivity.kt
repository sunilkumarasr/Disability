package com.smy3infotech.divyaangdisha.Activitys.Categorys

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.smy3infotech.divyaangdisha.Activitys.DashBoardActivity
import com.smy3infotech.divyaangdisha.AdaptersAndModels.AddPostResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.CatListAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Categorys.CategoriesModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Categorys.SubCategoriesModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.ImageAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SubCatListAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SubCategoriesItems.SubCategoriesItemsModel
import com.smy3infotech.divyaangdisha.Config.Preferences
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdisha.databinding.ActivityAddPostBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddPostActivity : AppCompatActivity() {

    val binding: ActivityAddPostBinding by lazy {
        ActivityAddPostBinding.inflate(layoutInflater)
    }

    //location
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private lateinit var adapter: ArrayAdapter<String>
    private val suggestionsList = mutableListOf<String>()
    private val placeIdList = mutableListOf<String>()
    var lat: String = ""
    var longi: String = ""

    //single image selection
    private val IMAGE_PICK_CODE = 1000
    private var selectedImageUri: Uri? = null

    //multi Images selection
    private val REQUEST_CODE_SELECT_IMAGES = 2000
    val imageUris = mutableListOf<Uri>()
    private lateinit var imageAdapter: ImageAdapter

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
            ViewController.showToast(this@AddPostActivity, "Accept permissions")
        }
    }
    var imageType: String = ""

    var categoriesId: String = ""
    var subcategoriesId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Add Post"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { exitDialog() }

        location()
        categoriesApi()
        setupRecyclerView()

        binding.cardImages.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
            } else {
                requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
            }
            imageType = "single"
        }

//        binding.cardMultiImages.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
//            } else {
//                requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
//            }
//            imageType = "multi"
//        }

        //multipile images
        binding.addMoreImages.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Allow multiple selections
            }

            // Launch the intent
            startActivityForResult(Intent.createChooser(intent, "Select Images"), REQUEST_CODE_SELECT_IMAGES)
            imageType = "multi"
        }

        binding.cardSubmit.setOnClickListener {
            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                addPostApi()
            }
        }

    }

    //location
    private fun location() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@AddPostActivity)

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(this@AddPostActivity, RetrofitClient.MapKey)
        }
        placesClient = Places.createClient(this@AddPostActivity)
        adapter = ArrayAdapter(this@AddPostActivity, android.R.layout.simple_list_item_1, suggestionsList)
        binding.listViews.adapter = adapter

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    searchPlace(query)
                }else{
                    binding.listViews.visibility = View.GONE
                }

            }
        }

        binding.locationEdit.addTextChangedListener(textWatcher)
        binding.listViews.setOnItemClickListener { parent, view, position, id ->
            val selectedPlaceId = placeIdList[position]
            val selectedPlaceName = suggestionsList[position]
            binding.locationEdit.removeTextChangedListener(textWatcher)
            binding.locationEdit.setText(selectedPlaceName)
            binding.locationEdit.addTextChangedListener(textWatcher)

            binding.listViews.visibility = View.GONE

            fetchPlaceDetails(selectedPlaceId, selectedPlaceName)
        }
    }
    private fun searchPlace(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                suggestionsList.clear()
                placeIdList.clear()

                for (prediction in response.autocompletePredictions) {
                    suggestionsList.add(prediction.getFullText(null).toString())
                    placeIdList.add(prediction.placeId)
                }

                if (suggestionsList.isNotEmpty()) {
                    binding.listViews.visibility = View.VISIBLE
                    adapter.notifyDataSetChanged()
                } else {
                    binding.listViews.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MapsActivity", "Place search failed: $exception")
            }
    }
    private fun fetchPlaceDetails(placeId: String, placeName: String) {
        val placeFields = listOf(Place.Field.LAT_LNG, Place.Field.NAME)

        val request = com.google.android.libraries.places.api.net.FetchPlaceRequest.builder(placeId, placeFields).build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val place = response.place
                val latLng = place.latLng
                if (latLng != null) {
                    lat = latLng.latitude.toString()
                    longi = latLng.longitude.toString()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MapsActivity", "Place details fetch failed: $exception")
            }
    }

    //categories list
    private fun categoriesApi() {
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.categoriesApi().enqueue(object : retrofit2.Callback<List<CategoriesModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<CategoriesModel>>,
                response: retrofit2.Response<List<CategoriesModel>>
            ) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val categories = response.body()
                        if (categories != null) {
                            categoriesDataSet(categories)
                        }
                    } else {

                    }
                } else {
                    ViewController.showToast(this@AddPostActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<CategoriesModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
            }
        })

    }
    private fun categoriesDataSet(categorysList: List<CategoriesModel>) {
        val adapter = CatListAdapter(this@AddPostActivity, categorysList)
        binding.spinnerCategory.adapter = adapter
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedState = parent?.getItemAtPosition(position) as CategoriesModel
                categoriesId = selectedState.category_id
                subCategoriesApi(selectedState.category_id)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }


    //subcategories list
    private fun subCategoriesApi(catId: String) {
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.subcategoriesApi(catId).enqueue(object : retrofit2.Callback<List<SubCategoriesModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<SubCategoriesModel>>,
                response: retrofit2.Response<List<SubCategoriesModel>>
            ) {
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val categories = response.body()
                        if (categories != null) {
                            subCategoriesDataSet(categories)
                        }
                    } else {
                        subcategoriesId = ""
                    }
                } else {
                    ViewController.showToast(this@AddPostActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<SubCategoriesModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                subcategoriesId = ""
                // Clear the spinner on failure
                clearSpinner()
            }
        })

    }
    private fun subCategoriesDataSet(subcategorysList: List<SubCategoriesModel>) {
        val adapter = SubCatListAdapter(this@AddPostActivity, subcategorysList)
        binding.spinnerSubCategory.adapter = adapter
        binding.spinnerSubCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedState = parent?.getItemAtPosition(position) as SubCategoriesModel
                subcategoriesId = selectedState.id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }
    private fun clearSpinner() {
        val emptyAdapter = ArrayAdapter<SubCategoriesModel>(this@AddPostActivity, android.R.layout.simple_spinner_item, ArrayList())
        binding.spinnerSubCategory.adapter = emptyAdapter
    }

    //add post
    private fun addPostApi() {
        val userId = Preferences.loadStringValue(this@AddPostActivity, Preferences.userId, "")
        Log.e("userId_",userId.toString())

        val mapLocation_ =binding.locationEdit.text?.trim().toString()
        val title_ =binding.titleEdit.text?.trim().toString()
        val desctiption_ =binding.desctiptionEdit.text?.trim().toString()
        val phoneNumber_ =binding.phoneNumberEdit.text?.trim().toString()
        val landLineNumber_ =binding.landLineNumberEdit.text?.trim().toString()
        val email_ =binding.emailEdit.text?.trim().toString()
        val address_ =binding.addressEdit.text?.trim().toString()
        val aboutCompany_ =binding.aboutCompanyEdit.text?.trim().toString()
        val servicesList_ =binding.servicesListEdit.text?.trim().toString()


        if(mapLocation_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Location")
            return
        }
        if(lat.isEmpty() || longi.isEmpty()){
            ViewController.showToast(applicationContext, "Enter select correct location")
            return
        }
        if(title_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter title")
            return
        }
        if(desctiption_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter description")
            return
        }
        if(phoneNumber_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter phone Number")
            return
        }
        if(landLineNumber_.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Land Line Number")
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
        if (selectedImageUri == null) {
            ViewController.showToast(applicationContext, "Please select cover image")
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
        if(categoriesId.isEmpty()){
            ViewController.showToast(applicationContext, "Select  Categories")
            return
        }
        if(subcategoriesId.isEmpty()){
            ViewController.showToast(applicationContext, "Select SubCategories")
            return
        }

        val mapLocation = RequestBody.create(MultipartBody.FORM, mapLocation_)
        val category_id = RequestBody.create(MultipartBody.FORM, categoriesId)
        val subcategory_id = RequestBody.create(MultipartBody.FORM, subcategoriesId)
        val title = RequestBody.create(MultipartBody.FORM, title_)
        val description = RequestBody.create(MultipartBody.FORM, desctiption_)
        val mobile = RequestBody.create(MultipartBody.FORM, phoneNumber_)
        val land_mobile = RequestBody.create(MultipartBody.FORM, landLineNumber_)
        val mail = RequestBody.create(MultipartBody.FORM, email_)
        val address = RequestBody.create(MultipartBody.FORM, address_)
        val about = RequestBody.create(MultipartBody.FORM, aboutCompany_)
        val services = RequestBody.create(MultipartBody.FORM, servicesList_)
        val created_by = RequestBody.create(MultipartBody.FORM, userId.toString())
        val lat_ = RequestBody.create(MultipartBody.FORM, lat)
        val long_ = RequestBody.create(MultipartBody.FORM, longi)

        //cover image
        val file = File(getRealPathFromURI(selectedImageUri!!))
        val requestFile = RequestBody.create(MultipartBody.FORM, file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)


//        //gallery
//        Log.e("img_", imageUris.toString())
//        imageUris.let { Log.e("img_1", it.toString()) }
//        val additionalImages = mutableListOf<MultipartBody.Part>()
//        for (uri in imageUris) {
//            val file = File(getRealPathFromURI(uri))
//            val requestFile = RequestBody.create(MultipartBody.FORM, file)
//            val part = MultipartBody.Part.createFormData("additional_images[]", file.name, requestFile)
//            additionalImages.add(part)
//        }

        val additionalImages = mutableListOf<MultipartBody.Part>()
        for (uri in imageUris) {
            val file = File(getRealPathFromURI(uri))
            val requestFile = RequestBody.create(MultipartBody.FORM, file)
            val part = MultipartBody.Part.createFormData("additional_images[]", file.name, requestFile)
            additionalImages.add(part)
        }


        ViewController.showLoading(this@AddPostActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.addPostApi(mapLocation, category_id, subcategory_id,  title, description, mobile,land_mobile, mail, address, about, services, created_by, lat_, long_, body, additionalImages).enqueue(object :
            Callback<AddPostResponse> {
            override fun onResponse(call: Call<AddPostResponse>, response: Response<AddPostResponse>) {
                ViewController.hideLoading()

                if (response.isSuccessful) {
                    val addResponse = response.body()
                    if (addResponse != null && addResponse.status.equals("success")) {
                        ViewController.showToast(applicationContext, "success")
                        startActivity(Intent(this@AddPostActivity, DashBoardActivity::class.java))
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

        if (imageType.equals("single")){
            //single image selection
            if (resultCode == Activity.RESULT_OK && data != null) {
                selectedImageUri = data.data!!
                val file = File(getRealPathFromURI(selectedImageUri!!))
                binding.txtFileName.text = file.name
            }
        }else{
            //multi image selection
            if (resultCode == Activity.RESULT_OK) {
                val clipData = data?.clipData
                var invalidImageCount = 0

                if (clipData != null) {
                    for (i in 0 until clipData.itemCount) {
                        val imageUri = clipData.getItemAt(i).uri
                        if (isValidImage(imageUri)) {
                            if (!imageUris.contains(imageUri)) {
                                imageUris.add(imageUri)
                            }
                        } else {
                            invalidImageCount++
                        }
                    }
                } else {
                    // Handle single image selection
                    val imageUri = data?.data
                    if (imageUri != null) {
                        if (isValidImage(imageUri)) {
                            if (!imageUris.contains(imageUri)) {
                                imageUris.add(imageUri)
                            }
                        } else {
                            invalidImageCount++
                        }
                    }
                }

                binding.txtFileName2.text = "${imageUris.size} - Images added"

                // Show a summary of invalid images
                if (invalidImageCount > 0) {
                    ViewController.showToast(
                        applicationContext,
                        "$invalidImageCount invalid image(s) ignored"
                    )
                }
            }
        }

    }

    private fun isValidImage(uri: Uri): Boolean {
        val mimeType = contentResolver.getType(uri)
        return mimeType == "image/jpeg" || mimeType == "image/png"
    }


    private fun setupRecyclerView() {
        imageAdapter = ImageAdapter(imageUris) { imageUri ->
            // Remove the selected image
            imageUris.remove(imageUri)
            imageAdapter.updateImages(imageUris)
            binding.txtFileName2.text = "${imageUris.size} - Images"
        }
        binding.recyclerViewImages.layoutManager = GridLayoutManager(this@AddPostActivity, 3)
        binding.recyclerViewImages.adapter = imageAdapter
    }

    override fun onBackPressed() {
        exitDialog()
    }

    private fun exitDialog(){
        val dialogBuilder = AlertDialog.Builder(this@AddPostActivity)
//        dialogBuilder.setTitle("Exit")
        dialogBuilder.setMessage("Are you sure want to exit this Adding?")
        dialogBuilder.setPositiveButton("Yes", { dialog, whichButton ->
            finish()
            dialog.dismiss()
        })
        dialogBuilder.setNegativeButton("No", { dialog, whichButton ->
            dialog.dismiss()
        })
        val b = dialogBuilder.create()
        b.show()
    }


}