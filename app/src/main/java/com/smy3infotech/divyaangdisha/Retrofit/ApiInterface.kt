package com.smy3infotech.divyaangdisha.Retrofit

import com.smy3infotech.divyaangdisha.AdaptersAndModels.AboutusResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.AddPostResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.AddProductResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.AskListModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.AskQuestionsModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.AskQuestionsRequest
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Categorys.CategoriesModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Categorys.SubCategoriesModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Citys.CitysModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.ContactUsResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.CreatePasswordRequest
import com.smy3infotech.divyaangdisha.AdaptersAndModels.CreatePasswordResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.DeletePostImageModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.DeleteProductImageModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.EmailRequest
import com.smy3infotech.divyaangdisha.AdaptersAndModels.EnqueryRequest
import com.smy3infotech.divyaangdisha.AdaptersAndModels.EnqueryResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.EnquerySaleRequest
import com.smy3infotech.divyaangdisha.AdaptersAndModels.EnquieryPostModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.EnquieryProductModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Faq.FaqModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.ForgotEmailResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.HelpAndSupport.HelpAndSupportRequest
import com.smy3infotech.divyaangdisha.AdaptersAndModels.HelpAndSupport.HelpAndSupportResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Home.HomeBannersModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.JobAlerts.JobAlertModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.LoginRequest
import com.smy3infotech.divyaangdisha.AdaptersAndModels.LoginResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.MyPostsList.MyPostsModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.MyProductsModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Notifications.NotificationModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.OTPRequest
import com.smy3infotech.divyaangdisha.AdaptersAndModels.OTPResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.PostBannersModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.PostItemDeleteModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.PostItemDetailsModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.PrivacyPolicyResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.ProductItemDeleteModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.ProductItemDetailsModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.ProfileResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.RegisterRequest
import com.smy3infotech.divyaangdisha.AdaptersAndModels.RegisterResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SalesBannersModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.TermsConditionsResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SalesHome.SaleModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SocialMediaModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.State.StateModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SubCategoriesItems.SubCategoriesItemsModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.UpdateLocationResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.UpdateProfileResponse
import com.smy3infotech.divyaangdisha.AdaptersAndModels.UseFullLinks.UseFullLinksModel
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

    @POST("reset_password")
    fun createApi(@Body createRequest: CreatePasswordRequest): Call<CreatePasswordResponse>

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
        @Part("country_code") country_code: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("location") location: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("km") km: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<UpdateProfileResponse>

    @Multipart
    @POST("update_user_location")
    fun updateLocation(
        @Part("user_id") user_id: RequestBody,
        @Part("location") location: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody
    ): Call<UpdateLocationResponse>

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

    @GET("firebase_notification")
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
        @Part("phone") phone: RequestBody,
        @Part("color") color: RequestBody,
        @Part("brand") brand: RequestBody,
        @Part("address") address: RequestBody,
        @Part("features") features: RequestBody,
        @Part("description") description: RequestBody,
        @Part("created_by") userId: RequestBody,
        @Part("location") location: RequestBody,
        @Part additional_images: List<MultipartBody.Part>
    ): Call<AddProductResponse>

    @Multipart
    @POST("update_product")
    fun updateProductApi(
        @Part("product") product: RequestBody,
        @Part("actual_price") actualPrice: RequestBody,
        @Part("offer_price") offerPrice: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("color") color: RequestBody,
        @Part("brand") brand: RequestBody,
        @Part("address") address: RequestBody,
        @Part("features") features: RequestBody,
        @Part("description") description: RequestBody,
        @Part("updated_by") userId: RequestBody,
        @Part("product_id") product_id: RequestBody,
        @Part("location") location: RequestBody,
        @Part additional_images: List<MultipartBody.Part>
    ): Call<AddProductResponse>

    @GET("sales_all_product_list")
    fun saleApi(@Query("location") category_id: String?): Call<SaleModel>

    @POST("sale_enquiry")
    fun enquerySaleApi(@Body enquerySaleRequest: EnquerySaleRequest): Call<EnqueryResponse>

    @GET("subcategories")
    fun subcategoriesApi(@Query("category_id") category_id: String?): Call<List<SubCategoriesModel>>

    @GET("product_listing_cat_subcat")
    fun categoriesBasedItemsApi(
        @Query("category_id") category_id: String?,
        @Query("subcategory_id") subcategory_id: String?,
        @Query("latitude") latitude: String?,
        @Query("longitude") longitude: String?,
        @Query("km") km: String?,
    ): Call<List<SubCategoriesItemsModel>>


//    @GET("product_listing_cat_subcat")
//    fun categoriesBasedItemsApi(
//        @Query("category_id") category_id: String?,
//        @Query("subcategory_id") subcategory_id: String?
//    ): Call<List<SubCategoriesItemsModel>>

    @GET("get_sales_product_by_user")
    fun MyProductsListApi(@Query("created_by") userID: String?): Call<List<MyProductsModel>>

    @DELETE("delete_sales_product")
    fun deleteProductApi(@Query("product_id") product_id: String): Call<ProductItemDeleteModel>

    @GET("delete_sale_images")
    fun deleteProductImageApi(@Query("id") id: String?): Call<DeleteProductImageModel>

    @GET("sales_product_list/{product_id}")
    fun productDetailsApi(@Path("product_id") product_id: String): Call<ProductItemDetailsModel>

    @GET("sale_enquiry_user")
    fun EnquieryProductListApi(@Query("product") product_id: String?): Call<List<EnquieryProductModel>>

    //posts sales
    @GET("listing_banner_list")
    fun PostBannersApi(): Call<List<PostBannersModel>>

    @Multipart
    @POST("add_post")
    fun addPostApi(
        @Part("location") location: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("subcategory") subcategoryid: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("landline") landline: RequestBody,
        @Part("mail") mail: RequestBody,
        @Part("address") address: RequestBody,
        @Part("about") about: RequestBody,
        @Part("services") services: RequestBody,
        @Part("created_by") created_by: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part image: MultipartBody.Part,
        @Part additional_images: List<MultipartBody.Part>
    ): Call<AddPostResponse>

    @Multipart
    @POST("update_post")
    fun updatePostApi(
        @Part("location") location: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("subcategory") subcategoryid: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("landline") landline: RequestBody,
        @Part("mail") mail: RequestBody,
        @Part("address") address: RequestBody,
        @Part("about") about: RequestBody,
        @Part("services") services: RequestBody,
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

    @GET("delete_listing_images")
    fun deletePostImageApi(@Query("id") id: String?): Call<DeletePostImageModel>

    @GET("listing_enquiry_user")
    fun EnquieryListApi(@Query("product_id") product_id: String?): Call<List<EnquieryPostModel>>

    @GET("socialmedia")
    fun socialMediaApi(): Call<List<SocialMediaModel>>

}