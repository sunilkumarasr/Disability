package com.smy3infotech.divyaangdisha.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.models.SlideModel
import com.smy3infotech.divyaangdisha.Activitys.Sales.AddProductActivity
import com.smy3infotech.divyaangdisha.Activitys.Sales.ProductDetaisActivity
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SalesBannersModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SalesHome.ProductData
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SalesHome.SaleAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SalesHome.SaleModel
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdisha.databinding.FragmentSaleBinding


class SaleFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentSaleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaleBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.cardAdd.setOnClickListener(this)

        SalesbannersApi()
        saleApi()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.cardAdd -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
            }

        }
    }

    private fun SalesbannersApi() {
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.SalesbannersApi().enqueue(object : retrofit2.Callback<List<SalesBannersModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<SalesBannersModel>>,
                response: retrofit2.Response<List<SalesBannersModel>>
            ) {
                if (response.isSuccessful) {
                    val banners = response.body() ?: emptyList()
                    BannerDataSet(banners)
                } else {
                    ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<SalesBannersModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })
    }
    private fun BannerDataSet(banners: List<SalesBannersModel>) {
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


    private fun saleApi() {
        ViewController.showLoading(requireActivity())

        val apiInterface = RetrofitClient.apiInterface
        apiInterface.saleApi().enqueue(object : retrofit2.Callback<SaleModel> {
            override fun onResponse(
                call: retrofit2.Call<SaleModel>,
                response: retrofit2.Response<SaleModel>
            ) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        DataSet(rsp.data) // Pass the list of ProductData
                    } else {
                        ViewController.showToast(requireActivity(), "Empty Response")
                    }
                } else {
                    ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<SaleModel>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.hideLoading()
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })
    }
    private fun DataSet(sale: List<ProductData>) {
        val layoutManager = GridLayoutManager(activity, 2) // 3 columns in the grid
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = SaleAdapter(sale) { item ->
            // Handle item click
            startActivity(Intent(activity, ProductDetaisActivity::class.java).apply {
                putExtra("product_id",item.product.id)
                putExtra("product_Name",item.product.product)
            })
        }
    }


}