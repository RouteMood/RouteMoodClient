package ru.hse.routemoodclient.imageManagerTest

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.hse.routemoodclient.ui.ImageManager
import java.io.File
import java.util.UUID

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ImageManagerTest {
    private lateinit var imageManager: ImageManager
    private lateinit var context: Context
    private lateinit var dataStore: DataStore<Preferences>
    private val testDataStoreName = "test_image_prefs_${System.currentTimeMillis()}"

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        // Создаем уникальное имя для DataStore в каждом тесте
        dataStore = PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(testDataStoreName) }
        )

        // Очищаем данные перед каждым тестом
        runBlocking {
            dataStore.edit { it.clear() }
            File(context.filesDir, ImageManager.IMAGE_DIR).deleteRecursively()
        }

        imageManager = ImageManager(dataStore, context)
    }

    @After
    fun cleanup() {
        runBlocking {
            // Удаляем тестовый DataStore
            File(context.filesDir, "datastore/$testDataStoreName.preferences_pb").delete()
            File(context.filesDir, ImageManager.IMAGE_DIR).deleteRecursively()
        }
    }


    @Test
    fun addAndGetUuidUriPair() = runTest {
        // Создаем тестовый файл изображения
        val testFile = File(context.filesDir, "test_image.jpg")
        testFile.createNewFile()
        testFile.writeText("test image content")
        val testUri = Uri.fromFile(testFile)

        val uuid = UUID.randomUUID()
        val savedFile = imageManager.addUuidUriPair(uuid, testUri)

        // Проверяем блокирующую версию
        val retrievedFile = imageManager.getFileForUuidBlocking(uuid)
        assertEquals(savedFile.absolutePath, retrievedFile?.absolutePath)
        assertTrue(retrievedFile?.exists() ?: false)

        // Проверяем Flow версию
        val flowFile = imageManager.getFileForUuid(uuid).first()
        assertEquals(savedFile.absolutePath, flowFile?.absolutePath)
    }

    @Test
    fun addAndGetUuidByteArrayPair() = runTest {
        val uuid = UUID.randomUUID()
        val testData = "test byte array content".toByteArray()

        val savedFile = imageManager.addUuidByteArrayPair(uuid, testData)

        // Проверяем содержимое файла
        val fileContent = savedFile.readBytes()
        assertTrue(fileContent.contentEquals(testData))

        // Проверяем Uri
        val uri = imageManager.getUriForUuidBlocking(uuid)
        assertNotNull(uri)
    }

    @Test
    fun removeUuidUriPair() = runTest {
        val uuid = UUID.randomUUID()
        val testData = "test content".toByteArray()
        val file = imageManager.addUuidByteArrayPair(uuid, testData)

        // Убеждаемся, что файл существует
        assertTrue(file.exists())

        // Удаляем
        imageManager.removeUuidUriPair(uuid)

        // Проверяем, что файл удален
        assertFalse(file.exists())

        // Проверяем, что запись удалена из DataStore
        val path = dataStore.data.map { it[imageManager.uuidKey(uuid)] }.first()
        assertNull(path)
    }

    @Test
    fun getAllPairs() = runTest {
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()

        imageManager.addUuidByteArrayPair(uuid1, "image1".toByteArray())
        imageManager.addUuidByteArrayPair(uuid2, "image2".toByteArray())

        val allPairs = imageManager.getAllPairs().first()

        assertEquals(2, allPairs.size)
        assertTrue(allPairs.containsKey(uuid1))
        assertTrue(allPairs.containsKey(uuid2))
    }

    @Test
    fun getUriForUuid() = runTest {
        val uuid = UUID.randomUUID()
        val testData = "test uri content".toByteArray()
        imageManager.addUuidByteArrayPair(uuid, testData)

        // Проверяем блокирующую версию
        val blockingUri = imageManager.getUriForUuidBlocking(uuid)
        assertNotNull(blockingUri)

        // Проверяем Flow версию
        val flowUri = imageManager.getUriForUuid(uuid).first()
        assertEquals(blockingUri, flowUri)
    }

    @Test
    fun fileNotExistsAfterRemoval() = runTest {
        val uuid = UUID.randomUUID()
        val file = imageManager.addUuidByteArrayPair(uuid, "test".toByteArray())

        // Удаляем
        imageManager.removeUuidUriPair(uuid)

        // Проверяем, что файл действительно удален
        assertFalse(file.exists())
    }

    @Test
    fun addInvalidUriThrowsException() = runTest {
        val uuid = UUID.randomUUID()
        val invalidUri = Uri.parse("content://invalid.uri")

        assertThrows(java.io.FileNotFoundException::class.java) {
            runBlocking {
                imageManager.addUuidUriPair(uuid, invalidUri)
            }
        }
    }
}