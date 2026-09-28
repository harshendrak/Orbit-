package com.orbit.recovery.data.remote

import com.orbit.recovery.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class AnthropicRequest(
    val model: String = "claude-sonnet-4-6",
    val max_tokens: Int = 1024,
    val system: String,
    val messages: List<ApiMessage>
)

data class ApiMessage(val role: String, val content: String)

data class AnthropicResponse(
    val content: List<AnthropicContent>
)

data class AnthropicContent(
    val text: String
)

interface AnthropicApiService {
    @POST("v1/messages")
    suspend fun sendMessage(@Body request: AnthropicRequest): AnthropicResponse

    companion object {
        fun create(): AnthropicApiService {
            val interceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-api-key", BuildConfig.ANTHROPIC_API_KEY)
                    .addHeader("anthropic-version", "2023-06-01")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://api.anthropic.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AnthropicApiService::class.java)
        }
    }
}
