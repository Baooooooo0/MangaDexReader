package com.example.mangadexreader.data
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Interface định nghĩa các điểm cuối (endpoints) của MangaDex API.
 */
interface MangaApiService {
    /**
     * Lấy một danh sách truyện từ API.
     */
    @GET("manga")
    suspend fun getMangaList(
        @Query("includes[]") includes:String="cover_art",
        @Query("title") title:String?,
        @Query("limit") limit:Int,
        @Query("offset") offset:Int
    ): MangaModels.MangaListResponse

    /**
     * Lấy thông tin chi tiết của một truyện dựa vào ID.
     */
    @GET("manga/{id}")
    suspend fun getMangaDetail(
        @Path("id") mangaId: String,
        @Query("includes[]") includes: String = "cover_art"
    ): MangaModels.MangaDetailResponse

    @GET("manga/{id}/feed")
    suspend fun getChapterFeed(
        @Path("id") mangaId: String,
        @QueryMap options: Map<String, String>
    ): MangaModels.ChapterListResponse

    @GET("/at-home/server/{id}")
    suspend fun getReaderPages(
        @Path("id") chapterId: String
    ): MangaModels.ReaderPageResponse

    /**
     * Lấy danh sách truyện mà người dùng đã bookmark.
     * Yêu cầu Access Token.
     */
    @GET("user/follows/manga")
    suspend fun getFollowedManga(
        @Header("Authorization") token: String
    ): MangaModels.MangaListResponse // API này trả về cấu trúc giống hệt MangaListResponse

    /**
     * Bookmark một truyện.
     * Yêu cầu Access Token.
     */
    @POST("manga/{id}/follow")
    suspend fun followManga(
        @Header("Authorization") token: String,
        @Path("id") mangaId: String
    ): Response<Unit> // Chúng ta chỉ cần biết thành công hay không, không cần dữ liệu trả về

    /**
     * Bỏ bookmark một truyện.
     * Yêu cầu Access Token.
     */
    @DELETE("manga/{id}/follow")
    suspend fun unfollowManga(
        @Header("Authorization") token: String,
        @Path("id") mangaId: String
    ): Response<Unit>

    @POST("auth/login")
    suspend fun loginWithGoogle(
        @Body request: MangaModels.LoginRequest
    ): MangaModels.LoginResponse
}