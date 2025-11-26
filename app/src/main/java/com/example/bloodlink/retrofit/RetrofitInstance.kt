package com.example.bloodlink.retrofit

import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    //const val BACKEND_URL = "https://blood-link-backend-g0hh.onrender.com/"
    const val BACKEND_URL = "http://192.168.1.106:8080/"

    fun createRetrofit(context: Context): Retrofit {
        val tokenManager = TokenManager(context)
        val client = createHttpClient(tokenManager)

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BACKEND_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createHttpClient(tokenManager: TokenManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                JWTInterceptor {
                    // Synchronous token retrieval for interceptor
                    runBlocking {
                        tokenManager.tokenFlow.first()
                    }
                }
            ).addInterceptor(HttpLoggingInterceptor().apply {
                HttpLoggingInterceptor.Level.BODY
            }).connectTimeout(60, TimeUnit.SECONDS) // Time to establish a connection
            .readTimeout(60, TimeUnit.SECONDS)    // Time to wait for data to be read
            .writeTimeout(60, TimeUnit.SECONDS)   // Time to wait for data to be written
            .build()
    }

    //private val client = createHttpClient(TokenManager(LocalContext.current))

    // Create the Retrofit instance
    /*private val retrofit by lazy {
        Retrofit.Builder()
            .client(client)
            .baseUrl(BACKEND_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }*/

    // Keep a cache of Retrofit instances per context to avoid recreation
    private var retrofit: Retrofit? = null

    // This is the main function you will call from your app
    fun getRetrofit(context: Context): Retrofit {
        // Return the cached instance if it exists, otherwise create it
        return retrofit ?: createRetrofit(context).also {
            retrofit = it
        }
    }

    fun getAlertApi(context: Context): AlertApi {
        return getRetrofit(context).create(AlertApi::class.java)
    }

    fun getUserApi(context: Context): UserApi {
        return getRetrofit(context).create(UserApi::class.java)
    }

    fun getAuthenticationApi(context: Context): AuthenticationApi {
        return getRetrofit(context).create(AuthenticationApi::class.java)
    }

    fun getBankStockApi(context: Context): BankStockApi {
        return getRetrofit(context).create(BankStockApi::class.java)
    }

    fun getBloodBankApi(context: Context): BloodBankApi {
        return getRetrofit(context).create(BloodBankApi::class.java)
    }

    fun getBloodRequestApi(context: Context): BloodRequestApi {
        return getRetrofit(context).create(BloodRequestApi::class.java)
    }

    fun getDoctorApi(context: Context): DoctorApi {
        return getRetrofit(context).create(DoctorApi::class.java)
    }

    fun getDonationRequestApi(context: Context): DonationRequestApi {
        return getRetrofit(context).create(DonationRequestApi::class.java)
    }

    fun getDonorApi(context: Context): DonorApi {
        return getRetrofit(context).create(DonorApi::class.java)
    }

    fun getDonorResponseApi(context: Context): DonorResponseApi {
        return getRetrofit(context).create(DonorResponseApi::class.java)
    }

    fun getProfileApi(context: Context): ProfileApi {
        return getRetrofit(context).create(ProfileApi::class.java)
    }

}
