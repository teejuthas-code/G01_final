package com.example.heartnote

import com.google.gson.annotations.SerializedName

data class RegisterClass(
    @SerializedName("username") val username: String?,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("user_password") val user_password: String?
)