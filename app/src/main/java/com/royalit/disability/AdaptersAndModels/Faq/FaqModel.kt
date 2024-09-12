package com.royalit.disability.AdaptersAndModels.Faq

data class FaqModel(
    val title: String,
    val slug: String,
    val description: String,
    val created_by: String,
    val status: String,
    val created_date: String,
    val created_time: String,
    var isExpanded: Boolean = false
)