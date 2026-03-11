package com.example.heartnote

import com.google.gson.annotations.SerializedName

data class MessageResponse(

    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String
)