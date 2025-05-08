package ru.hse.routemoodclient.drawRoute

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import ru.hse.routemoodclient.data.RouteUiState
import kotlin.coroutines.suspendCoroutine

class ApiService : ViewModel() {
    private val _positionsState = MutableStateFlow(listOf<LatLng>())
    val positionsState: StateFlow<List<LatLng>> = _positionsState.asStateFlow()

    private suspend fun getRoutesSuspend(key: String, origin: LatLng, destination: LatLng, alternatives: Boolean): List<List<LatLng>>? {
        return suspendCoroutine { cont ->
            val TAG = "drawgooglemaproute-request_response:"
            viewModelScope.launch {
                try {
                    val request = Api.invoke().getRoute(
                        "${origin.latitude},${origin.longitude}",
                        "${destination.latitude},${destination.longitude}",
                        key,
                        alternatives,
                        "walking"
                    )
                    val data = withContext(Dispatchers.IO) {
                        if (request.isSuccessful) {
                            request.body()
                        } else {
                            request.message()
                        }
                    }

                    if (data is ResponseBody) {
                        val json = JsonParser().parse(data.string()).asJsonObject
                        if (json.get("status").asString == "OK") {
                            val routes = FetchRoutes().getRoutes(json.getAsJsonArray("routes"))
                            cont.resumeWith(Result.success(routes))
                        } else {
                            Log.i(TAG, json.get("status").asString)
                            cont.resumeWith(Result.failure(Exception(json.get("status").asString)))
                        }
                    } else if (data is String) {
                        Log.i(TAG, data)
                        cont.resumeWith(Result.failure(Exception(data)))
                    }
                } catch (e: Exception) {
                    Log.i(TAG, e.message ?: "")
                    cont.resumeWith(Result.failure(e))
                }
            }
        }
    }

    fun updatePositions(
        mapsApiKey : String,
        positions: List<LatLng>
    ) {
        viewModelScope.launch {
            val newPositions = mutableListOf<LatLng>()
            var previousPos = positions.first()

            for (currentPos in positions) {
                if (currentPos == positions.first()) {
                    continue
                }
                val route = getRoutesSuspend(
                    mapsApiKey,
                    previousPos,
                    currentPos,
                    false
                )
                if (route != null) {
                    newPositions.addAll(route.first())
                }
                previousPos = currentPos
            }

            _positionsState.value = newPositions
        }
    }

    fun clearRoute() {
        _positionsState.value = listOf<LatLng>()
    }

    private interface Api {
        @GET("json")
        suspend fun getRoute(
            @Query("origin") origin: String,
            @Query("destination") destination: String,
            @Query("key") key: String,
            @Query("alternatives") alternatives: Boolean,
            @Query("mode") mode: String
        ): Response<ResponseBody>

        companion object {
            private val retrofit by lazy {
                Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                    .build()
            }

            operator fun invoke(): Api {
                return retrofit.create(Api::class.java)
            }
        }
    }

}