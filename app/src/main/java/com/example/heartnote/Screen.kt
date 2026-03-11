package com.example.heartnote

sealed class Screen(val route: String, val name: String) {

    data object Home : Screen("home_screen", "Home")
    data object Login : Screen("login_screen", "Login")
    data object Register : Screen("register_screen", "Register")
    data object Profile : Screen("profile_screen", "Profile")
    data object Note : Screen("note_screen", "Note")

    data object Insert : Screen(route = "insert_screen", name = "Insert")

    data object EditNote : Screen(route = "edit_note_screen", name = "EditNote")

    data object Collection : Screen(route = "collection_screen", name = "CollectionNote")

    data object Calender : Screen(route = "calender_screen", name = "Calender")

    data object AddCollection : Screen(route = "add_collection_screen", name = "AddCollectionNote")

    data object Todolist : Screen("todolist_screen","Todolist")
    data object Addtodolist : Screen("addtodolist_screen","Addtodolist")

    data object Detaillist : Screen("detaillist","Detaillist")

    data object SearchScreen : Screen("searchscreen","SearchScreen")

    data object Planner : Screen("planner_screen","Planner")
    data object AddPlanner : Screen("AddPlanner_screen","AddPlanner")

    data object EditPlanner: Screen(route = "EditPlanner_screen", name = "EditPlanner")
    data object SelectBG : Screen("SekectBg_screen","SelectBG")

    data object AddNoteCollection :
        Screen("add_note_collection_screen/{collectionId}", "AddNoteCollection") {

        fun createRoute(collectionId: Int) =
            "add_note_collection_screen/$collectionId"
    }

    data object CollectionDetail :
        Screen("collection_detail_screen/{collectionId}", "CollectionDetailScreen") {

        fun createRoute(collectionId: Int) =
            "collection_detail_screen/$collectionId"
    }
    data object ThemeFont : Screen("Themefont_screen","themefont")
    data object Trash : Screen("Trash_screen","Trash")

    data object Favorite : Screen("favorite_screen","Favorite")

    data object About : Screen("About_screen","About")


}