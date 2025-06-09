package ru.hse.routemoodclient.routesstoragetests

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.hse.routemoodclient.data.DataRepository
import ru.hse.routemoodclient.data.RouteDao
import ru.hse.routemoodclient.data.RouteDatabase
import ru.hse.routemoodclient.data.RouteEntity
import ru.hse.routemoodclient.data.RouteUiState
import ru.hse.routemoodclient.data.UserState
import ru.hse.routemoodclient.data.toRouteEntity

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DataRepositoryTest {
    private lateinit var repository: DataRepository
    private lateinit var dao: RouteDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database = Room.inMemoryDatabaseBuilder(
            context,
            RouteDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.routeDao()
        repository = DataRepository(dao)
    }

    @Test
    fun insertAndGetRoute() = runTest {
        val route = RouteEntity(name = "Repo Test", route = listOf(LatLng(1.0, 1.0)))
        repository.insertRoute(route)

        val routes = repository.getRouteList().first()
        assertEquals(1, routes.size)
        assertEquals("Repo Test", routes[0].name)
    }

    @Test
    fun updateUserState() = runTest {
        val newState = UserState(username = "newUser", login = "newLogin")
        repository.updateUserState(newState)

        val currentState = repository.userState.value
        assertEquals("newUser", currentState.username)
        assertEquals("newLogin", currentState.login)
    }

    @Test
    fun updateRouteState() = runTest {
        val newState = RouteUiState(name = "New Route", isStartSet = true)
        repository.updateRouteState(newState)

        val currentState = repository.routeState.value
        assertEquals("New Route", currentState.name)
        assertTrue(currentState.isStartSet)
    }

    @Test
    fun loadingState() = runTest {
        repository.setLoading(true)
        assertTrue(repository.isLoading.value)

        repository.setLoading(false)
        assertFalse(repository.isLoading.value)
    }

    @Test
    fun routeConversion() = runTest {
        val uiState = RouteUiState(
            name = "Test",
            route = listOf(LatLng(1.0, 1.0), LatLng(2.0, 2.0))
        )
        val entity = uiState.toRouteEntity()

        repository.insertRoute(entity)
        val loaded = repository.getRouteList().first()[0]

        assertEquals(uiState.name, loaded.name)
        assertEquals(uiState.route, loaded.route)
    }
}