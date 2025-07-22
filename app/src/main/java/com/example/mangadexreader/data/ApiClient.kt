package com.example.mangadexreader.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Header

/**
 * Object Singleton để tạo và cung cấp một instance của ApiService.
 */
object ApiClient {

    // URL cơ sở của API MangaDex. **LƯU Ý: Phải kết thúc bằng dấu / **
    private const val BASE_URL = "https://api.mangadex.org/"

    // Cấu hình trình phân tích JSON, bỏ qua các khóa không xác định để tránh lỗi
    private val json = Json { ignoreUnknownKeys = true }

    // Tạo một interceptor để log lại các request và response, rất hữu ích để debug
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Tạo OkHttpClient và thêm interceptor vào
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Tạo đối tượng Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient) // Sử dụng http client đã có logger
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType())) // Thêm bộ chuyển đổi Kotlinx Serialization
        .build()

    /**
     * Cung cấp một instance của MangaApiService.
     * 'by lazy' đảm bảo rằng đối tượng chỉ được tạo ra ở lần đầu tiên nó được gọi.
     */
    val apiService: MangaApiService by lazy {
        retrofit.create(MangaApiService::class.java)
    }
}