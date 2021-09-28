package testDatabaseOperations

import Database
import MAX_FILE_SIZE
import PATH_DATA_DIRECTORY
import setDefaultValues
import kotlin.test.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class TestSizeDBOperation {
    private val standardOut = System.out
    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @BeforeTest
    fun setConstants() = run {
        PATH_DATA_DIRECTORY = "testData/TestDatabaseOperations"
        MAX_FILE_SIZE = 100
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
        localDB.removeKey(3.toULong())
        localDB.removeKey(5.toULong())
        localDB.removeKey(10.toULong())
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
        localDB.removeKey(3.toULong())
        localDB.removeKey(5.toULong())
        localDB.removeKey(10.toULong())
        localDB.removeKey(11.toULong())
        localDB.removeKey(13.toULong())
        localDB.removeKey(15.toULong())
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
        localDB.removeKey(0.toULong())
        localDB.removeKey(1.toULong())
        localDB.removeKey(2.toULong())
        localDB.removeKey(3.toULong())
        localDB.removeKey(4.toULong())
        localDB.removeKey(5.toULong())
        localDB.size()
        assertEquals(stream.toString().trim().toInt(), 0)
    }
}