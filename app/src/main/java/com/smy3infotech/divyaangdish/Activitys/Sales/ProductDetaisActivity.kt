package com.smy3infotech.divyaangdish.Activitys.Sales

import android.os.Bundle
import android.app.Dialog
import android.content.Intent
import android.view.Window
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.denzcoskun.imageslider.models.SlideModel
import com.smy3infotech.divyaangdish.Activitys.DashBoardActivity
import com.smy3infotech.divyaangdish.AdaptersAndModels.EnqueryResponse
import com.smy3infotech.divyaangdish.AdaptersAndModels.EnquerySaleRequest
import com.smy3infotech.divyaangdish.AdaptersAndModels.ProductItemDetailsModel
import com.smy3infotech.divyaangdish.Config.Preferences
import com.smy3infotech.divyaangdish.Config.ViewController
import com.smy3infotech.divyaangdish.R
import com.smy3infotech.divyaangdish.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdish.databinding.ActivityProductDetaisBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetaisActivity : AppCompatActivity() {

    val binding: ActivityProductDetaisBinding by lazy {
        ActivityProductDetaisBinding.inflate(layoutInflater)
    }

    lateinit var product_id:String
    lateinit var product_Name:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        product_id= intent.getStringExtra("product_id").toString()
        product_Name= intent.getStringExtra("product_Name").toString()


        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = product_Name
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            productDetailsApi()
        }

        binding.cardEnquiry.setOnClickListener {
            enqueryDailouge()
        }
    }


    private fun productDetailsApi() {
        Log.e("product_id_",product_id)
        ViewController.showLoading(this@ProductDetaisActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.productDetailsApi(product_id).enqueue(object :
            Callback<ProductItemDetailsModel> {
            override fun onResponse(call: Call<ProductItemDetailsModel>, response: Response<ProductItemDetailsModel>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        if (rsp.status.equals("success")) {
                            productDataSet(rsp)
                        }
                    }
                } else {
                    ViewController.showToast(this@ProductDetaisActivity, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ProductItemDetailsModel>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(applicationContext, "Try again: ${t.message}")
            }
        })
    }

    private fun productDataSet(productDetails: ProductItemDetailsModel) {
        binding.txtTitle.text = productDetails.data?.product?.product ?: ""
        binding.txtBrand.text = productDetails.data?.product?.brand ?: ""
        binding.txtColor.text = productDetails.data?.product?.color ?: ""
        binding.txtPhone.text = "Phone Number: "+ productDetails.data?.product?.phone
        binding.txtOfferPrice.text = "₹ "+productDetails.data?.product?.offer_price
        binding.txtDec.text = productDetails.data?.product?.description ?: ""
        binding.txtAddress.text = productDetails.data?.product?.address ?: ""

        val spannableString = SpannableString("₹ " + productDetails.data?.product?.actual_price)
        spannableString.setSpan(StrikethroughSpan(), 0, spannableString.length, 0)
        binding.txtActulePrice.text = spannableString

        //images list
        if (productDetails.data?.images?.size!=0) {
            val imageList = mutableListOf<SlideModel>()
            productDetails.data?.images?.forEach {
                val imageUrl = RetrofitClient.Image_Path+it.additionalImage
                if (imageUrl != null) {
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
            }
            binding.imageSlider.setImageList(imageList)
        }
    }


    //enquery
    private fun enqueryDailouge() {

        val dialog = Dialog(this@ProductDetaisActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.sale_enquery_custom_dialog)

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
            val phone_ = phoneEdit.text.toString()
            val eamil_ = emailEdit.text.toString()
            val message_ = messageEdit.text.toString()

            if (name_.equals("")) {
                ViewController.showToast(applicationContext, "Enter name")
            } else if (phone_.equals("")) {
                ViewController.showToast(applicationContext, "Enter Phone Number")
            } else if (eamil_.equals("")) {
                ViewController.showToast(applicationContext, "Enter Email Address")
            } else if (message_.equals("")) {
                ViewController.showToast(applicationContext, "Enter message")
            } else if (!validateMobileNumber(phone_)) {
                ViewController.showToast(applicationContext, "Enter Valid mobile number")
            } else if (!validateEmail(eamil_)) {
                ViewController.showToast(applicationContext, "Enter Valid email")
            } else {
                dialog.dismiss()
                enquerySaleApi(name_, phone_, eamil_, message_)
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

    private fun enquerySaleApi(name_: String, phone_: String, email_: String, message_: String) {
        val userId = Preferences.loadStringValue(this@ProductDetaisActivity, Preferences.userId, "")

        ViewController.showLoading(this@ProductDetaisActivity)

        Log.e("product", product_id)

        val apiInterface = RetrofitClient.apiInterface
        val enqueryRequest = EnquerySaleRequest(name_, phone_, email_, message_, product_id, userId.toString())

        apiInterface.enquerySaleApi(enqueryRequest).enqueue(object : Callback<EnqueryResponse> {
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
                            Intent(this@ProductDetaisActivity, DashBoardActivity::class.java)
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