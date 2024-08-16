package com.royalit.disability.Fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.royalit.disability.Activitys.AboutUsActivity
import com.royalit.disability.Activitys.AskQuestionsActivity
import com.royalit.disability.Activitys.ContactUsActivity
import com.royalit.disability.Activitys.EditProfileActivity
import com.royalit.disability.Activitys.FaqActivity
import com.royalit.disability.Activitys.HelpAndSupportActivity
import com.royalit.disability.Activitys.JobAlertsActivity
import com.royalit.disability.Activitys.PostListingsActivity
import com.royalit.disability.Activitys.PrivacyPolicyActivity
import com.royalit.disability.Activitys.ProductListingsActivity
import com.royalit.disability.Activitys.TermsAndConditionsActivity
import com.royalit.disability.Activitys.UsefulLinksActivity
import com.royalit.disability.Logins.LoginActivity
import com.royalit.disability.R
import com.royalit.disability.databinding.FragmentProfileBinding

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
                startActivity(Intent(activity, PostListingsActivity::class.java))
            }

            R.id.linearProductListings -> {
                startActivity(Intent(activity, ProductListingsActivity::class.java))
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
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finishAffinity() // Close all activities in the app
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