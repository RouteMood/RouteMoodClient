package ru.hse.routemoodclient.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.hse.routemood.ApiCallback
import ru.hse.routemood.Controller
import ru.hse.routemood.dto.AuthRequest
import ru.hse.routemood.dto.AuthResponse
import ru.hse.routemood.dto.GptRequest
import ru.hse.routemood.dto.RateRequest
import ru.hse.routemood.dto.RatingRequest
import ru.hse.routemood.dto.RatingResponse
import ru.hse.routemood.dto.RegisterRequest
import ru.hse.routemood.models.Route
import ru.hse.routemood.models.Route.RouteItem
import ru.hse.routemoodclient.data.DataRepository
import ru.hse.routemoodclient.data.RouteUiState
import ru.hse.routemoodclient.data.UserState
import java.util.UUID
import javax.inject.Inject
import java.io.File
import java.io.FileOutputStream

sealed interface UserUiState {
    data class Success(val userState: StateFlow<UserState>) : UserUiState
    data class Error(val error: String?) : UserUiState
    object Loading : UserUiState
}

data class PublishedRoute (
    val id : UUID = UUID(0, 0),
    val name: String = "default route",
    val description: String = "default",
    val rating: Double = 0.0,
    val authorUsername: String = "",
    val route: List<LatLng> = listOf()
)

/**
 * [ServerViewModel] holds information about a route settings in terms of length, time.
 */
