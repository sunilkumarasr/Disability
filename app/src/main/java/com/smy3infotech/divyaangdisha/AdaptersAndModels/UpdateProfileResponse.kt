package com.smy3infotech.divyaangdisha.AdaptersAndModels

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("message" ) var message : String? = null
)