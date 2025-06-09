package ru.hse.routemoodclient.routesstoragetests

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.hse.routemoodclient.data.RouteDao
import ru.hse.routemoodclient.data.RouteDatabase
import ru.hse.routemoodclient.data.RouteEntity

@RunWith(AndroidJUnit4::class)
class RouteDaoTest {
    private lateinit var database: RouteDatabase
    private lateinit var dao: RouteDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            RouteDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.routeDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertRouteAndGetById() = runTest {
        val route = RouteEntity(name = "Test Route", route = listOf(LatLng(1.0, 1.0)))
        dao.insert(route)

        val loaded = dao.getRouteById(1).first()
        assertEquals(route.copy(id = 1), loaded)
    }

    @Test
    fun getAllRoutes() = runTest {
        val route1 = RouteEntity(name = "Route 1", route = listOf(LatLng(1.0, 1.0)))
        val route2 = RouteEntity(name = "Route 2", route = listOf(LatLng(2.0, 2.0)))

        dao.insert(route1)
        dao.insert(route2)

        val routes = dao.getAllRoutes().first()
        assertEquals(2, routes.size)
    }

    @Test
    fun deleteRoute() = runTest {
        val route = RouteEntity(name = "To Delete", route = listOf(LatLng(1.0, 1.0)))
        dao.insert(route)

        val inserted = dao.getRouteById(1).first()
        dao.delete(inserted)

        val routes = dao.getAllRoutes().first()
        assertTrue(routes.isEmpty())
    }

    @Test
    fun nukeTable() = runTest {
        val route = RouteEntity(name = "Test", route = listOf(LatLng(1.0, 1.0)))
        dao.insert(route)

        dao.nukeTable()

        val routes = dao.getAllRoutes().first()
        assertTrue(routes.isEmpty())
    }
}