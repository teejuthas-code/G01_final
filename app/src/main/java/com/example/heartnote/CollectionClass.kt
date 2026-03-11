package com.example.heartnote

import com.google.gson.annotations.SerializedName

data class CollectionClass(
    @SerializedName("collection_id")val collection_id: Int = 0,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("collection_name") val collection_name: String,
    @SerializedName("background_color") val background_color: String
)