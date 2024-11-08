package com.smy3infotech.divyaangdish.Fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.smy3infotech.divyaangdish.Activitys.AboutUsActivity
import com.smy3infotech.divyaangdish.Activitys.AskQuestionsActivity
import com.smy3infotech.divyaangdish.Activitys.ContactUsActivity
import com.smy3infotech.divyaangdish.Activitys.EditProfileActivity
import com.smy3infotech.divyaangdish.Activitys.FaqActivity
import com.smy3infotech.divyaangdish.Activitys.HelpAndSupportActivity
import com.smy3infotech.divyaangdish.Activitys.JobAlerts.JobAlertsActivity
import com.smy3infotech.divyaangdish.Activitys.Categorys.MyPostsActivity
import com.smy3infotech.divyaangdish.Activitys.PrivacyPolicyActivity
import com.smy3infotech.divyaangdish.Activitys.Sales.MyProductsActivity
import com.smy3infotech.divyaangdish.Activitys.TermsAndConditionsActivity
import com.smy3infotech.divyaangdish.Activitys.UsefulLinksActivity
import com.smy3infotech.divyaangdish.AdaptersAndModels.ProfileResponse
import com.smy3infotech.divyaangdish.Config.Preferences
import com.smy3infotech.divyaangdish.Config.ViewController
import com.smy3infotech.divyaangdish.Logins.LoginActivity
import com.smy3infotech.divyaangdish.R
import com.smy3infotech.divyaangdish.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdish.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(), View.OnClickListener  {

    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

        getProfileApi()

        binding.imgEdit.setOnClickListener(this)
        binding.linearJobalerts.setOnClickListener(this)
        binding.linearAboutUs.setOnClickListener(this)
        binding.linearContactUs.setOnClickListener(this)
        binding.linearAskQuestions.setOnClickListener(this)
        binding.linearPostListings.setOnClickListener(this)
        binding.linearProductListings.setOnClickListener(this)
        binding.linearTermsandConditions.setOnClickListener(this)
        binding.linearPrivacyPolicy.setOnClickListener(this)
        binding.linearFAQ.setOnClickListener(this)
        binding.linearHelpAndSupport.setOnClickListener(this)
        binding.linearUsefulLinks.setOnClickListener(this)
        binding.linearLogout.setOnClickListener(this)
    }

    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")
        Log.e("userId_",userId.toString())

        ViewController.showLoading(requireActivity())
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getProfileApi(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        binding.txtName.text = rsp.data?.name.toString()
                        binding.txtEmail.text = rsp.data?.email.toString()
                        binding.txtMobile.text = rsp.data?.phone.toString()
                        if (!rsp.data?.image.equals("")){
                            Glide.with(binding.profileImage).load(rsp.data?.image).into(binding.profileImage)
                        }
                    }
                } else {
                    ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })
    }


    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.imgEdit -> {
                startActivity(Intent(activity, EditProfileActivity::class.java))
            }

            R.id.linearJobalerts -> {
                startActivity(Intent(activity, JobAlertsActivity::class.java))
            }

            R.id.linearAboutUs -> {
                startActivity(Intent(activity, AboutUsActivity::class.java))
            }

            R.id.linearContactUs -> {
                startActivity(Intent(activity, ContactUsActivity::class.java))
            }

            R.id.linearAskQuestions -> {
                startActivity(Intent(activity, AskQuestionsActivity::class.java))
            }

            R.id.linearPostListings -> {
                startActivity(Intent(activity, MyPostsActivity::class.java))
            }

            R.id.linearProductListings -> {
                startActivity(Intent(activity, MyProductsActivity::class.java))
            }

            R.id.linearTermsandConditions -> {
                startActivity(Intent(activity, TermsAndConditionsActivity::class.java))
            }

            R.id.linearPrivacyPolicy -> {
                startActivity(Intent(activity, PrivacyPolicyActivity::class.java))
            }

            R.id.linearFAQ -> {
                startActivity(Intent(activity, FaqActivity::class.java))
            }

            R.id.linearHelpAndSupport -> {
                startActivity(Intent(activity, HelpAndSupportActivity::class.java))
            }

            R.id.linearUsefulLinks -> {
                startActivity(Intent(activity, UsefulLinksActivity::class.java))
            }

            R.id.linearLogout -> {
                logOut()
            }

        }
    }


    private fun logOut() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Are you sure you want to logout?")
        builder.setTitle("Alert!")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Yes"
        ) { dialog: DialogInterface?, which: Int ->
            Preferences.deleteSharedPreferences(requireActivity())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finishAffinity()
        }
        builder.setNegativeButton(
            "No"
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }


}