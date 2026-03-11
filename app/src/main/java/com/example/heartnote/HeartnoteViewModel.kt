package com.example.heartnote

import android.R.attr.background
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeartnoteViewModel : ViewModel() {
    private var _loginResult by mutableStateOf<LoginClass?>(null)
    val loginResult get() = _loginResult

    private var _userProfile by mutableStateOf<ProfileClass?>(null)
    val userProfile get() = _userProfile

    private var _errorMessage by mutableStateOf("")
    val errorMessage get() = _errorMessage
    //เพิ่มlist
// TodoList
    var todoList by mutableStateOf<List<TodoItem>>(emptyList())

    var noteList = mutableStateListOf<Note>()
        private set


    var plannerId by mutableStateOf(0)
    var startDateText by mutableStateOf("")
    var endDateText by mutableStateOf("")
    var remindTimeText by mutableStateOf("")
    fun resetLoginResult() {
        _loginResult = null
    }
    var currentUserId by mutableStateOf(0)
    var plannerList by mutableStateOf<List<PlannerClass>>(emptyList())

    var noteIdText by mutableStateOf(0)
    var titleText by mutableStateOf("")
    var contentText by mutableStateOf("")

    var backgroundColorText by mutableStateOf("#FF6B8A")
    var background by mutableStateOf("bg1")

    fun setNoteForEdit(note: Note) {
        noteIdText = note.note_id
        titleText = note.title
        contentText = note.content
        backgroundColorText = note.background_color
    }

    var collectionList = mutableStateListOf<CollectionClass>()
        private set

    var notesInCollection by mutableStateOf<List<Note>>(emptyList())
        private set


    // Login Function
    fun login(username: String, user_password: String) {
        viewModelScope.launch {
            try {
                val loginData = mapOf(
                    "username" to username,
                    "user_password" to user_password
                )

                val response = HeartnoteClient.heartnoteAPI.loginUser(loginData)

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body?.error == false) {
                        _loginResult = body
                        _errorMessage = ""
                    } else {
                        _errorMessage = "Login failed"
                    }

                } else {
                    _errorMessage = "Login failed"
                }

            } catch (e: Exception) {
                _errorMessage = "เชื่อมต่อเซิร์ฟเวอร์ไม่ได้"
            }
        }
    }

    fun getProfile(id: Int) {
        viewModelScope.launch {
            try {
                val response = HeartnoteClient.heartnoteAPI.getUserProfile(id)
                if (response.isSuccessful) {
                    _userProfile = response.body()
                } else {
                    _errorMessage = "User data not found"
                }
            } catch (e: Exception) {
                _errorMessage = "Error: ${e.message}"
            }
        }
    }

    fun getAllNote() {
        viewModelScope.launch {
            try {
                val response = HeartnoteClient.heartnoteAPI.retrieveNote()

                Log.d("NOTE_API", response.toString())

                noteList.clear()
                noteList.addAll(response)

            } catch (e: Exception) {
                Log.e("HeartnoteViewModel", "Get Error: ${e.message}")
            }
        }
    }

    fun getCollections(userId: Int) {

        viewModelScope.launch {

            try {

                val response =
                    HeartnoteClient.heartnoteAPI.getCollections(userId)

                collectionList.clear()
                collectionList.addAll(response)

            } catch (e: Exception) {

                Log.e("COLLECTION", e.message.toString())

            }

        }

    }

    fun insertNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {

                HeartnoteClient.heartnoteAPI.insertNote(
                    user_id = note.user_id,
                    title = note.title,
                    content = note.content,
                    backgroundColor = note.background_color
                )

                getAllNote()
                onSuccess()

            } catch (e: Exception) {
                Log.e("INSERT_NOTE", "Error: ${e.message}")
            }
        }
    }

    fun addCollection(collection: CollectionClass, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {

                HeartnoteClient.heartnoteAPI.addCollection(
                    user_id = collection.user_id,
                    collection_name = collection.collection_name,
                    backgroundColor = collection.background_color
                )

                getCollections(collection.user_id)
                onSuccess()

            } catch (e: Exception) {
                Log.e("INSERT_COLLECTION", "Error: ${e.message}")
            }
        }
    }

    fun getNotesByCollection(collectionId: Int) {
        viewModelScope.launch {
            try {

                val res = HeartnoteClient.heartnoteAPI.getNotesByCollection(collectionId)

                if (res.isSuccessful) {
                    notesInCollection = res.body() ?: emptyList()
                }

            } catch (e: Exception) {
                _errorMessage = e.message.toString()
            }
        }
    }

    fun addNoteToCollection(noteId: Int, collectionId: Int) {

        viewModelScope.launch {

            try {

                val request = NoteCollectionClass(
                    note_id = noteId,
                    collection_id = collectionId
                )

                val res = HeartnoteClient.heartnoteAPI.addNoteToCollection(request)

                if(res.isSuccessful){
                    getNotesByCollection(collectionId)
                }

            } catch (e: Exception) {
                Log.e("API", "Error: ${e.message}")
            }

        }
    }

    fun deleteCollection(collectionId: Int, userId: Int) {

        viewModelScope.launch {

            try {

                val response =
                    HeartnoteClient.heartnoteAPI.deleteCollection(collectionId)

                if (response.isSuccessful) {

                    Log.d("COLLECTION_DELETE", "Success")

                    // โหลด collection ใหม่
                    getCollections(userId)

                } else {

                    Log.e(
                        "COLLECTION_DELETE",
                        "Fail = ${response.errorBody()?.string()}"
                    )

                }

            } catch (e: Exception) {

                Log.e("COLLECTION_DELETE", e.message.toString())

            }

        }

    }

    fun deleteNoteFromCollection(noteId: Int, collectionId: Int) {
        viewModelScope.launch {
            try {

                val res = HeartnoteClient.heartnoteAPI
                    .deleteNoteFromCollection(noteId, collectionId)

                if (res.isSuccessful) {
                    getNotesByCollection(collectionId)
                }

            } catch (e: Exception) {
                _errorMessage = e.message.toString()
            }
        }
    }

    // UPDATE NOTE
    fun updateNote(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {

            try {

                val response = HeartnoteClient.heartnoteAPI.updateNote(
                    id = noteIdText,
                    title = titleText,
                    content = contentText,
                    backgroundColor = backgroundColorText
                )

                if (response.isSuccessful) {

                    Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show()

                    onSuccess()

                    getAllNote()

                } else {

                    Toast.makeText(
                        context,
                        "Server Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            } catch (e: Exception) {

                Log.e("VM", e.message.toString())

                Toast.makeText(
                    context,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }
    }

    // DELETE NOTE
    fun deleteNote(context: Context, noteId: Int, onSuccess: () -> Unit) {

        viewModelScope.launch {

            try {

                val response = HeartnoteClient.heartnoteAPI.deleteNote(noteId)

                if (response.isSuccessful) {

                    val apiResponse = response.body()

                    if (apiResponse != null && apiResponse.error == false) {

                        Toast.makeText(
                            context,
                            "Deleted: ${apiResponse.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                        onSuccess()

                        getAllNote()

                    } else {

                        Toast.makeText(
                            context,
                            "Delete Failed: ${apiResponse?.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                } else {

                    Toast.makeText(
                        context,
                        "Server Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            } catch (e: Exception) {

                Log.e("HeartnoteViewModel", "Delete Error: ${e.message}")

                Toast.makeText(
                    context,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }
    }

    fun updateFavorite(noteId: Int, isFav: Int) {
        viewModelScope.launch {
            try {

                val response = HeartnoteClient.heartnoteAPI.updateFavorite(
                    noteId,
                    isFav
                )
                Log.d("FAV", response.toString())
            } catch (e: Exception) {
                Log.e("FAV_ERROR", e.message.toString())
            }
        }
    }

    fun starFavorite(note: Note) {
        val newValue = if (note.is_fav == 1) 0 else 1
        val index = noteList.indexOfFirst { it.note_id == note.note_id }
        if (index != -1) {
            noteList[index] = noteList[index].copy(is_fav = newValue)
        }
        updateFavorite(note.note_id, newValue)
    }


    fun updateColor(noteId: Int, color: String) {
        viewModelScope.launch {
            try {

                HeartnoteClient.heartnoteAPI.updateColor(
                    noteId,
                    color
                )
            } catch (e: Exception) {
                Log.e("COLOR_ERROR", e.message.toString())
            }
        }
    }

    // Register Function
    fun register(
        context: Context, user: RegisterClass,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = HeartnoteClient.heartnoteAPI.registerUser(user)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorRawString = response.errorBody()?.string()
                    val finalMessage = if (!errorRawString.isNullOrEmpty()) {
                        try {
                            val errorData = Gson().fromJson(
                                errorRawString,
                                RegisterResponse::class.java
                            )
                            errorData.message
                        } catch (e: Exception) {
                            errorRawString
                        }
                    } else {
                        response.message()
                    }
                    _errorMessage = finalMessage.toString()
                    Toast.makeText(
                        context, _errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                _errorMessage = "Network Error: ${e.message}"
                Toast.makeText(
                    context, _errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //todolist
    fun fetchTasks(userId: Int) {

        viewModelScope.launch {

            try {

                val response = HeartnoteClient.heartnoteAPI.getTodoList(userId)

                if (response.isSuccessful) {

                    todoList = response.body() ?: emptyList()

                } else {

                    _errorMessage = "Load todo failed"

                }

            } catch (e: Exception) {

                _errorMessage = e.message.toString()

            }

        }

    }


    // เพิ่มTodo
    fun addTodo(todo: TodoItem) {

        viewModelScope.launch {

            try {

                val response = HeartnoteClient.heartnoteAPI.addTodo(todo)

                if (response.isSuccessful) {

                    // โหลด list ใหม่
                    fetchTasks(todo.userId)


                } else {

                    _errorMessage = "Add todo failed"

                }

            } catch (e: Exception) {

                _errorMessage = e.message.toString()

            }

        }

    }


    // แก้ไขTodo
    fun updateTodo(id: Int, todo: TodoItem) {

        viewModelScope.launch {

            try {

                val response = HeartnoteClient.heartnoteAPI.updateTodo(id, todo)

                if (response.isSuccessful) {

                    fetchTasks(todo.userId)


                } else {

                    _errorMessage = "Update failed"

                }

            } catch (e: Exception) {

                _errorMessage = e.message.toString()

            }

        }

    }


    // ลบTodo
    fun deleteTodo(todo: TodoItem) {

        viewModelScope.launch {

            try {
                    val response = HeartnoteClient.heartnoteAPI.deleteTodo(
                        todo.todoListId
                        )


                if (response.isSuccessful) {
                    fetchTasks(todo.userId)
                }

            } catch (e: Exception) {
                _errorMessage = e.message.toString()
            }

        }

    }
    fun getNotesByUser(userId: Int) {

        viewModelScope.launch {

            try {

                val response = HeartnoteClient.heartnoteAPI.getNotesByUser(userId)

                noteList.clear()
                noteList.addAll(response)

            } catch (e: Exception) {

                Log.e("API", "error ${e.message}")

            }

        }

    }
    fun getPlanner(userId: Int) {

        viewModelScope.launch {

            try {

                val response = HeartnoteClient.heartnoteAPI.getPlanner(userId)

                if (response.isSuccessful) {

                    plannerList = response.body() ?: emptyList()

                    Log.d("planner", plannerList.toString())
                }

            } catch (e: Exception) {

                Log.e("planner", e.message.toString())

            }

        }
    }

    fun addPlanner(planner: PlannerClass) {
        viewModelScope.launch {
            try {

                val response = HeartnoteClient.heartnoteAPI.addPlanner(planner)

                if (response.isSuccessful) {

                    Log.d("API_DEBUG","ADD SUCCESS")

                    getPlanner(planner.user_id)

                } else {

                    Log.e("API_DEBUG","ADD FAIL = ${response.errorBody()?.string()}")

                }

            } catch (e: Exception) {

                Log.e("API_DEBUG","ERROR = ${e.message}")

            }
        }
    }

    fun plannerDone(plannerId: Int) {

        viewModelScope.launch {

            try {

                HeartnoteClient.heartnoteAPI.plannerDone(plannerId)

                getPlanner(currentUserId) // refresh list

            } catch (e: Exception) {

                Log.e("planner", e.message.toString())

            }

        }

    }


    fun updatePlanner(planner: PlannerClass) {

        viewModelScope.launch {

            try {

                val response = HeartnoteClient.heartnoteAPI.updatePlanner(
                    planner.planner_id,
                    planner
                )

                if (response.isSuccessful) {
                    getPlanner(planner.user_id)
                } else {
                    _errorMessage = "Update planner failed"
                }

            } catch (e: Exception) {
                _errorMessage = e.message.toString()
            }

        }
    }

    fun deletePlanner(id: Int, userId: Int) {

        viewModelScope.launch {

            try {

                val response = HeartnoteClient.heartnoteAPI.deletePlanner(id)

                if (response.isSuccessful) {
                    getPlanner(userId)
                } else {
                    _errorMessage = "Delete planner failed"
                }

            } catch (e: Exception) {
                _errorMessage = e.message.toString()
            }

        }

    }

    fun setPlannerForEdit(planner: PlannerClass){

        android.util.Log.d("API_DEBUG","start = ${planner.event_start}")
        android.util.Log.d("API_DEBUG","end = ${planner.event_end}")
        android.util.Log.d("API_DEBUG","remind = ${planner.remind}")
        plannerId = planner.planner_id
        contentText = planner.planner_content ?: ""

        startDateText = planner.event_start
            ?.substringBefore("T")
            ?: "2024-01-01"

        endDateText = planner.event_end
            ?.substringBefore("T")
            ?: "2024-01-01"

        remindTimeText = planner.remind
            ?.substringAfter("T")
            ?.substringBefore(".")
            ?.take(5)
            ?: "00:00"
    }

    fun restorePlanner(id: Int, userId: Int) {

        viewModelScope.launch {

            try {

                val response = HeartnoteClient.heartnoteAPI.restorePlanner(id)

                if (response.isSuccessful) {
                    getPlanner(userId)
                } else {
                    _errorMessage = "Restore planner failed"
                }

            } catch (e: Exception) {
                _errorMessage = e.message.toString()
            }

        }

    }

    fun plannerDone(id: Int, userId: Int) {

        viewModelScope.launch {

            try {

                val response =
                    HeartnoteClient.heartnoteAPI.plannerDone(id)

                if (response.isSuccessful) {
                    getPlanner(userId)
                }

            } catch (e: Exception) {
                _errorMessage = e.message.toString()
            }

        }

    }


    fun updateBackground(){

        viewModelScope.launch {

            try{

                val data = mapOf(
                    "background" to background
                )

                val response =
                    HeartnoteClient.heartnoteAPI.updateUserBackground(
                        currentUserId,
                        data
                    )

                if(response.isSuccessful){
                    Log.d("BG_UPDATE","SUCCESS")
                }else{
                    Log.e("BG_UPDATE","FAIL = ${response.errorBody()?.string()}")
                }

            }catch(e:Exception){
                Log.e("BG_UPDATE","ERROR = ${e.message}")
            }

        }
    }

    var trashPlannerList by mutableStateOf<List<PlannerClass>>(emptyList())
    fun getTrashPlanner(userId: Int) {

        viewModelScope.launch {

            try {

                val response = HeartnoteClient.heartnoteAPI.getTrashPlanner(userId)

                if (response.isSuccessful) {

                    trashPlannerList = response.body() ?: emptyList()

                }

            } catch (e: Exception) {

                Log.e("trash", e.message.toString())

            }

        }

    }

    fun deletePlannerPermanent(id: Int, userId: Int) {

        viewModelScope.launch {

            try {

                val response =
                    HeartnoteClient.heartnoteAPI.deletePlannerPermanent(id)

                if (response.isSuccessful) {

                    getTrashPlanner(userId)

                }

            } catch (e: Exception) {

                _errorMessage = e.message.toString()

            }

        }

    }








    var trashNoteList by mutableStateOf<List<Note>>(emptyList())
    fun getTrashNote(userId: Int) {

        viewModelScope.launch {

            try {

                val response = HeartnoteClient.heartnoteAPI.getTrashNote(userId)
                Log.d("TRASH_NOTE", response.body().toString())


                if (response.isSuccessful) {

                    trashNoteList = response.body() ?: emptyList()

                }

            } catch (e: Exception) {

                Log.e("NOTE_TRASH", e.message.toString())

            }

        }

    }


    fun restoreNote(noteId: Int, userId: Int) {

        viewModelScope.launch {

            try {

                val response =
                    HeartnoteClient.heartnoteAPI.restoreNote(noteId)

                if (response.isSuccessful) {

                    getTrashNote(userId)
                    getNotesByUser(userId)

                }

            } catch (e: Exception) {

                Log.e("NOTE_RESTORE", e.message.toString())

            }

        }

    }


    fun deleteNotePermanent(noteId: Int, userId: Int) {

        viewModelScope.launch {

            try {

                val response =
                    HeartnoteClient.heartnoteAPI.deleteNotePermanent(noteId)

                if (response.isSuccessful) {

                    getTrashNote(userId)

                }

            } catch (e: Exception) {

                Log.e("NOTE_DELETE_PERMANENT", e.message.toString())

            }

        }

    }





    }