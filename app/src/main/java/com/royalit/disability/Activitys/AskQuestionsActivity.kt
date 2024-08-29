package com.royalit.disability.Activitys

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.databinding.ActivityAboutUsBinding
import com.royalit.disability.databinding.ActivityAskQuestionsBinding

class AskQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    val binding: ActivityAskQuestionsBinding by lazy {
        ActivityAskQuestionsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Ask Questions"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        binding.cardAdd.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.cardAdd -> {
                AddshowDialog()
            }

        }
    }


    private fun AddshowDialog() {
        val dialog = Dialog(this@AskQuestionsActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_question_custom_dialog)

// Set the dialog size
        val window = dialog.window
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, // Width
            ViewGroup.LayoutParams.WRAP_CONTENT  // Height
        )

        val imgClose = dialog.findViewById<ImageView>(R.id.imgClose)
        imgClose?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}