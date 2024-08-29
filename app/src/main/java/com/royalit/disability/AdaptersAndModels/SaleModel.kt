package com.royalit.disability.AdaptersAndModels

data class SaleModel(
    val id: String,
    val product: String,
    val actual_price: String,
    val offer_price: String,
    val address: String,
    val features: String,
    val description: String,
    val image: String,
    val created_date: String,
    val created_time: String
)