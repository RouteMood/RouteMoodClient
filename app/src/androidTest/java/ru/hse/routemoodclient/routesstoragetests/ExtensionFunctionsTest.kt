package ru.hse.routemoodclient.routesstoragetests

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import ru.hse.routemoodclient.data.RouteEntity
import ru.hse.routemoodclient.data.RouteUiState
import ru.hse.routemoodclient.data.toRouteEntity
import ru.hse.routemoodclient.data.toRouteUiState

@RunWith(AndroidJUnit4::class)
class ExtensionFunctionsTest {
    @Test
    fun testRouteUiStateToEntityConversion() {
        val uiState = RouteUiState(
            name = "Test Route",
            routeRequest = "Custom request",
            route = listOf(LatLng(1.0, 1.0), LatLng(2.0, 2.0))
        )

        val entity = uiState.toRouteEntity()

        assertEquals(uiState.name, entity.name)
        assertEquals(uiState.routeRequest, entity.routeRequest)
        assertEquals(uiState.route, entity.route)
    }

    @Test
    fun testRouteEntityToUiStateConversion() {
        val entity = RouteEntity(
            id = 1,
            name = "Test Entity",
            routeRequest = "Entity request",
            route = listOf(LatLng(1.0, 1.0), LatLng(2.0, 2.0))
        )

        val uiState = entity.toRouteUiState()

        assertEquals(entity.name, uiState.name)
        assertEquals(entity.routeRequest, uiState.routeRequest)
        assertEquals(entity.route, uiState.route)
        assertEquals(entity.route[0], uiState.start)
        assertEquals(entity.route.last(), uiState.end)
        assertTrue(uiState.isStartSet)
        assertTrue(uiState.isEndSet)
    }
}