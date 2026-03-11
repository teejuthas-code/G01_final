package com.example.heartnote

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todolist.ui.AddTaskScreen
import androidx.compose.ui.platform.LocalContext
@Composable
fun NavGraph(navController: NavHostController, viewModel: HeartnoteViewModel) {
    val context = LocalContext.current
    val sharedPref = SharedPreferencesManager(context)
    val userId = sharedPref.getSavedUserId()


    NavHost(
        navController = navController,
        startDestination = Screen.Home.route // กำหนดหน้าแรกเป็น Login

    ) {

        // 1. หน้า Login
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        // 1. หน้า Login
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController, viewModel = viewModel)
        }

        // 2. หน้า Register
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController, viewModel = viewModel)
        }

        // 3. หน้า Profile
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController, viewModel = viewModel)
        }



        composable(route = Screen.Note.route) {
            NoteScreen(navController = navController, viewModel = viewModel)
        }

        //5.หน้า todolist
        composable(Screen.Todolist.route) {
            TodoListScreen(navController = navController, viewModel = viewModel)
        }

        // 5. หน้าจอเพิ่มโน๊ต (Insert)
        composable(route = Screen.Insert.route) {
            InsertNoteScreen(navController, viewModel = viewModel)
        }

        // 9. หน้า แก้โน๊ต
        composable(route = Screen.EditNote.route) {
            EditNoteScreen(navController, viewModel = viewModel)
        }

        // 9. หน้า collection
        composable(route = Screen.Collection.route) {
            CollectionScreen(navController, viewModel = viewModel)
        }

        // 9. หน้า add collection
        composable(route = Screen.AddCollection.route) {
            AddCollectionScreen(navController, viewModel = viewModel)
        }

        composable(Screen.Addtodolist.route) {
            AddTaskScreen(
                navController = navController,
                viewModel = viewModel,
                userId = userId
            )
        }
        composable(
            "detaillist/{id}"
        ){ backStackEntry ->

            val id = backStackEntry.arguments?.getString("id")!!.toInt()

            EditTodoScreen(
                todoId = id,
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(Screen.SearchScreen.route) {

            SearchScreen(
                navController = navController,
                viewModel = viewModel
            )

        }

        composable(route = Screen.Planner.route) {
            PlannerScreen(navController = navController, viewModel = viewModel)
        }

        composable(route = Screen.Favorite.route) {
            FavoriteScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = Screen.AddPlanner.route + "/{date}"
        ) { backStackEntry ->

            val date = backStackEntry.arguments?.getString("date") ?: ""

            Addplanner(
                navController = navController,
                viewModel = viewModel,
                date = date
            )

        }
        composable(
            route = Screen.EditPlanner.route
        ) {
            Editplanner(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(route = Screen.SelectBG.route) {
            SelectBackground(navController = navController, viewModel = viewModel)
        }

        composable(Screen.CollectionDetail.route) { backStackEntry ->

            val collectionId =
                backStackEntry.arguments?.getString("collectionId")?.toInt() ?: 0

            CollectionDetailScreen(
                navController = navController,
                viewModel = viewModel,
                collectionId = collectionId
            )
        }

        composable(Screen.AddNoteCollection.route) { backStackEntry ->

            val collectionId =
                backStackEntry.arguments?.getString("collectionId")?.toInt() ?: 0

            AddNoteCollectionScreen(
                navController = navController,
                viewModel = viewModel,
                collectionId = collectionId
            )
        }
        composable(Screen.ThemeFont.route) {
            ThemeFontScreen(navController)
        }

        composable(Screen.Trash.route) {
            Trashscreen(navController = navController, viewModel = viewModel)
        }

        composable(Screen.About.route) {
            Aboutscreen(navController = navController, viewModel = viewModel)
        }


    }


    }
