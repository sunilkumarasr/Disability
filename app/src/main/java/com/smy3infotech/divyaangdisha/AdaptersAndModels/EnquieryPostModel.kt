package com.smy3infotech.divyaangdisha.AdaptersAndModels

data class EnquieryPostModel(
    val id: String,
    val category_id: String,
    val subcategory_id: String,
    val name: String,
    val product_id: String,
    val email: String,
    val phone: String,
    val message: String,
    val created_by: String,
    val created_date: String
)
