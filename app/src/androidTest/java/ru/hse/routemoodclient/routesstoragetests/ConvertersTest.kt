package ru.hse.routemoodclient.routesstoragetests

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import ru.hse.routemoodclient.data.Converters

@RunWith(AndroidJUnit4::class)
class ConvertersTest {
    private val converters = Converters()

    @Test
    fun testLatLngListConversion() {
        val originalList = listOf(
            LatLng(59.9386, 30.3141),
            LatLng(55.7558, 37.6173)
        )

        val json = converters.fromLatLngList(originalList)
        assertNotNull(json)

        val convertedList = converters.toLatLngList(json)
        assertEquals(originalList, convertedList)
    }

    @Test
    fun testNullConversion() {
        assertEquals("null", converters.fromLatLngList(null))
        assertNull(converters.toLatLngList(null))
    }

    @Test
    fun testEmptyListConversion() {
        val emptyList = emptyList<LatLng>()
        val json = converters.fromLatLngList(emptyList)
        val convertedList = converters.toLatLngList(json)
        assertEquals(emptyList, convertedList)
    }
}