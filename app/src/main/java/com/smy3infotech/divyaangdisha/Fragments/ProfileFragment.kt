package com.smy3infotech.divyaangdisha.Fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.smy3infotech.divyaangdisha.Activitys.AboutUsActivity
import com.smy3infotech.divyaangdisha.Activitys.AskQuestionsActivity
import com.smy3infotech.divyaangdisha.Activitys.ContactUsActivity
import com.smy3infotech.divyaangdisha.Activitys.EditProfileActivity
import com.smy3infotech.divyaangdisha.Activitys.FaqActivity
import com.smy3infotech.divyaangdisha.Activitys.HelpAndSupportActivity
import com.smy3infotech.divyaangdisha.Activitys.JobAlerts.JobAlertsActivity
import com.smy3infotech.divyaangdisha.Activitys.Categorys.MyPostsActivity
import com.smy3infotech.divyaangdisha.Activitys.DashBoardActivity
import com.smy3infotech.divyaangdisha.Activitys.PrivacyPolicyActivity
import com.smy3infotech.divyaangdisha.Activitys.Sales.MyProductsActivity
import com.smy3infotech.divyaangdisha.Activitys.SplashActivity
import com.smy3infotech.divyaangdisha.Activitys.TermsAndConditionsActivity
import com.smy3infotech.divyaangdisha.Activitys.UsefulLinksActivity
import com.smy3infotech.divyaangdisha.AdaptersAndModels.ProfileResponse
import com.smy3infotech.divyaangdisha.Config.Preferences
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.Logins.LoginActivity
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.Retrofit.RetrofitClient
import com.smy3infotech.divyaangdisha.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

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
        binding.linearLanguage.setOnClickListener(this)
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
                        binding.txtMobile.text = rsp.data?.country_code.toString()+"-"+rsp.data?.phone.toString()
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
                val animations = ViewController.animation()
                v.startAnimation(animations)
                startActivity(Intent(activity, EditProfileActivity::class.java))
            }

            R.id.linearJobalerts -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                startActivity(Intent(activity, JobAlertsActivity::class.java))
            }

            R.id.linearLanguage -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                LanguageBottomDialog()
            }

            R.id.linearAboutUs -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                startActivity(Intent(activity, AboutUsActivity::class.java))
            }

            R.id.linearContactUs -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                startActivity(Intent(activity, ContactUsActivity::class.java))
            }

            R.id.linearAskQuestions -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                startActivity(Intent(activity, AskQuestionsActivity::class.java))
            }

            R.id.linearPostListings -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                startActivity(Intent(activity, MyPostsActivity::class.java))
            }

            R.id.linearProductListings -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                startActivity(Intent(activity, MyProductsActivity::class.java))
            }

            R.id.linearTermsandConditions -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                startActivity(Intent(activity, TermsAndConditionsActivity::class.java))
            }

            R.id.linearPrivacyPolicy -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                startActivity(Intent(activity, PrivacyPolicyActivity::class.java))
            }

            R.id.linearFAQ -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                startActivity(Intent(activity, FaqActivity::class.java))
            }

            R.id.linearHelpAndSupport -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                startActivity(Intent(activity, HelpAndSupportActivity::class.java))
            }

            R.id.linearUsefulLinks -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                startActivity(Intent(activity, UsefulLinksActivity::class.java))
            }

            R.id.linearLogout -> {
                val animations = ViewController.animation()
                v.startAnimation(animations)
                logOut()
            }

        }
    }

    private fun LanguageBottomDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_language, null)
        bottomSheetDialog.setContentView(view)

        val linearTelugu = view.findViewById<LinearLayout>(R.id.linearTelugu)
        val linearHindi = view.findViewById<LinearLayout>(R.id.linearHindi)
        val linearEnglish = view.findViewById<LinearLayout>(R.id.linearEnglish)
        linearTelugu.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            saveLocalePreference("te")
            restartActivity()
            bottomSheetDialog.dismiss()
        }
        linearHindi.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            saveLocalePreference("hi")
            restartActivity()
            bottomSheetDialog.dismiss()
        }
        linearEnglish.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            saveLocalePreference("en")
            restartActivity()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()

    }


    private fun saveLocalePreference(languageCode: String) {
        val prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("app_lang", languageCode).apply()
    }

    private fun restartActivity() {
        activity?.let {
            val intent = Intent(it, DashBoardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            it.startActivity(intent)
            it.finish()
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