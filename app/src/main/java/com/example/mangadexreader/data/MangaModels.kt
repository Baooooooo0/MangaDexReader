package com.example.mangadexreader.data
import com.example.mangadexreader.data.MangaModels.MangaData
import kotlinx.serialization.Serializable

class MangaModels {
    /**
     * Lớp bao bọc toàn bộ phản hồi từ API danh sách manga.
     */
    @Serializable
    data class MangaListResponse(
        val result: String,
        val data: List<MangaData>, // Một danh sách các đối tượng truyện
        val total: Int
    )

    @Serializable
    data class MangaDetailResponse(
        val data: MangaData
    )

    /**
     * Đại diện cho một đối tượng truyện trong mảng "data".
     */
    @Serializable
    data class MangaData(
        val id: String, // ID duy nhất của truyện
        val type: String,
        val attributes: MangaAttributes, // Các thuộc tính chi tiết của truyện
        val relationships: List<Relationship> // Mối quan hệ, dùng để tìm ảnh bìa
    ){
        val coverFileName: String?
            get() = relationships.firstOrNull{it.type == "cover_art"}?.attributes?.fileName
    }

    /**
     * Chứa các thuộc tính chi tiết như tên, mô tả.
     * Chúng ta sử dụng Map<String, String> vì API trả về nhiều ngôn ngữ (ví dụ: "en", "ja").
     * Dấu '?' cho biết trường đó có thể là null (không tồn tại trong phản hồi của API).
     */
    @Serializable
    data class MangaAttributes(
        val title: Map<String, String>,
        val description: Map<String, String>,
        val year: Int? = null, // Có thể không có năm
        val status: String? = null // Ví dụ: "ongoing", "completed"
    )

    /**
     * Đại diện cho một đối tượng trong mảng "relationships".
     * Chúng ta chỉ quan tâm đến những relationship có type là "cover_art".
     */
    @Serializable
    data class Relationship(
        val id: String,
        val type: String,
        val attributes: CoverAttribute? = null
    )

    @Serializable
    data class ChapterListResponse(
        val data: List<ChapterData>
    )

    @Serializable
    data class ChapterData(
        val id: String,
        val type: String,
        val attributes: ChapterAttributes
    )

    @Serializable
    data class ChapterAttributes(
        val title: String?,
        val chapter: String?,
        val pages: Int,
        val translatedLanguage: String
    )

    @Serializable
    data class ChapterPages(
        val hash: String,
        val data: List<String>,
        val dataSaver: List<String>
    )

    @Serializable
    data class ReaderPageResponse(
        val baseUrl: String,
        val chapter: ChapterPages
    )

    @Serializable
    data class LoginRequest(
        val idToken: String
    )

    @Serializable
    data class LoginResponse(
        val token: Token
    ) {
        @Serializable
        data class Token(
            val session: String, // Access Token
            val refresh: String  // Refresh Token (dùng để lấy token mới khi hết hạn)
        )
    }
}

@Serializable
data class CoverAttribute(
    val fileName: String? = null
)



