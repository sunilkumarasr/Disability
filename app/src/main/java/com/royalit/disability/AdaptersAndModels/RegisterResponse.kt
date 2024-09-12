package com.royalit.disability.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("message" ) var message : String? = null
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val location: String,
    val state: String,
    val city: String,
    val password: String
)