package com.example.navigationlab.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.location.LocationServices
import com.example.navigationlab.data.location.DefaultLocationRepository
import com.example.navigationlab.ui.location.LocationViewModel
import com.example.navigationlab.ui.location.LocationScreen
import com.example.navigationlab.data.remote.RetrofitClient
import com.example.navigationlab.data.local.DatabaseProvider
import com.example.navigationlab.data.preferences.UserPreferencesRepository
import com.example.navigationlab.data.remote.StudentRemoteDataSource
import com.example.navigationlab.data.repository.DefaultStudentRepository
import com.example.navigationlab.data.repository.FavoriteRepository
import com.example.navigationlab.ui.detail.DetailViewModel
import com.example.navigationlab.ui.home.HomeViewModel
import com.example.navigationlab.ui.favorites.FavoritesViewModel
import com.example.navigationlab.ui.screen.DetailScreen
import com.example.navigationlab.ui.screen.HomeScreen
import com.example.navigationlab.ui.screen.FavoritesScreen

private object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{studentId}"
    const val PROFILE = "profile"
    const val ABOUT = "about"
    const val FAVORITES = "favorites"
    const val LOCATION = "location"
    fun detail(studentId: Int) = "detail/$studentId"
}

@Composable
fun NavigationLabApp() {
    val remoteDataSource = remember { StudentRemoteDataSource(RetrofitClient.apiService) }
    val repository = remember { DefaultStudentRepository(remoteDataSource) }
    val context = LocalContext.current
    val preferencesRepository = remember { UserPreferencesRepository(context.applicationContext) }
    val favoriteRepository = remember {
        FavoriteRepository(DatabaseProvider.getDatabase(context).favoriteStudentDao())
    }
    val locationRepository = remember {
        val appContext = context.applicationContext
        DefaultLocationRepository(
            context = appContext,
            client = LocationServices.getFusedLocationProviderClient(appContext)
        )
    }
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory(repository, preferencesRepository))
            val uiState = homeViewModel.uiState.collectAsStateWithLifecycle().value
            val searchQuery = homeViewModel.searchQuery.collectAsStateWithLifecycle().value

            HomeScreen(
                uiState = uiState,
                searchQuery = searchQuery,
                onStudentClick = { id -> navController.navigate(Routes.detail(id)) },
                onProfileClick = { navController.navigate(Routes.PROFILE) },
                onAboutClick = { navController.navigate(Routes.ABOUT) },
                onFavoritesClick = { navController.navigate(Routes.FAVORITES) },
                onLocationClick = { navController.navigate(Routes.LOCATION) },
                onRetry = homeViewModel::retry,
                onRefresh = homeViewModel::refresh,
                onSearchQueryChange = homeViewModel::onSearchQueryChange
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: -1
            val detailViewModel: DetailViewModel = viewModel(
                key = "detail-$studentId",
                factory = DetailViewModel.factory(repository, favoriteRepository)
            )
            LaunchedEffect(studentId) { detailViewModel.loadStudent(studentId) }
            val uiState = detailViewModel.uiState.collectAsStateWithLifecycle().value
            DetailScreen(
                uiState = uiState,
                onBack = { navController.popBackStack() },
                onRetry = { detailViewModel.loadStudent(studentId) },
                onToggleFavorite = detailViewModel::toggleFavorite
            )
        }

        composable(Routes.FAVORITES) {
            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModel.factory(favoriteRepository, preferencesRepository)
            )
            val favoritesState = favoritesViewModel.uiState.collectAsStateWithLifecycle().value
            FavoritesScreen(
                uiState = favoritesState,
                onStudentClick = { id -> navController.navigate(Routes.detail(id)) },
                onToggleSort = favoritesViewModel::toggleSortOrder,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.LOCATION) {
            val locationViewModel: LocationViewModel = viewModel(
                factory = LocationViewModel.factory(locationRepository)
            )
            LocationScreen(
                viewModel = locationViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.PROFILE) {
            SimplePage("Profile", "Halaman Profile", onBack = { navController.popBackStack() })
        }
        composable(Routes.ABOUT) {
            SimplePage(
                "About",
                "NavigationLab - Praktikum Pemrograman Aplikasi Bergerak Modul 08.",
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun SimplePage(title: String, description: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Text(description)
        Button(onClick = onBack) { Text("Kembali") }
    }
}
