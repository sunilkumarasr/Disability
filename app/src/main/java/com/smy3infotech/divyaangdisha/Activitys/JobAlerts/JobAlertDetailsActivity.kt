package com.smy3infotech.divyaangdisha.Activitys.JobAlerts

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.databinding.ActivityJobAlertDetailsBinding

class JobAlertDetailsActivity : AppCompatActivity() {

    val binding: ActivityJobAlertDetailsBinding by lazy {
        ActivityJobAlertDetailsBinding.inflate(layoutInflater)
    }

    lateinit var title:String
    lateinit var description:String
    lateinit var post_date:String
    lateinit var last_date:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        title= intent.getStringExtra("title").toString()
        description= intent.getStringExtra("description").toString()
        post_date= intent.getStringExtra("post_date").toString()
        last_date= intent.getStringExtra("last_date").toString()

        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Job Alerts Details"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }


        if(!ViewController.noInterNetConnectivity(applicationContext)){
            ViewController.showToast(applicationContext, "Please check your connection ")
        }else{
            JobAlertDetailsApi()
        }

    }

    private fun JobAlertDetailsApi() {
        binding.txtSub.text = title
        val processedHtml = autoLinkUrls(description ?: "")
        binding.txtDec.text = Html.fromHtml(processedHtml, Html.FROM_HTML_MODE_LEGACY)
        binding.txtDec.movementMethod = LinkMovementMethod.getInstance()
        binding.txtDec.linksClickable = true

        binding.txtPostDate.text = post_date
        binding.txtLastDate.text = last_date
    }


    fun autoLinkUrls(html: String): String {
        val urlRegex = "(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+)".toRegex()
        return urlRegex.replace(html) {
            val url = it.value
            "<a href=\"$url\">$url</a>"
        }
    }


}