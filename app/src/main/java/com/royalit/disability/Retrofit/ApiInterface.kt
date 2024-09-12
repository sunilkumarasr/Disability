package com.royalit.disability.Retrofit

import com.royalit.disability.AdaptersAndModels.AboutusResponse
import com.royalit.disability.AdaptersAndModels.AddPostResponse
import com.royalit.disability.AdaptersAndModels.AddProductResponse
import com.royalit.disability.AdaptersAndModels.AskListModel
import com.royalit.disability.AdaptersAndModels.AskQuestionsModel
import com.royalit.disability.AdaptersAndModels.AskQuestionsRequest
import com.royalit.disability.AdaptersAndModels.Categorys.CategoriesModel
import com.royalit.disability.AdaptersAndModels.Citys.CitysModel
import com.royalit.disability.AdaptersAndModels.ContactUsResponse
import com.royalit.disability.AdaptersAndModels.EmailRequest
import com.royalit.disability.AdaptersAndModels.EnqueryRequest
import com.royalit.disability.AdaptersAndModels.EnqueryResponse
import com.royalit.disability.AdaptersAndModels.EnquerySaleRequest
import com.royalit.disability.AdaptersAndModels.Faq.FaqModel
import com.royalit.disability.AdaptersAndModels.ForgotEmailResponse
import com.royalit.disability.AdaptersAndModels.HelpAndSupport.HelpAndSupportRequest
import com.royalit.disability.AdaptersAndModels.HelpAndSupport.HelpAndSupportResponse
import com.royalit.disability.AdaptersAndModels.Home.HomeBannersModel
import com.royalit.disability.AdaptersAndModels.JobAlerts.JobAlertModel
import com.royalit.disability.AdaptersAndModels.LoginRequest
import com.royalit.disability.AdaptersAndModels.LoginResponse
import com.royalit.disability.AdaptersAndModels.MyPostsList.MyPostsModel
import com.royalit.disability.AdaptersAndModels.MyProductsModel
import com.royalit.disability.AdaptersAndModels.Notifications.NotificationModel
import com.royalit.disability.AdaptersAndModels.OTPRequest
import com.royalit.disability.AdaptersAndModels.OTPResponse
import com.royalit.disability.AdaptersAndModels.PostBannersModel
import com.royalit.disability.AdaptersAndModels.PostItemDeleteModel
import com.royalit.disability.AdaptersAndModels.PostItemDetailsModel
import com.royalit.disability.AdaptersAndModels.PrivacyPolicyResponse
import com.royalit.disability.AdaptersAndModels.ProductItemDeleteModel
import com.royalit.disability.AdaptersAndModels.ProductItemDetailsModel
import com.royalit.disability.AdaptersAndModels.ProfileResponse
import com.royalit.disability.AdaptersAndModels.RegisterRequest
import com.royalit.disability.AdaptersAndModels.RegisterResponse
import com.royalit.disability.AdaptersAndModels.SalesBannersModel
import com.royalit.disability.AdaptersAndModels.TermsConditionsResponse
import com.royalit.disability.AdaptersAndModels.SalesHome.SaleModel
import com.royalit.disability.AdaptersAndModels.State.StateModel
import com.royalit.disability.AdaptersAndModels.SubCategoriesItems.SubCategoriesItemsModel
import com.royalit.disability.AdaptersAndModels.UpdateProfileResponse
import com.royalit.disability.AdaptersAndModels.UseFullLinks.UseFullLinksModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    //logins
    @POST("register")
    fun registerApi(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("login")
    fun loginApi(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("forgot_password")
    fun forgotEmailApi(@Body loginRequest: EmailRequest): Call<ForgotEmailResponse>

    @POST("verify_otp")
    fun otpApi(@Body loginRequest: OTPRequest): Call<OTPResponse>

    //menu
    @POST("help_support")
    fun HelpAndSupportApi(@Body helpSupprtRequest: HelpAndSupportRequest): Call<HelpAndSupportResponse>

    //menu
    @POST("askquestion")
    fun askQuestionApi(@Body askRequest: AskQuestionsRequest): Call<AskQuestionsModel>

    @GET("ticket_list")
    fun askListListApi(@Query("user_id") user_id: String?): Call<List<AskListModel>>

    @GET("profile_get")
    fun getProfileApi(@Query("id") id: String?): Call<ProfileResponse>

    @Multipart
    @POST("update_profile")
    fun updateProfileApi(
        @Part("user_id") user_id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<UpdateProfileResponse>

    @GET("aboutus")
    fun aboutusApi(): Call<AboutusResponse>

    @GET("privacy_policy")
    fun privacyPolicyApi(): Call<PrivacyPolicyResponse>

    @GET("terms_conditions")
    fun termsConditionsApi(): Call<TermsConditionsResponse>

    @GET("contactus")
    fun contactUsApi(): Call<ContactUsResponse>

    @GET("job_alerts")
    fun jobAlertApi(): Call<List<JobAlertModel>>

    @GET("faq")
    fun faqListApi(): Call<List<FaqModel>>

    @GET("usefull_link")
    fun useFullLinksApi(): Call<List<UseFullLinksModel>>

    //home
    @GET("home_banner_list")
    fun HomebannersApi(): Call<List<HomeBannersModel>>

    @GET("notification")
    fun NotificationsListApi(): Call<List<NotificationModel>>

    @GET("state")
    fun stateListApi(): Call<List<StateModel>>

    @GET("city")
    fun cityApi(@Query("state") state: String?): Call<List<CitysModel>>

    //products category's
    @GET("product_banner_list")
    fun SalesbannersApi(): Call<List<SalesBannersModel>>

    @Multipart
    @POST("add_product")
    fun addProductApi(
        @Part("product") product: RequestBody,
        @Part("actual_price") actualPrice: RequestBody,
        @Part("offer_price") offerPrice: RequestBody,
        @Part("color") color: RequestBody,
        @Part("brand") brand: RequestBody,
        @Part("address") address: RequestBody,
        @Part("features") features: RequestBody,
        @Part("description") description: RequestBody,
        @Part("created_by") userId: RequestBody,
        @Part additional_images: List<MultipartBody.Part>
    ): Call<AddProductResponse>

    @Multipart
    @POST("update_product")
    fun updateProductApi(
        @Part("product") product: RequestBody,
        @Part("actual_price") actualPrice: RequestBody,
        @Part("offer_price") offerPrice: RequestBody,
        @Part("color") color: RequestBody,
        @Part("brand") brand: RequestBody,
        @Part("address") address: RequestBody,
        @Part("features") features: RequestBody,
        @Part("description") description: RequestBody,
        @Part("updated_by") userId: RequestBody,
        @Part("product_id") product_id: RequestBody,
        @Part additional_images: List<MultipartBody.Part>
    ): Call<AddProductResponse>

    @GET("sales_all_product_list")
    fun saleApi(): Call<SaleModel>

    @POST("sale_enquiry")
    fun enquerySaleApi(@Body enquerySaleRequest: EnquerySaleRequest): Call<EnqueryResponse>

    @GET("product")
    fun categoriesBasedItemsApi(@Query("category_id") category_id: String?): Call<List<SubCategoriesItemsModel>>

    @GET("get_sales_product_by_user")
    fun MyProductsListApi(@Query("created_by") userID: String?): Call<List<MyProductsModel>>

    @DELETE("delete_sales_product")
    fun deleteProductApi(@Query("product_id") product_id: String): Call<ProductItemDeleteModel>

    @GET("sales_product_list/{product_id}")
    fun productDetailsApi(@Path("product_id") product_id: String): Call<ProductItemDetailsModel>

    //posts sales
    @GET("listing_banner_list")
    fun PostBannersApi(): Call<List<PostBannersModel>>

    @Multipart
    @POST("add_post")
    fun addPostApi(
        @Part("city") city: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("mail") mail: RequestBody,
        @Part("address") address: RequestBody,
        @Part("about") about: RequestBody,
        @Part("services") services: RequestBody,
        @Part("created_by") created_by: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("location") location: RequestBody,
        @Part image: MultipartBody.Part,
        @Part additional_images: List<MultipartBody.Part>
    ): Call<AddPostResponse>

    @Multipart
    @POST("update_post")
    fun updatePostApi(
        @Part("city") city: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("mail") mail: RequestBody,
        @Part("address") address: RequestBody,
        @Part("about") about: RequestBody,
        @Part("services") services: RequestBody,
        @Part("location") location: RequestBody,
        @Part("updated_by") updated_by: RequestBody,
        @Part("product_id") product_id: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part image: MultipartBody.Part,
        @Part additional_images: List<MultipartBody.Part>
    ): Call<AddPostResponse>

    @GET("categories")
    fun categoriesApi(): Call<List<CategoriesModel>>

    @POST("listing_enquiry")
    fun enqueryApi(@Body enqueryRequest: EnqueryRequest): Call<EnqueryResponse>

    @GET("get_post/{post_id}")
    fun categoriesItemsDetailsApi(@Path("post_id") postId: String): Call<PostItemDetailsModel>

    @GET("get_post_by_user")
    fun MyPostsListApi(@Query("created_by") userID: String?): Call<List<MyPostsModel>>

    @DELETE("delete_post")
    fun deletePostApi(@Query("product_id") product_id: String): Call<PostItemDeleteModel>


}