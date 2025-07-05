package com.example.mangadexreader.repository

import com.example.mangadexreader.data.ApiClient
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
    suspend fun getMangaList(): MangaModels.MangaListResponse {
        return apiService.getMangaList()
    }

    /**
     * Gọi service để lấy thông tin chi tiết của truyện.
     */
    suspend fun getMangaDetail(mangaId: String): MangaModels.MangaDetailResponse {
        return apiService.getMangaDetail(mangaId = mangaId)
    }
}