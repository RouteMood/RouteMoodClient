package ru.hse.routemoodclient.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Database(entities = [RouteEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RouteDatabase : RoomDatabase() {
    abstract fun routeDao(): RouteDao

    companion object {
        @Volatile
        private var Instance: RouteDatabase? = null

        fun getDatabase(context: Context): RouteDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, RouteDatabase::class.java, "route_database")
                    .fallbackToDestructiveMigration(true)
                    .build().also { Instance = it }
            }
        }
    }
}

class Converters {
    private val gson: Gson = Gson()

    @TypeConverter
    fun fromLatLngList(value: List<LatLng>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toLatLngList(value: String?): List<LatLng>? {
        val listType = object : TypeToken<List<LatLng>>() {}.type
        return gson.fromJson(value, listType)
    }
}