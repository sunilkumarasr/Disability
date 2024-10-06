package com.smy3infotech.divyaangdisha.Activitys.Categorys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.models.SlideModel
import com.smy3infotech.divyaangdisha.Activitys.DashBoardActivity
import com.smy3infotech.divyaangdisha.Activitys.MapLocationActivity
import com.smy3infotech.divyaangdisha.AdaptersAndModels.PostBannersModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SubCategories.SubCategoriesAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SubCategories.SubCategoriesModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SubCategoriesItems.SubCategoriesItemsAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SubCategoriesItems.SubCategoriesItemsModel
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.LocationBottomSheetFragment
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdisha.databinding.ActivityCategoriesBasedItemsListBinding

class CategoriesBasedItemsListActivity : AppCompatActivity() {

    val binding: ActivityCategoriesBasedItemsListBinding by lazy {
        ActivityCategoriesBasedItemsListBinding.inflate(layoutInflater)
    }

    lateinit var category_id:String
    lateinit var category_Name:String

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


        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = category_Name
        val imgMapFilter = binding.root.findViewById<View>(R.id.imgMapFilter)
        imgMapFilter.visibility = View.VISIBLE
        imgMapFilter.setOnClickListener {
            startActivity(Intent(this@CategoriesBasedItemsListActivity, MapLocationActivity::class.java))
        }

        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        PostBannersApi()
        dataList()
        categoriesBasedItemsApi()


        binding.cardBottom.setOnClickListener {
            bottomPopup()
        }

    }

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
        binding.imageSlider.setImageList(imageList)
    }

    private fun dataList() {
        // Create a static list of data
        val dataList = listOf(
            SubCategoriesModel("All"),
            SubCategoriesModel("Vision Correction"),
            SubCategoriesModel("Contact Lens"),
            SubCategoriesModel("Hearing Clinics"),
            SubCategoriesModel("Sale")
        )

        // Initialize RecyclerView
        binding.Crecyclerview.layoutManager = LinearLayoutManager(this@CategoriesBasedItemsListActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.Crecyclerview.adapter = SubCategoriesAdapter(dataList) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun categoriesBasedItemsApi() {
        ViewController.showLoading(this@CategoriesBasedItemsListActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.categoriesBasedItemsApi(category_id).enqueue(object : retrofit2.Callback<List<SubCategoriesItemsModel>> {
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
                            categoriesDataSet(categories)
                        }
                    } else {
                        binding.txtNoData.visibility = View.VISIBLE
                    }
                } else {
                    ViewController.showToast(this@CategoriesBasedItemsListActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<SubCategoriesItemsModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.hideLoading()
                binding.txtNoData.visibility = View.VISIBLE
            }
        })

    }
    private fun categoriesDataSet(categories: List<SubCategoriesItemsModel>) {
        // Initialize RecyclerView
        binding.recyclerview.layoutManager = LinearLayoutManager(this@CategoriesBasedItemsListActivity)
        binding.recyclerview.adapter = SubCategoriesItemsAdapter(categories) { item, type ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            if (type.equals("call")){
                mobile = item.mobile
                checkAndRequestPermission()
            }else{
                startActivity(Intent(this@CategoriesBasedItemsListActivity, PostCategoriesDetailsActivity::class.java).apply {
                    putExtra("category_id",category_id)
                    putExtra("post_id",item.id)
                    putExtra("post_Name",item.title)
                })
            }
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
            override fun onItemSelected(lat_value: String, longi_value: String) {
                // Handle the value received from the bottom sheet
                Toast.makeText(this@CategoriesBasedItemsListActivity, "Selected: $lat_value - $longi_value ", Toast.LENGTH_SHORT).show()
            }
        })

        bottomSheet.show(supportFragmentManager, "MyBottomSheetFragment")
    }

}