package com.example.mangadexreader.repository

import com.example.mangadexreader.data.ApiClient
import com.example.mangadexreader.data.MangaModels.ChapterListResponse
import com.example.mangadexreader.data.MangaModels

/**
 * Repository chịu trách nhiệm lấy dữ liệu truyện từ các nguồn dữ liệu (hiện tại là từ API).
 */
class MangaRepository {

    // Lấy instance của ApiService từ ApiClient đã tạo
    private val apiService = ApiClient.apiService

    /**
     * Gọi API để lấy danh sách truyện.
     * Đây là một suspend function vì nó gọi một suspend function khác từ ApiService.
     */
    suspend fun getMangaList(title: String?, limit: Int, offset: Int): MangaModels.MangaListResponse {
        return apiService.getMangaList(title = title, limit = limit, offset = offset)
    }

    /**
     * Gọi service để lấy thông tin chi tiết của truyện.
     */
    suspend fun getMangaDetail(mangaId: String): MangaModels.MangaDetailResponse {
        return apiService.getMangaDetail(mangaId = mangaId)
    }

    suspend fun getChapterFeed(mangaId: String): ChapterListResponse {
        val orderOptions = mapOf("order[chapter]" to "asc")
        return apiService.getChapterFeed(mangaId, orderOptions)
    }

    suspend fun getReaderPages(chapterId: String): MangaModels.ReaderPageResponse {
        return apiService.getReaderPages(chapterId)
    }

    suspend fun getFollowedManga(token: String): MangaModels.MangaListResponse {
        // Thêm "Bearer " vào trước token là quy tắc của API
        return apiService.getFollowedManga(token = "Bearer $token")
    }

    suspend fun followManga(token: String, mangaId: String) {
        apiService.followManga(token = "Bearer $token", mangaId = mangaId)
    }

    suspend fun unfollowManga(token: String, mangaId: String) {
        apiService.unfollowManga(token = "Bearer $token", mangaId = mangaId)
    }

    suspend fun loginWithGoogle(request: MangaModels.LoginRequest): MangaModels.LoginResponse {
        return apiService.loginWithGoogle(request)
    }
}