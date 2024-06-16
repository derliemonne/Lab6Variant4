package com.example.lab6variant4.network
import com.example.lab6variant4.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName


private const val BASE_URL = "https://cleaner.dadata.ru/api/v1/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface ApiService {
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json",
        "Authorization: Token ${BuildConfig.API_KEY}",
        "X-Secret: ${BuildConfig.SECRET_KEY}"
    )
    @POST("clean/phone")
    fun cleanPhone(@Body requestBody: String): Call<String>}

object Api {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

data class PhoneNumberResponse(
    @SerializedName("source") val source: String,
    @SerializedName("type") val type: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("country_code") val countryCode: String,
    @SerializedName("city_code") val cityCode: String,
    @SerializedName("number") val number: String,
    @SerializedName("extension") val extension: String?,
    @SerializedName("provider") val provider: String,
    @SerializedName("country") val country: String,
    @SerializedName("region") val region: String,
    @SerializedName("city") val city: String,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("qc_conflict") val qcConflict: Int,
    @SerializedName("qc") val qc: Int
)