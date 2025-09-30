package com.smy3infotech.divyaangdisha.Activitys

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.databinding.ActivityChatBinding

class ChatListActivity : AppCompatActivity(), View.OnClickListener {

    val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)


        inits()


    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Chat"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener {
            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.imgNotification -> {
//                val animations = ViewController.animation()
//                v.startAnimation(animations)
//
//                startActivity(Intent(this@ChatActivity, NotificationActivity::class.java))
//            }

        }
    }


}