@HiltViewModel
class ServerViewModel @Inject constructor(
    private val dataRepository: DataRepository,
) : ViewModel() {
    private var controller = Controller()

    fun copyAssetToFile(context: Context, assetFileName: String): File {
        val file = File(context.cacheDir, assetFileName)
        if (!file.exists()) {
            context.assets.open(assetFileName).use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }
        return file
    }

    /**
     * User State
     */
    val userState: StateFlow<UserState> = dataRepository.userState
    /**
     * User Ui state that stores the status of the most recent request
     */
    var userUiState: UserUiState by mutableStateOf(UserUiState.Success(userState))
        private set
    /**
     * Route state
     */
    val routeState: StateFlow<RouteUiState> = dataRepository.routeState
    /**
     * Operation's state
     */
    var isRefreshing by mutableStateOf(false)
        private set


    /**
     * User's published routes
     */
    private val _publishedRoutesState = MutableStateFlow(listOf<PublishedRoute>())
    val publishedRoutesState: StateFlow<List<PublishedRoute>> = _publishedRoutesState.asStateFlow()
    /**
     * Routes from Network
     */
    private val _networkRoutesState = MutableStateFlow(listOf<PublishedRoute>())
    val networkRoutesState: StateFlow<List<PublishedRoute>> = _networkRoutesState.asStateFlow()

    fun saveUsername(
        username: String
    ) {
        val updatedUserState = userState.value.copy(username = username)
        dataRepository.updateUserState(updatedUserState)
    }
    fun saveUserLogin(
        login: String
    ) {
        val updatedUserState = userState.value.copy(login = login)
        dataRepository.updateUserState(updatedUserState)
    }
    fun saveUserPassword(
        password: String
    ) {
        val updatedUserState = userState.value.copy(password = password)
        dataRepository.updateUserState(updatedUserState)
    }
    fun resetUser() {
        dataRepository.updateUserState(UserState())
    }

    /**
     * Ask login user from server
     */
    fun askLoginUser() {
        dataRepository.setLoading(true);

        userUiState = UserUiState.Loading
        val callback = object : ApiCallback<AuthResponse> {
            override fun onSuccess(result: AuthResponse?) {
                dataRepository.setLoading(false)
                if (result != null) {
                    val updatedUserState = userState.value.copy(token = result.token)
                    dataRepository.updateUserState(updatedUserState)
                    UserUiState.Success(userState)
                } else {
                    UserUiState.Error("Connection failed")
                }
            }
            override fun onError(error: String?) {
                dataRepository.setLoading(false)
                UserUiState.Error(error)
            }
        }
        try {
            controller.loginUser(
                AuthRequest(userState.value.username, userState.value.password),
                callback
            )
        } catch (error : Exception) {
            UserUiState.Error(error.message)
        }

    }

    /**
     * Ask register user from server
     */
    fun askRegisterUser() {
        userUiState = UserUiState.Loading
        controller.registerUser(
            RegisterRequest(userState.value.username, userState.value.login, userState.value.password),
            object : ApiCallback<AuthResponse> {
                override fun onSuccess(result: AuthResponse?) {
                    if (result != null) {
                        val updatedUserState = userState.value.copy(token = result.token)
                        dataRepository.updateUserState(updatedUserState)
                        UserUiState.Success(userState)
                    }
                }
                override fun onError(error: String?) {
                    UserUiState.Error(error)
                }
            }
        )
    }

    /**
     * Ask fictive route from server
     */
    fun askFictiveRoute() {
        controller.loginUser(
            AuthRequest(userState.value.username, userState.value.password),
            object : ApiCallback<AuthResponse?> {
                override fun onSuccess(result: AuthResponse?) {
                    controller.getFictiveRoute(object : ApiCallback<Route?> {
                        override fun onSuccess(result: Route?) {
                            val updatedRouteState = routeState.value.copy(route = latlngList(result))
                            dataRepository.updateRouteState(updatedRouteState)
                        }

                        override fun onError(error: String) {
                            System.err.println(error)
                        }
                    })
                }

                override fun onError(error: String) {
                    System.err.println(error)
                }
            })
    }

    /**
     * Ask route from server
     */
    fun askRoute() {
        dataRepository.setLoading(true);
        val getRouteResponse = object : ApiCallback<Route?> {
            override fun onSuccess(result: Route?) {
                val updatedRouteState = routeState.value.copy(route = latlngList(result))
                dataRepository.updateRouteState(updatedRouteState)
//                setLoading(false);
            }

            override fun onError(error: String) {
                System.err.println(error)
//                setLoading(false);
            }
        }
        val getResponse = object : ApiCallback<AuthResponse?> {
            override fun onSuccess(result: AuthResponse?) {
                try {
                    controller.getRoute(
                        GptRequest(
                            routeState.value.routeRequest,
                            routeState.value.start.latitude,
                            routeState.value.start.longitude,
                        ), getRouteResponse
                    )
                } catch (ex: Exception) {
                    // TODO
                }
                dataRepository.setLoading(false);
            }

            override fun onError(error: String) {
                System.err.println(error)
                dataRepository.setLoading(false);
            }
        }

        try {
            controller.loginUser(
                AuthRequest(userState.value.username, userState.value.password),
                getResponse
            )
        } catch (ex: Exception) {
            // TODO
        }
    }

    /**
     * Ask save route on server
     */
    fun askSaveRoute() {
        val saveRouteResponse = object : ApiCallback<RatingResponse?> {
            override fun onSuccess(result: RatingResponse?) {
                if (result != null) {
                    val convertedRoute = latlngList(result.route)
                    val newRoute = PublishedRoute(
                        id = result.id,
                        name = result.name,
                        description = result.description,
                        rating = result.rating,
                        authorUsername = result.authorUsername,
                        route = convertedRoute
                        )
                    _publishedRoutesState.value += newRoute
                }
            }
            override fun onError(error: String) {
                System.err.println(error)
            }
        }
        val saveResponse = object : ApiCallback<AuthResponse?> {
            override fun onSuccess(result: AuthResponse?) {
                try {
                    val routeItemList: List<RouteItem> = routeState.value.route.map {
                        latLng -> RouteItem(latLng.latitude, latLng.longitude)
                    }
                    val convertedRoute : Route = Route.builder().route(routeItemList).build()
                    controller.saveRoute(
                        RatingRequest(
                            routeState.value.name,
                            routeState.value.routeRequest,
                            userState.value.username,
                            convertedRoute,
                        ), saveRouteResponse
                    )
                } catch (ex: Exception) {
                    // TODO
                }

            }
            override fun onError(error: String) {
                System.err.println(error)
            }
        }

        try {
            controller.loginUser(
                AuthRequest(userState.value.username, userState.value.password),
                saveResponse
            )
        } catch (ex: Exception) {
            // TODO
        }
    }

    /**
     * Ask add route's rate on server
     */
    fun askAddRate(routeId: UUID, rating: Int) {
        val addRateResponse = object : ApiCallback<RatingResponse?> {
            override fun onSuccess(result: RatingResponse?) {
                // TODO
            }
            override fun onError(error: String) {
                System.err.println(error)
            }
        }
        val saveResponse = object : ApiCallback<AuthResponse?> {
            override fun onSuccess(result: AuthResponse?) {
                try {
                    controller.addRate(
                        RateRequest(
                            routeId,
                            userState.value.username,
                            rating
                        ), addRateResponse
                    )
                } catch (ex: Exception) {
                    // TODO
                }

            }

            override fun onError(error: String) {
                System.err.println(error)
            }
        }

        try {
            controller.loginUser(
                AuthRequest(userState.value.username, userState.value.password),
                saveResponse
            )
        } catch (ex: Exception) {
            // TODO
        }
    }

    /**
     * Ask delete route from server
     */
    fun askDeleteRoute(routeId: UUID) {
        val addRateResponse = object : ApiCallback<Void?> {
            override fun onSuccess(result: Void?) {
                // TODO
            }
            override fun onError(error: String) {
                System.err.println(error)
            }
        }
        val saveResponse = object : ApiCallback<AuthResponse?> {
            override fun onSuccess(result: AuthResponse?) {
                try {
                    controller.deleteRoute(
                        routeId,
                        addRateResponse
                    )
                } catch (ex: Exception) {
                    // TODO
                }

            }

            override fun onError(error: String) {
                System.err.println(error)
            }
        }

        try {
            controller.loginUser(
                AuthRequest(userState.value.username, userState.value.password),
                saveResponse
            )
        } catch (ex: Exception) {
            // TODO
        }
    }

    /**
     * Ask update user's published route list
     */
    fun askUserRoutes() {
        isRefreshing = true
        val rateListResponse = object : ApiCallback<List<RatingResponse>?> {
            override fun onSuccess(result: List<RatingResponse>?) {
                if (result != null) {
                    _publishedRoutesState.value = result.map { route ->
                        PublishedRoute(
                            id = route.id,
                            name = route.name,
                            description = route.description,
                            rating = route.rating,
                            authorUsername = route.authorUsername,
                            route = latlngList(route.route)
                        )
                    }
                }
                isRefreshing = false
            }
            override fun onError(error: String) {
                System.err.println(error)
                isRefreshing = false
            }
        }
        val updateResponse = object : ApiCallback<AuthResponse?> {
            override fun onSuccess(result: AuthResponse?) {
                try {
                    controller.getListRatedRoutesByAuthorUsername(
                        userState.value.username,
                        rateListResponse
                    )
                } catch (ex: Exception) {
                    // TODO
                }

            }
            override fun onError(error: String) {
                System.err.println(error)
                isRefreshing = false
            }
        }

        try {
            controller.loginUser(
                AuthRequest(userState.value.username, userState.value.password),
                updateResponse
            )
        } catch (ex: Exception) {
            // TODO
        }
    }

    /**
     * Ask update on rating route list
     */
    fun askListRoutes() {
        isRefreshing = true
        val rateListResponse = object : ApiCallback<List<RatingResponse>?> {
            override fun onSuccess(result: List<RatingResponse>?) {
                if (result != null) {
                    _networkRoutesState.value = result.map { route ->
                        PublishedRoute(
                            id = route.id,
                            name = route.name,
                            description = route.description,
                            rating = route.rating,
                            authorUsername = route.authorUsername,
                            route = latlngList(route.route)
                        )
                    }
                }
                isRefreshing = false
            }
            override fun onError(error: String) {
                isRefreshing = false
                System.err.println(error)
            }
        }
        val updateResponse = object : ApiCallback<AuthResponse?> {
            override fun onSuccess(result: AuthResponse?) {
                try {
                    controller.listRoutes(rateListResponse)
                } catch (ex: Exception) {
                    // TODO
                }

            }
            override fun onError(error: String) {
                System.err.println(error)
                isRefreshing = false
            }
        }

        try {
            controller.loginUser(
                AuthRequest(userState.value.username, userState.value.password),
                updateResponse
            )
        } catch (ex: Exception) {
            // TODO
        }
    }

    /**
     * Returns a list of [LatLng] from [route]
     */
    private fun latlngList(route: Route?): List<LatLng> {
        val convertedList = mutableListOf<LatLng>()
        if (route != null) {
            for (rt in route.route) {
                convertedList.add(LatLng(rt.latitude, rt.longitude))
            }
        }
        return convertedList.toList()
    }
}