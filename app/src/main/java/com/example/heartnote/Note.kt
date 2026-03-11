package com.example.heartnote

import com.google.gson.annotations.SerializedName

data class Note (
    @SerializedName("note_id") val note_id: Int = 0,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("title") val title: String = "",
    @SerializedName("content") val content: String = "",
    @SerializedName("background_color") val background_color: String = "",
    @SerializedName("create_at") val create_at: String = "",
    @SerializedName("is_fav") val is_fav: Int = 0,
    @SerializedName("delete_at")
    val delete_at: String? = null
)