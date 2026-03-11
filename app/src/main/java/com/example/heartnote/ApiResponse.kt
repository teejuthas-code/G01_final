package com.example.heartnote

data class ApiResponse(
    val error: Boolean?,
    val message: String?,
    val insertId: Int? = null
)
