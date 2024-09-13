package com.royalit.disability.Activitys.Categorys

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.royalit.disability.Activitys.DashBoardActivity
import com.royalit.disability.AdaptersAndModels.Categorys.ItemsImagesListAdapter
import com.royalit.disability.AdaptersAndModels.EnqueryRequest
import com.royalit.disability.AdaptersAndModels.EnqueryResponse
import com.royalit.disability.AdaptersAndModels.LoginRequest
import com.royalit.disability.AdaptersAndModels.LoginResponse
import com.royalit.disability.AdaptersAndModels.PostItemDetailsModel
import com.royalit.disability.Config.Preferences
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.Retrofit.RetrofitClient
import com.royalit.disability.databinding.ActivityPostViewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostCategoriesDetailsActivity : AppCompatActivity() {

    val binding: ActivityPostViewBinding by lazy {
        ActivityPostViewBinding.inflate(layoutInflater)
    }

//    AIzaSyDvW-fjH0kspIU4CFSFKwNPNQpUnN4K-QQ

    lateinit var category_id: String
    lateinit var post_id: String
    lateinit var post_Name: String

    //call
    companion object {
        private const val REQUEST_CALL_PERMISSION = 1
    }

    var mobileNUmber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        category_id = intent.getStringExtra("category_id").toString()
        post_id = intent.getStringExtra("post_id").toString()
        post_Name = intent.getStringExtra("post_Name").toString()


        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = post_Name
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        categoriesItemsDetailsApi()

        binding.cardCall.setOnClickListener {
            checkAndRequestPermission()
        }

        binding.cardEnquiry.setOnClickListener {
            enqueryDailouge()
        }
        binding.cardEnquiry1.setOnClickListener {
            enqueryDailouge()
        }


    }

    private fun categoriesItemsDetailsApi() {
        ViewController.showLoading(this@PostCategoriesDetailsActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.categoriesItemsDetailsApi(post_id)
            .enqueue(object : Callback<PostItemDetailsModel> {
                override fun onResponse(
                    call: Call<PostItemDetailsModel>,
                    response: Response<PostItemDetailsModel>
                ) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val rsp = response.body()
                        if (rsp != null) {
                            if (rsp.status.equals("success")) {
                                postDataSet(rsp)
                            } else {
                                binding.scrollView.visibility = View.GONE
                                binding.txtNoData.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        binding.scrollView.visibility = View.GONE
                        binding.txtNoData.visibility = View.VISIBLE
                        ViewController.showToast(
                            this@PostCategoriesDetailsActivity,
                            "Error: ${response.code()}"
                        )
                    }
                }

                override fun onFailure(call: Call<PostItemDetailsModel>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.showToast(applicationContext, "Try again: ${t.message}")
                    binding.scrollView.visibility = View.GONE
                    binding.txtNoData.visibility = View.VISIBLE
                }
            })
    }

    private fun postDataSet(postDetails: PostItemDetailsModel) {
        Glide.with(binding.imgBanner)
            .load(RetrofitClient.Image_Path + postDetails.data?.product?.image)
            .placeholder(R.drawable.home_bannes).error(R.drawable.home_bannes)
            .into(binding.imgBanner)
        binding.txtName.text = postDetails.data?.product?.title ?: ""
        binding.txtlocation.text = postDetails.data?.product?.location ?: ""
        binding.txtmobile.text = postDetails.data?.product?.mobile ?: ""
        binding.txtemail.text = postDetails.data?.product?.mail ?: ""
        binding.txtAbout.text =
            Html.fromHtml(postDetails.data?.product?.about ?: "", Html.FROM_HTML_MODE_LEGACY)
        binding.txtService.text =
            Html.fromHtml(postDetails.data?.product?.services ?: "", Html.FROM_HTML_MODE_LEGACY)

        mobileNUmber = postDetails.data?.product?.mobile ?: ""

        if (postDetails.data?.product?.certified.equals("1")) {
            binding.imgCertified.visibility = View.VISIBLE
        }
        if (postDetails.data?.product?.verified.equals("1")) {
            binding.imgVertified.visibility = View.VISIBLE
        }

        if (postDetails.data?.images?.size != 0) {
            val layoutManager = GridLayoutManager(this@PostCategoriesDetailsActivity, 3)
            binding.recyclerviewImages.layoutManager = layoutManager
            binding.recyclerviewImages.adapter = postDetails.data?.images?.let {
                ItemsImagesListAdapter(it) { item ->

                }
            }
        } else {
            binding.recyclerviewImages.visibility = View.GONE
            binding.txtNoImages.visibility = View.VISIBLE
        }


//        binding.webview.settings.setJavaScriptEnabled(true)
//
//        binding.webview.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                if (url != null) {
//                    view?.loadUrl(url)
//                }
//                return true
//            }
//        }
//        binding.webview.loadUrl(postDetails.data?.location_url.toString())

        binding.webviewLocation.getSettings().setAllowFileAccess(true)
        binding.webviewLocation.getSettings().setPluginState(WebSettings.PluginState.ON)
        binding.webviewLocation.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND)
        binding.webviewLocation.setWebViewClient(WebViewClient())
        binding.webviewLocation.getSettings().setJavaScriptEnabled(true)
        binding.webviewLocation.getSettings().setLoadWithOverviewMode(true)
        binding.webviewLocation.getSettings().setUseWideViewPort(true)
        val displaymetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displaymetrics)
        val height = displaymetrics.heightPixels
        val width = displaymetrics.widthPixels
        binding.webviewLocation.loadData("<iframe height=600 width= $width src=\"${postDetails.data?.location_url.toString()}\"></iframe>", "text/html",
            "utf-8" )

    }

    //call
    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION
            )
        } else {
            // Permission already granted, you can make the call
            makePhoneCall()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
        val phoneNumber = mobileNUmber
        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        startActivity(callIntent)
    }

    //enquery

    private fun enqueryDailouge() {

        val dialog = Dialog(this@PostCategoriesDetailsActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.enquery_custom_dialog)

        val window = dialog.window
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, // Width
            ViewGroup.LayoutParams.WRAP_CONTENT  // Height
        )

        val imgClose = dialog.findViewById<ImageView>(R.id.imgClose)
        val nameEdit = dialog.findViewById<EditText>(R.id.nameEdit)
        val phoneEdit = dialog.findViewById<EditText>(R.id.phoneEdit)
        val emailEdit = dialog.findViewById<EditText>(R.id.emailEdit)
        val messageEdit = dialog.findViewById<EditText>(R.id.messageEdit)
        val cardLogin = dialog.findViewById<CardView>(R.id.cardLogin)

        cardLogin.setOnClickListener {

            val name_ = nameEdit.text.toString()
            val phone_ = phoneEdit.text.toString().trim()
            val email_ = emailEdit.text.toString().trim()
            val message_ = messageEdit.text.toString()

            if (name_.equals("")) {
                ViewController.showToast(applicationContext, "Enter name")
            } else if (phone_.equals("")) {
                ViewController.showToast(applicationContext, "Enter Phone Number")
            } else if (email_.equals("")) {
                ViewController.showToast(applicationContext, "Enter Email Address")
            } else if (message_.equals("")) {
                ViewController.showToast(applicationContext, "Enter message")
            } else if (!validateMobileNumber(phone_)) {
                ViewController.showToast(applicationContext, "Enter Valid mobile number")
            } else if (!validateEmail(email_)) {
                ViewController.showToast(applicationContext, "Enter Valid email")
            } else {
                dialog.dismiss()
                enqueryApi(name_, phone_, email_, message_)
            }

        }

        imgClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun validateMobileNumber(mobile: String): Boolean {
        val mobilePattern = "^[6-9][0-9]{9}$"
        return mobile.matches(Regex(mobilePattern))
    }

    private fun validateEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}"
        return email.matches(Regex(emailPattern))
    }


    private fun enqueryApi(name_: String, phone_: String, email_: String, message_: String) {
        ViewController.showLoading(this@PostCategoriesDetailsActivity)

        Log.e("post_id_", post_id)
        Log.e("category_id_", category_id)

        val apiInterface = RetrofitClient.apiInterface
        val enqueryRequest = EnqueryRequest(name_, phone_, email_, message_, post_id, category_id)

        apiInterface.enqueryApi(enqueryRequest).enqueue(object : Callback<EnqueryResponse> {
            override fun onResponse(
                call: Call<EnqueryResponse>,
                response: Response<EnqueryResponse>
            ) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.status == "success") {
                        ViewController.showToast(applicationContext, loginResponse.message ?: "Success")
                        startActivity(
                            Intent(this@PostCategoriesDetailsActivity, DashBoardActivity::class.java)
                        )
                    } else {
                        ViewController.showToast(applicationContext, "Error: ${loginResponse?.message ?: "Unknown Error"}")
                    }
                } else {
                    ViewController.showToast(applicationContext, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<EnqueryResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(applicationContext, "Try again: ${t.message}")
            }
        })
    }

}