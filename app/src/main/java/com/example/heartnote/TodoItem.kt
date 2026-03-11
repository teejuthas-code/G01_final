package com.example.heartnote

import com.google.gson.annotations.SerializedName

data class TodoItem(


    @SerializedName("todo_list_id")
    val todoListId: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("task_text")
    val taskText: String,

    @SerializedName("description")
    val description: String?,


    @SerializedName("reminder_at")
    val reminderAt: String? = null,


    @SerializedName("is_done")
    val isDone: Int







)
