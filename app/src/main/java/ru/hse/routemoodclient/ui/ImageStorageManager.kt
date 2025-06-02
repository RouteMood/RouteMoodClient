package ru.hse.routemoodclient.ui

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ImageManager @Inject constructor(
    @Named("imagePrefs") private val dataStore: DataStore<Preferences>,
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val IMAGE_DIR = "user_images"
    }

    private val imageDir by lazy {
        File(context.filesDir, IMAGE_DIR).apply {
            if (!exists()) mkdirs()
        }
    }

    private fun uuidKey(uuid: UUID) = stringPreferencesKey("image_${uuid}")

    // Добавление/обновление пары UUID -> Uri и сохранение файла
    suspend fun addUuidUriPair(uuid: UUID, uri: Uri): File {
        val file = createImageFile(uuid)
        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        } ?: throw IllegalStateException("Could not open input stream for URI: $uri")

        dataStore.edit { preferences ->
            preferences[uuidKey(uuid)] = file.absolutePath
        }

        return file
    }

    suspend fun addUuidByteArrayPair(uuid: UUID, byteArray: ByteArray): File {
        val file = createImageFile(uuid)

        // Write the byte array directly to the file
        file.outputStream().use { output ->
            output.write(byteArray)
        }

        // Save the file path in DataStore
        dataStore.edit { preferences ->
            preferences[uuidKey(uuid)] = file.absolutePath
        }

        return file
    }

    // Синхронное получение File по UUID (блокирующий вызов)
    fun getFileForUuidBlocking(uuid: UUID): File? {
        return runBlocking {
            dataStore.data
                .map { preferences -> preferences[uuidKey(uuid)] }
                .first()
                ?.let { File(it) }
                ?.takeIf { it.exists() }
        }
    }

    // Синхронное получение Uri по UUID (блокирующий вызов)
    fun getUriForUuidBlocking(uuid: UUID): Uri? {
        return getFileForUuidBlocking(uuid)?.toUri()
    }

    // Получение File по UUID
    fun getFileForUuid(uuid: UUID): Flow<File?> = dataStore.data
        .map { preferences ->
            preferences[uuidKey(uuid)]?.let { File(it) }
        }

    // Получение Uri по UUID
    fun getUriForUuid(uuid: UUID): Flow<Uri?> = getFileForUuid(uuid)
        .map { it?.toUri() }

    // Удаление пары и файла
    suspend fun removeUuidUriPair(uuid: UUID) {
        val filePath = dataStore.data
            .map { preferences -> preferences[uuidKey(uuid)] }
            .first()

        dataStore.edit { preferences ->
            preferences.remove(uuidKey(uuid))
        }

        filePath?.let { File(it).delete() }
    }

    // Получение всех пар
    fun getAllPairs(): Flow<Map<UUID, File>> = dataStore.data
        .map { preferences ->
            preferences.asMap()
                .filterKeys { it.name.startsWith("image_") }
                .mapKeys { UUID.fromString(it.key.name.removePrefix("image_")) }
                .mapValues { File(it.value.toString()) }
                .filterValues { it.exists() }
        }

    private fun createImageFile(uuid: UUID): File {
        return File(imageDir, "${uuid}.jpg")
    }

    private fun File.toUri(): Uri {
        return Uri.fromFile(this)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Named("imagePrefs")
    fun provideImagePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("image_prefs") }
        )
    }
}