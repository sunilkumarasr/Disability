package com.royalit.disability.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.disability.Activitys.AddPostActivity
import com.royalit.disability.Activitys.AddProductActivity
import com.royalit.disability.Activitys.CategoriesBasedItemsListActivity
import com.royalit.disability.AdaptersAndModels.CategoriesHomeAdapter
import com.royalit.disability.AdaptersAndModels.CategoriesModel
import com.royalit.disability.AdaptersAndModels.Home.HomeCategoriesAdapter
import com.royalit.disability.AdaptersAndModels.SaleAdapter
import com.royalit.disability.AdaptersAndModels.SaleModel
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.Retrofit.RetrofitClient
import com.royalit.disability.databinding.FragmentCategoriesBinding
import com.royalit.disability.databinding.FragmentSaleBinding


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

        dataList()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.cardAdd -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
            }

        }
    }

    private fun dataList() {
        ViewController.showLoading(requireActivity())

        val apiInterface = RetrofitClient.apiInterface
        apiInterface.saleApi().enqueue(object : retrofit2.Callback<List<SaleModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<SaleModel>>,
                response: retrofit2.Response<List<SaleModel>>
            ) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val sale = response.body()
                        if (sale != null) {
                            DataSet(sale)
                        }
                    } else {

                    }
                } else {
                    ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<SaleModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.hideLoading()
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })

    }

    private fun DataSet(sale: List<SaleModel>) {
        val layoutManager = GridLayoutManager(activity, 2) // 3 columns in the grid
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = SaleAdapter(sale) { item ->
            // Handle item click
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity, CategoriesBasedItemsListActivity::class.java))
        }
    }

}