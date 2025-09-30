package com.smy3infotech.divyaangdisha.Activitys.Categorys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Categorys.SubCategoriesModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.PostBannersModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.ProfileResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SubCategories.SubCategoriesAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SubCategoriesItems.SubCategoriesItemsAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SubCategoriesItems.SubCategoriesItemsModel
import com.smy3infotech.divyaangdisha.Config.Preferences
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.LocationBottomSheetFragment
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdisha.databinding.ActivityCategoriesBasedItemsListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesBasedItemsListActivity : AppCompatActivity(), OnMapReadyCallback  {

    val binding: ActivityCategoriesBasedItemsListBinding by lazy {
        ActivityCategoriesBasedItemsListBinding.inflate(layoutInflater)
    }

    lateinit var category_id:String
    lateinit var category_Name:String
    lateinit var sub_id: String

    private lateinit var catAdapter: SubCategoriesItemsAdapter
    private var categoriesList = ArrayList<SubCategoriesItemsModel>()

    var lat: String = ""
    var longi: String = ""
    var location: String = ""
    var Km: String = ""

    //map
    var mapViewStatus: Boolean = false
    private lateinit var mapFragment: SupportMapFragment
    private var isMapReady = false
    private lateinit var googleMap: GoogleMap


    //call
    companion object {
        private const val REQUEST_CALL_PERMISSION = 1
    }
    var mobile: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        category_id= intent.getStringExtra("category_id").toString()
        category_Name= intent.getStringExtra("category_Name").toString()
        sub_id= intent.getStringExtra("sub_id").toString()

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = category_Name
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }


        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            PostBannersApi()
        }


        val lati = Preferences.loadStringValue(this@CategoriesBasedItemsListActivity, Preferences.lat, "")
        val longii = Preferences.loadStringValue(this@CategoriesBasedItemsListActivity, Preferences.longi, "")
        val locationi = Preferences.loadStringValue(this@CategoriesBasedItemsListActivity, Preferences.location, "")
        val klm = Preferences.loadStringValue(this@CategoriesBasedItemsListActivity, Preferences.km, "")
        if(lati.equals("") || longii.equals("") || klm.equals("")){
            getProfileApi()
        }else{
            binding.txtLocation.text = locationi
            lat = lati.toString()
            longi = longii.toString()
            Km = klm.toString()
            subcategoriesApi("noraml")
        }

        binding.imgLocationChange.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgLocationChange.startAnimation(animations)

            bottomPopup()
        }

        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        binding.switchMap.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mapViewStatus = true
                if (categoriesList.isNotEmpty()) {
                    binding.recyclerview.visibility = View.GONE
                    binding.linearMap.visibility = View.VISIBLE
                }
            } else {
                mapViewStatus = false
                if (categoriesList.isNotEmpty()) {
                    binding.linearMap.visibility = View.GONE
                    binding.recyclerview.visibility = View.VISIBLE
                }
            }
        }


    }

    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(this@CategoriesBasedItemsListActivity, Preferences.userId, "")
        Log.e("userId_",userId.toString())

        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getProfileApi(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        binding.txtLocation.text = rsp.data?.location.toString()
                        lat = rsp.data?.latitude.toString()
                        longi = rsp.data?.longitude.toString()
                        Km = rsp.data?.longitude.toString()
                        subcategoriesApi("noraml")
                    }
                } else {
                    ViewController.showToast(this@CategoriesBasedItemsListActivity, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                ViewController.showToast(this@CategoriesBasedItemsListActivity, t.message.toString())
            }
        })
    }

    //banners
    private fun PostBannersApi() {
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.PostBannersApi().enqueue(object : retrofit2.Callback<List<PostBannersModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<PostBannersModel>>,
                response: retrofit2.Response<List<PostBannersModel>>
            ) {
                if (response.isSuccessful) {
                    val banners = response.body() ?: emptyList()
                    BannerDataSet(banners)
                } else {
                    ViewController.showToast(this@CategoriesBasedItemsListActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<PostBannersModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.showToast(this@CategoriesBasedItemsListActivity, "Try again: ${t.message}")
            }
        })
    }
    private fun BannerDataSet(banners: List<PostBannersModel>) {
        val imageList = mutableListOf<SlideModel>()
        banners.forEach {
            val imageUrl = it.image
            if (imageUrl.isNotEmpty()) {
                imageList.add(SlideModel(imageUrl))
            } else {
                imageList.add(
                    SlideModel(
                        R.drawable.home_bannes
                    )
                )
            }
        }
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
    }

    private fun subcategoriesApi(viewType: String) {
        binding.linearNoData.visibility = View.GONE
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.subcategoriesApi(category_id).enqueue(object : retrofit2.Callback<List<SubCategoriesModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<SubCategoriesModel>>,
                response: retrofit2.Response<List<SubCategoriesModel>>
            ) {
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val categories = response.body()
                        if (categories != null) {
                            if (categories.size != 0) {
                                subcategoriesdataList(categories)
                            } else {
                                binding.linearNoData.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        binding.linearNoData.visibility = View.VISIBLE
                    }
                } else {
                    ViewController.showToast(this@CategoriesBasedItemsListActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<SubCategoriesModel>>, t: Throwable) {
                Log.e("citys_error", t.message.toString())
                binding.linearNoData.visibility = View.VISIBLE

            }
        })

    }

    private fun subcategoriesdataList(categoriesl: List<SubCategoriesModel>) {
        val layoutManager = LinearLayoutManager(this@CategoriesBasedItemsListActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.Crecyclerview.layoutManager = layoutManager

        val adapter = SubCategoriesAdapter(categoriesl, sub_id) { item ->
            sub_id = item.id
            categoriesBasedItemsApi(item.id)
            scrollToCenter(binding.Crecyclerview, categoriesl.indexOfFirst { it.id == item.id })
        }

        binding.Crecyclerview.adapter = adapter

        // ✅ Auto-select and call API for sub_id or first item
        val selectedIndex = categoriesl.indexOfFirst { it.id == sub_id }
            .takeIf { it != -1 } ?: 0

        sub_id = categoriesl[selectedIndex].id
        categoriesBasedItemsApi(sub_id)

        // ✅ Scroll to center
        binding.Crecyclerview.post {
            scrollToCenter(binding.Crecyclerview, selectedIndex)
        }
    }
    private fun scrollToCenter(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        val view = layoutManager.findViewByPosition(position)

        if (view != null) {
            val parentCenter = recyclerView.width / 2
            val childCenter = view.left + view.width / 2
            val scrollNeeded = childCenter - parentCenter
            recyclerView.smoothScrollBy(scrollNeeded, 0)
        } else {

            // View is not laid out yet, scroll with offset
            recyclerView.scrollToPosition(position)
            recyclerView.post {
                val v = layoutManager.findViewByPosition(position)
                v?.let {
                    val parentCenter = recyclerView.width / 2
                    val childCenter = it.left + it.width / 2
                    val scrollNeeded = childCenter - parentCenter
                    recyclerView.smoothScrollBy(scrollNeeded, 0)
                }
            }

        }
    }



    private fun categoriesBasedItemsApi(subId: String) {
        binding.linearNoData.visibility = View.GONE

        val apiInterface = RetrofitClient.apiInterface
        apiInterface.categoriesBasedItemsApi(category_id, subId, lat, longi, Km).enqueue(object : retrofit2.Callback<List<SubCategoriesItemsModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<SubCategoriesItemsModel>>,
                response: retrofit2.Response<List<SubCategoriesItemsModel>>
            ) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val categories = response.body()
                        if (categories != null) {
                            // Initialize and clear the list
                            categoriesList.clear()
                            categoriesList.addAll(categories)
                            if (categoriesList.size > 0) {
                                categoriesDataSet(categoriesList)
                                if (mapViewStatus){
                                    binding.linearMap.visibility = View.VISIBLE
                                }else{
                                    binding.recyclerview.visibility = View.VISIBLE
                                }
                            } else {
                                binding.recyclerview.visibility = View.GONE
                                binding.linearMap.visibility = View.GONE
                                binding.linearNoData.visibility = View.VISIBLE
                            }
                        } else {
                            binding.recyclerview.visibility = View.GONE
                            binding.linearMap.visibility = View.GONE
                            binding.linearNoData.visibility = View.VISIBLE
                        }
                    } else {
                        binding.recyclerview.visibility = View.GONE
                        binding.linearMap.visibility = View.GONE
                        binding.linearNoData.visibility = View.VISIBLE
                    }
                } else {
                    ViewController.showToast(this@CategoriesBasedItemsListActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<SubCategoriesItemsModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                binding.recyclerview.visibility = View.GONE
                binding.linearMap.visibility = View.GONE
                binding.linearNoData.visibility = View.VISIBLE
            }
        })

    }

    private fun categoriesDataSet(categories: List<SubCategoriesItemsModel>) {
        // Initialize RecyclerView
        binding.recyclerview.layoutManager = LinearLayoutManager(this@CategoriesBasedItemsListActivity)
        catAdapter = SubCategoriesItemsAdapter(categories) { item, type ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            if (type.equals("call")){
                mobile = item.mobile
                checkAndRequestPermission()
            }else{
                startActivity(Intent(this@CategoriesBasedItemsListActivity, PostCategoriesDetailsActivity::class.java).apply {
                    putExtra("category_id",category_id)
                    putExtra("sub_id",sub_id)
                    putExtra("post_id",item.id)
                    putExtra("post_Name",item.title)
                })
            }
        }
        binding.recyclerview.adapter = catAdapter

        // map
        googleMap.clear()
        val boundsBuilder = LatLngBounds.Builder()
        var hasValidLocation = false

        val customIcon = getResizedBitmapDescriptor(R.drawable.marker_new_ic, 80, 80)

        var firstValidMarker: Marker? = null

        for (item in categories) {
            val lat = item.latitude?.toDoubleOrNull()
            val lng = item.longitude?.toDoubleOrNull()

            if (lat != null && lng != null) {
                val position = LatLng(lat, lng)

                val markerOptions = MarkerOptions()
                    .position(position)
                    .title(item.title)         // 👈 Name (bold in popup)
                    .snippet(item.address)     // 👈 Address (below name)
                    .icon(customIcon)

                val marker = googleMap.addMarker(markerOptions)

                if (firstValidMarker == null) {
                    firstValidMarker = marker
                }

                boundsBuilder.include(position)
                hasValidLocation = true
            }
        }

        // Fit camera to all markers
        if (hasValidLocation) {
            val bounds = boundsBuilder.build()
            val padding = 150

            mapFragment.view?.post {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))

                // Enforce minimum zoom
                val minZoomLevel = 12f
                if (googleMap.cameraPosition.zoom < minZoomLevel) {
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(minZoomLevel))
                }

                // 👇 Show popup on first marker
                firstValidMarker?.showInfoWindow()

            }
        }

        // Set custom info window adapter
        googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? = null

            override fun getInfoContents(marker: Marker): View {
                val view = layoutInflater.inflate(R.layout.custom_marker_popup, null)
                val tvName = view.findViewById<TextView>(R.id.tvName)
                val tvAddress = view.findViewById<TextView>(R.id.tvAddress)

                tvName.text = marker.title ?: "No Name"
                tvAddress.text = marker.snippet ?: "No Address"

                return view
            }
        })


    }

    //map
    private fun getResizedBitmapDescriptor(
        @DrawableRes resId: Int,
        width: Int,
        height: Int
    ): BitmapDescriptor {
        val bitmap = BitmapFactory.decodeResource(resources, resId)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        isMapReady = true

        // If data already loaded, draw markers now
        if (categoriesList.isNotEmpty()) {
            categoriesDataSet(categoriesList)
        }

    }

    //search
    private fun filter(text: String) {
        val filteredList = categoriesList.filter { item ->
            item.services.contains(text, ignoreCase = true)
        }

        if (filteredList.isEmpty()) {
            binding.linearNoData.visibility = View.VISIBLE
        } else {
            binding.linearNoData.visibility = View.GONE
        }

        // Update list only if catAdapter is initialized
        if (::catAdapter.isInitialized) {
            catAdapter.updateList(filteredList)
        }
    }

    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
        } else {
            // Permission already granted, you can make the call
            makePhoneCall()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else {
                // Permission denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makePhoneCall() {
        val phoneNumber = mobile
        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        startActivity(callIntent)
    }

    private fun bottomPopup() {
        val bottomSheet = LocationBottomSheetFragment()

        // Set listener to get value from the bottom sheet
        bottomSheet.setOnItemClickListener(object : LocationBottomSheetFragment.OnItemClickListener {
            override fun onItemSelected(lat_value: String, longi_value: String, locationsss: String, Klm: String) {

                //Preferences
                Preferences.saveStringValue(applicationContext, Preferences.lat,
                    lat_value
                )
                Preferences.saveStringValue(applicationContext, Preferences.longi,
                    longi_value
                )
                Preferences.saveStringValue(applicationContext, Preferences.km,
                    Klm
                )
                Preferences.saveStringValue(applicationContext, Preferences.location,
                    locationsss
                )

                lat = lat_value
                longi = longi_value
                location = locationsss
                Km = Klm
                binding.txtLocation.text = location

                subcategoriesApi("noraml")
            }
        })

        bottomSheet.show(supportFragmentManager, "MyBottomSheetFragment")
    }

}