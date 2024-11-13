package com.smy3infotech.divyaangdisha.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.models.SlideModel
import com.smy3infotech.divyaangdisha.Activitys.Categorys.CategoriesBasedItemsListActivity
import com.smy3infotech.divyaangdisha.Activitys.JobAlerts.JobAlertDetailsActivity
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Categorys.CategoriesModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Home.HomeCategoriesAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Home.HomeBannersModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.JobAlerts.JobAlertHomeAdapter
import com.smy3infotech.divyaangdisha.AdaptersAndModels.JobAlerts.JobAlertModel
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdisha.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {
        if (!ViewController.noInterNetConnectivity(requireActivity())) {
            ViewController.showToast(requireActivity(), "Please check your connection ")
            return
        } else {
            HomebannersApi()
            categoriesApi()
            jobAlertApi()
        }
    }


    private fun HomebannersApi() {
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.HomebannersApi().enqueue(object : retrofit2.Callback<List<HomeBannersModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<HomeBannersModel>>,
                response: retrofit2.Response<List<HomeBannersModel>>
            ) {
                if (response.isSuccessful) {
                    val banners = response.body() ?: emptyList()
                    BannerDataSet(banners)
                } else {
                    ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<HomeBannersModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })
    }
    private fun BannerDataSet(banners: List<HomeBannersModel>) {
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


    private fun categoriesApi() {
            ViewController.showLoading(requireActivity())
            val apiInterface = RetrofitClient.apiInterface
            apiInterface.categoriesApi()
                .enqueue(object : retrofit2.Callback<List<CategoriesModel>> {
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
                                    DataSet(categories)
                                }
                            } else {

                            }
                        } else {
                            ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<List<CategoriesModel>>,
                        t: Throwable
                    ) {
                        Log.e("cat_error", t.message.toString())
                        ViewController.hideLoading()
                        ViewController.showToast(requireActivity(), "Try again: ${t.message}")
                    }
                })

    }
    private fun DataSet(categories: List<CategoriesModel>) {

        val layoutManager = GridLayoutManager(activity, 3)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = HomeCategoriesAdapter(categories) { item ->
            // Handle item click
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity, CategoriesBasedItemsListActivity::class.java).apply {
                putExtra("category_id",item.category_id)
                putExtra("category_Name",item.category)
            })
        }
    }


    private fun jobAlertApi() {
            val apiInterface = RetrofitClient.apiInterface
            apiInterface.jobAlertApi().enqueue(object : retrofit2.Callback<List<JobAlertModel>> {
                override fun onResponse(
                    call: retrofit2.Call<List<JobAlertModel>>,
                    response: retrofit2.Response<List<JobAlertModel>>
                ) {
                    if (response.isSuccessful) {
                        val rsp = response.body()
                        if (rsp != null) {
                            val joblist = response.body()
                            if (joblist != null) {
                                JobDataSet(joblist)
                            }
                        }
                    } else {
                        ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<JobAlertModel>>, t: Throwable) {
                    Log.e("cat_error", t.message.toString())
                    ViewController.showToast(requireActivity(), "Try again: ${t.message}")
                }
            })
    }
    private fun JobDataSet(joblist: List<JobAlertModel>) {
        binding.recyclerviewjobAlert.layoutManager = LinearLayoutManager(activity)
        binding.recyclerviewjobAlert.adapter = JobAlertHomeAdapter(joblist) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity, JobAlertDetailsActivity::class.java).apply {
                putExtra("title",item.title)
                putExtra("description",item.description)
                putExtra("post_date",item.post_date)
                putExtra("last_date",item.last_date)
            })
        }
    }

}