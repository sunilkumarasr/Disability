package com.royalit.disability.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.royalit.disability.Activitys.AboutUsActivity
import com.royalit.disability.Activitys.AddPostActivity
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
import com.royalit.disability.R
import com.royalit.disability.databinding.FragmentCategoriesBinding
import com.royalit.disability.databinding.FragmentProfileBinding

class CategoriesFragment : Fragment(), View.OnClickListener {


    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {
        binding.cardAdd.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.cardAdd -> {
                startActivity(Intent(activity, AddPostActivity::class.java))
            }

        }
    }

}