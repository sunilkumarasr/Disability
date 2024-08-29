package com.royalit.disability.Activitys

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.databinding.ActivityAddProductBinding

class AddProductActivity : AppCompatActivity() {

    val binding: ActivityAddProductBinding by lazy {
        ActivityAddProductBinding.inflate(layoutInflater)
    }

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Add Product"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }


        binding.cardChoose.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        binding.cardSubmit.setOnClickListener {
            addProduct()
        }
    }

    private fun addProduct() {
        val productName=binding.productNameEdit.text?.trim().toString()
        val actualPrice=binding.ActualEdit.text?.trim().toString()
        val offerPrice=binding.offerPriceEdit.text?.trim().toString()
        val address=binding.addressEdit.text?.trim().toString()
        val features=binding.featuresEdit.text?.trim().toString()
        val description=binding.descriptionEdit.text?.trim().toString()

        if(productName.isEmpty()){
            ViewController.showToast(applicationContext, "Enter product name")
            return
        }
        if(actualPrice.isEmpty()){
            ViewController.showToast(applicationContext, "Enter actual price")
            return
        }
        if(offerPrice.isEmpty()){
            ViewController.showToast(applicationContext, "Enter offer price")
            return
        }
        if(address.isEmpty()){
            ViewController.showToast(applicationContext, "Enter address")
            return
        }
        if(features.isEmpty()){
            ViewController.showToast(applicationContext, "Enter features")
            return
        }
        if(description.isEmpty()){
            ViewController.showToast(applicationContext, "Enter description")
            return
        }

        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
            return
        }else{
            ViewController.showLoading(this@AddProductActivity)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
//            imageView.setImageURI(imageUri)
            val imageName = getImageName(this@AddProductActivity, imageUri)
            binding.txtFileName.text = imageName
        }
    }


    private fun getRealPathFromURI(contentUri: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return filePath ?: ""
    }

    fun getImageName(context: Context, imageUri: Uri?): String? {
        if (imageUri == null) return null

        return when (imageUri.scheme) {
            "file" -> getRealPathFromURI(imageUri)
            else -> null
        }
    }

}