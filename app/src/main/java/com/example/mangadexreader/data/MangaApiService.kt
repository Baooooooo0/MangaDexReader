package com.example.mangadexreader.data
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface định nghĩa các điểm cuối (endpoints) của MangaDex API.
 */
interface MangaApiService {

    /**
     * Lấy một danh sách truyện từ API.
     */
    @GET("manga")
    suspend fun getMangaList(@Query("includes[]") includes:String="cover_art", @Query("title") title:String?): MangaModels.MangaListResponse

    /**
     * Lấy thông tin chi tiết của một truyện dựa vào ID.
     */
    @GET("manga/{id}")
    suspend fun getMangaDetail(
        @Path("id") mangaId: String,
        @Query("includes[]") includes: String = "cover_art"
    ): MangaModels.MangaDetailResponse

}