package com.royalit.disability.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class AddProductResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("message" ) var message : String? = null
)
