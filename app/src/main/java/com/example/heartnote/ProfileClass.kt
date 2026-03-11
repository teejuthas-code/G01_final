package com.example.heartnote

import com.google.gson.annotations.SerializedName

data class ProfileClass(
    @SerializedName("error") val error: Boolean,
    @SerializedName("user_id") val user_id: Int?,
    @SerializedName("username") val username: String?,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profile_image") val profile_image: String,
    @SerializedName("created_at") val created_at: String?
)
