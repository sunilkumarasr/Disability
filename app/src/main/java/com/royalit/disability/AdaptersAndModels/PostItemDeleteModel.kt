package com.royalit.disability.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class PostItemDeleteModel(
    @SerializedName("message" ) var message : String? = null
)
