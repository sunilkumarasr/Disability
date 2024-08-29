package com.royalit.disability.Retrofit

import com.royalit.disability.AdaptersAndModels.AboutusResponse
import com.royalit.disability.AdaptersAndModels.AddProductResponse
import com.royalit.disability.AdaptersAndModels.CategoriesModel
import com.royalit.disability.AdaptersAndModels.CitysModel
import com.royalit.disability.AdaptersAndModels.ContactUsResponse
import com.royalit.disability.AdaptersAndModels.LoginRequest
import com.royalit.disability.AdaptersAndModels.LoginResponse
import com.royalit.disability.AdaptersAndModels.PrivacyPolicyResponse
import com.royalit.disability.AdaptersAndModels.ProfileResponse
import com.royalit.disability.AdaptersAndModels.RegisterRequest
import com.royalit.disability.AdaptersAndModels.RegisterResponse
import com.royalit.disability.AdaptersAndModels.TermsConditionsResponse
import com.royalit.disability.AdaptersAndModels.SaleModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiInterface {

    @POST("register")
    fun registerApi(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("login")
    fun loginApi(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("profile_get")
    fun getProfileApi(@Query("id") id: String?): Call<ProfileResponse>

    @GET("aboutus")
    fun aboutusApi(): Call<AboutusResponse>

    @GET("privacy_policy")
    fun privacyPolicyApi(): Call<PrivacyPolicyResponse>

    @GET("terms_conditions")
    fun termsConditionsApi(): Call<TermsConditionsResponse>

    @GET("contactus")
    fun contactUsApi(): Call<ContactUsResponse>

    @GET("categories")
    fun categoriesApi(): Call<List<CategoriesModel>>

    @GET("sales_product_list")
    fun saleApi(): Call<List<SaleModel>>

    @GET("city")
    fun cityApi(): Call<List<CitysModel>>

//    @Multipart
//    @POST("submitForm")
//    fun addProductApi(
//        @Part("product") product: RequestBody,
//        @Part("actual_price") actualPrice: RequestBody,
//        @Part("offer_price") offerPrice: RequestBody,
//        @Part("address") address: RequestBody,
//        @Part("features") features: RequestBody,
//        @Part("description") description: RequestBody,
//        @Part image: MultipartBody.Part
//    ): Call<AddProductResponse>

}