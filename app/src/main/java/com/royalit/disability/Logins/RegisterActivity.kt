package com.royalit.disability.Logins

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.royalit.disability.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }


}