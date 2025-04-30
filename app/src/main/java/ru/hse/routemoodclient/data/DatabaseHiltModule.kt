package ru.hse.routemoodclient.data

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import androidx.room.Room
import android.content.Context

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }


    @Provides
    fun provideDatabase(context: Context): RouteDatabase {
        return Room.databaseBuilder(
            context,
            RouteDatabase::class.java,
            "route_database"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun provideRouteDao(database: RouteDatabase): RouteDao {
        return database.routeDao()
    }
}