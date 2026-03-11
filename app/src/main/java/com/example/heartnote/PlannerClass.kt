package com.example.heartnote

import com.google.gson.annotations.SerializedName

data class PlannerClass(
    @SerializedName("planner_id") val planner_id: Int=0,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("planner_content") val planner_content: String,
    @SerializedName("event_start") val event_start: String,
    @SerializedName("event_end") val event_end: String,
    @SerializedName("remind") val remind: String,
    @SerializedName("is_done") val is_done: Int,
    @SerializedName("delete_at")
    val delete_at: String? = null
)

