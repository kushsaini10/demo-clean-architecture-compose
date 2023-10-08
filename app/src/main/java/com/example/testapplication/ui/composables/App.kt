package com.example.testapplication.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testapplication.navigation.Routes
import com.example.testapplication.ui.activities.MainActivity
import com.example.testapplication.ui.theme.TestApplicationTheme
import com.example.testapplication.viewmodels.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainActivity.App(
    appViewModel: AppViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {

    val homeUiState by appViewModel.uiState.collectAsState()

    TestApplicationTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { paddingValues ->

            NavHost(
                navController = navController,
                startDestination = Routes.ListScreen,
                modifier = Modifier.padding(paddingValues)
            ) {

                composable(route = Routes.ListScreen) {
                    ListScreen(homeUiState, navController)
                }

                composable(
                    route = "${Routes.DetailScreen}/{id}",
                    arguments = listOf(navArgument("id") {
                        type = NavType.IntType
                    })
                ) {

                    it.arguments?.getInt("id")?.let { id ->
                        DetailScreen(
                            itemId = id,
                            appViewModel = appViewModel,
                            navController = navController
                        )
                    }
                }
            }

        }
    }
}

