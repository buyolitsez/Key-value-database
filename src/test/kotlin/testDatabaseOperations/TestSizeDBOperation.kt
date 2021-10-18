package testDatabaseOperations

import Database
import MAX_FILE_SIZE
import PATH_DATA_DIRECTORY
import SEPARATOR
import setDefaultValues
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestSizeDBOperation {
    private val standardOut = System.out
    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @BeforeTest
    fun setConstants() = run {
        SEPARATOR = '='
        PATH_DATA_DIRECTORY = "testData/TestDatabaseOperations"
        MAX_FILE_SIZE = 100U
    }

    @AfterTest
    fun setAll() = run {
        setDefaultValues()
    }

    @AfterTest
    fun tearDown() {
        System.setOut(standardOut)
    }

    @Test
    fun testSet1To10AndThenCheck() {
        val localDB = Database()
        localDB.clear()
        for (i in 0..10) {
            localDB.set(i.toULong(), i.toString())
        }
        localDB.removeKey(3U)
        localDB.removeKey(5U)
        localDB.removeKey(10U)
        localDB.size()
        assertEquals(stream.toString().trim().toInt(), 8)
    }

    @Test
    fun testReset1To10() {
        val localDB = Database()
        localDB.clear()
        for (i in 0..10) {
            localDB.set(i.toULong(), i.toString())
        }
        for (i in 0..10) {
            localDB.set(i.toULong(), (i + 5).toString())
        }
        localDB.removeKey(3U)
        localDB.removeKey(5U)
        localDB.removeKey(10U)
        localDB.removeKey(11U)
        localDB.removeKey(13U)
        localDB.removeKey(15U)
        localDB.size()
        assertEquals(stream.toString().trim().toInt(), 8)
    }

    @Test
    fun testZeroSize() {
        val localDB = Database()
        localDB.clear()
        for (i in 0..5) {
            localDB.set(i.toULong(), i.toString())
        }
        localDB.removeKey(0U)
        localDB.removeKey(1U)
        localDB.removeKey(2U)
        localDB.removeKey(3U)
        localDB.removeKey(4U)
        localDB.removeKey(5U)
        localDB.size()
        assertEquals(stream.toString().trim().toInt(), 0)
    }
}