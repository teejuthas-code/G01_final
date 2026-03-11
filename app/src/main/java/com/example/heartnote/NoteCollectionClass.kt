package com.example.heartnote

import com.google.gson.annotations.SerializedName

data class NoteCollectionClass(
    @SerializedName("note_id") val note_id: Int,
    @SerializedName("collection_id") val collection_id: Int
)