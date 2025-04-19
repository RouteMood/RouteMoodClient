package ru.hse.routemoodclient.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.hse.routemood.ApiCallback
import ru.hse.routemood.Controller
import ru.hse.routemood.models.AuthRequest
import ru.hse.routemood.models.AuthResponse
import ru.hse.routemood.models.GptRequest
import ru.hse.routemood.models.RegisterRequest
import ru.hse.routemood.models.Route
import ru.hse.routemoodclient.data.DataRepository
import ru.hse.routemoodclient.data.RouteUiState
import ru.hse.routemoodclient.data.UserState
import javax.inject.Inject

/**
 * [ServerViewModel] holds information about a route settings in terms of length, time.
 */
@HiltViewModel
class ServerViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val controller : Controller = Controller()
    /**
     * User state
     */
    val userState: StateFlow<UserState> = dataRepository.userState
    /**
     * Route state
     */
    val routeState: StateFlow<RouteUiState> = dataRepository.routeState

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

    /**
     * Ask login user from server
     */
    fun askLoginUser() {
        val callback = object : ApiCallback<AuthResponse> {
            override fun onSuccess(result: AuthResponse?) {
                if (result != null) {
                    val updatedUserState = userState.value.copy(token = result.token)
                    dataRepository.updateUserState(updatedUserState)
                }
            }

            override fun onError(error: String?) {
                // TODO
            }
        }
        try {
            controller.loginUser(
                AuthRequest(userState.value.username, userState.value.password),
                callback
            )
        } catch (ex: Exception) {
            println(ex)
        }

    }

    /**
     * Ask register user from server
     */
    fun askRegisterUser() {
        val dataResponse = controller.registerUser(
            RegisterRequest(userState.value.username, userState.value.login, userState.value.password),
            object : ApiCallback<AuthResponse> {
                override fun onSuccess(result: AuthResponse?) {
                    if (result != null) {
                        val updatedUserState = userState.value.copy(token = result.token)
                        dataRepository.updateUserState(updatedUserState)
                    }
                }

                override fun onError(error: String?) {
                    // TODO
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
        val getRouteResponse = object : ApiCallback<Route?> {
            override fun onSuccess(result: Route?) {
                val updatedRouteState = routeState.value.copy(route = latlngList(result))
                dataRepository.updateRouteState(updatedRouteState)
            }

            override fun onError(error: String) {
                System.err.println(error)
            }
        }
        val getResponse = object : ApiCallback<AuthResponse?> {
            override fun onSuccess(result: AuthResponse?) {
                try {
                    controller.getRoute(
                        GptRequest(
                            "Default walk",
                            routeState.value.start.longitude,
                            routeState.value.start.latitude
                        ), getRouteResponse
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
                getResponse
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
        return convertedList
    }
}