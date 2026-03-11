package com.example.heartnote


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface HeartnoteAPI {
    // 1. Register: ส่งข้อมูลนักเรียนใหม่เพื่อลงทะเบียน
    @POST("register")
    suspend fun registerUser(
        @Body userData: RegisterClass
    ): Response<RegisterResponse>

    // 2. Login: ส่งรหัสผ่านและ ID ในรูปแบบ Map เพื่อตรวจสอบสิทธิ์
    @POST("login")
    suspend fun loginUser(
        @Body loginData: Map<String, String>
    ): Response<LoginClass>

    @GET("profile/{id}")
    suspend fun getUserProfile(
        @Path("id") id: Int
    ): Response<ProfileClass>
    ///todolist
    // ADD TODO
    @POST("addTodo")
    suspend fun addTodo(
        @Body todo: TodoItem
    ): Response<ApiResponse>


    // GET TODO
    @GET("todos/{user_id}")
    suspend fun getTodoList(
        @Path("user_id") userId: Int
    ): Response<List<TodoItem>>


    // UPDATE TODO
    @PUT("editTodo/{id}")
    suspend fun updateTodo(
        @Path("id") id: Int,
        @Body todo: TodoItem
    ): Response<ApiResponse>


    // DELETE TODO
    @DELETE("deleteTodo/{id}")
    suspend fun deleteTodo(
        @Path("id") id: Int
    ): Response<ApiResponse>


    // GET NOTES
    @GET("allNotes")
    suspend fun retrieveNote(): List<Note>

    @GET("notes/{user_id}")
    suspend fun getNotesByUser(
        @Path("user_id") userId: Int
    ): List<Note>

    @FormUrlEncoded
    @POST("insertNote")
    suspend fun insertNote(
        @Field("user_id") user_id: Int,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("background_color") backgroundColor: String
    ): Note

    // Update note
    @FormUrlEncoded
    @PUT("updateNote/{id}")
    suspend fun updateNote(
        @Path("id") id: Int,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("background_color") backgroundColor: String
    ): Response<Note>

    //    @DELETE("deleteNote/{id}")
//    suspend fun deleteNote(
//        @Path("id") id: Int
//    ): Response<ApiResponse>
    @PUT("deleteNote/{id}")
    suspend fun deleteNote(
        @Path("id") noteId: Int
    ): Response<ApiResponse>

    @PUT("updateFavorite/{id}")
    suspend fun updateFavorite(
        @Path("id") noteId: Int,
        @Query("is_fav") isFav: Int
    ): Response<ApiResponse>

    @PUT("updateColor/{id}")
    suspend fun updateColor(
        @Path("id") noteId: Int,
        @Query("color") color: String
    ): Response<ApiResponse>

    @GET("collections/{user_id}")
    suspend fun getCollections(
        @Path("user_id") userId: Int
    ): List<CollectionClass>

    @FormUrlEncoded
    @POST("addCollection")
    suspend fun addCollection(
        @Field("user_id") user_id: Int,
        @Field("collection_name") collection_name: String,
        @Field("background_color") backgroundColor: String
    ): Note

    @POST("addNoteToCollection")
    suspend fun addNoteToCollection(
        @Body request: NoteCollectionClass
    ): Response<ApiResponse>

    @DELETE("deleteNoteFromCollection")
    suspend fun deleteNoteFromCollection(
        @Query("note_id") noteId: Int,
        @Query("collection_id") collectionId: Int
    ): Response<ApiResponse>

    // โหลดโน้ตจาก collection
    @GET("notesByCollection/{collection_id}")
    suspend fun getNotesByCollection(
        @Path("collection_id") collectionId: Int
    ): Response<List<Note>>

    @DELETE("deleteCollection/{collection_id}")
    suspend fun deleteCollection(
        @Path("collection_id") collectionId: Int
    ): Response<ApiResponse>


    // GET PLANNER
    @GET("allPlanner/{user_id}")
    suspend fun getPlanner(
        @Path("user_id") userId: Int
    ): Response<List<PlannerClass>>



    @POST("planner")
    suspend fun addPlanner(
        @Body planner: PlannerClass
    ): Response<Unit>

    @PUT("planner/{id}")
    suspend fun updatePlanner(
        @Path("id") id: Int,
        @Body planner: PlannerClass
    ): Response<Map<String,String>>

    // soft delete
    @PUT("planner/delete/{id}")
    suspend fun deletePlanner(
        @Path("id") id: Int
    ): Response<Map<String,String>>

    // restore from trash
    @PUT("planner/restore/{id}")
    suspend fun restorePlanner(
        @Path("id") id: Int
    ): Response<Map<String,String>>

    // trash list
    @GET("planner/trash/{user_id}")
    suspend fun getTrashPlanner(
        @Path("user_id") userId: Int
    ): Response<List<PlannerClass>>


    //getTrashNote
    @GET("note/trash/{user_id}")
    suspend fun getTrashNote(
        @Path("user_id") userId: Int
    ): Response<List<Note>>

    //rtstore note
    @PUT("note/restore/{id}")
    suspend fun restoreNote(
        @Path("id") noteId: Int
    ): Response<ApiResponse>

    //ลบเลย
    @DELETE("note/permanent/{note_id}")
    suspend fun deleteNotePermanent(
        @Path("note_id") noteId: Int
    ): Response<ApiResponse>
    @DELETE("planner/permanent/{id}")
    suspend fun deletePlannerPermanent(
        @Path("id") id: Int
    ): Response<Map<String,String>>

    // toggle done
    @PUT("plannerDone/{planner_id}")
    suspend fun plannerDone(
        @Path("planner_id") plannerId: Int
    ): Response<Unit>

    @PUT("user/background/{id}")
    suspend fun updateUserBackground(
        @Path("id") id: Int,
        @Body data: Map<String,String>
    ): Response<ResponseBody>
}




