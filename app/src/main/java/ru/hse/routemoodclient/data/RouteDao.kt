package ru.hse.routemoodclient.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(route: RouteEntity)

    @Delete
    suspend fun delete(route: RouteEntity)

    @Query("SELECT * from routes WHERE id = :id")
    fun getRouteById(id: Int): Flow<RouteEntity>

    @Query("SELECT * from routes")
    fun getAllRoutes(): Flow<List<RouteEntity>>

    @Query("DELETE FROM routes")
    suspend fun nukeTable()

    @Query("DELETE FROM sqlite_sequence WHERE name='routes'")
    suspend fun resetAutoIncrement()
}

