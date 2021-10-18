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

internal class TestRemoveKeyDBOperation {
    private val standardOut = System.out
    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @BeforeTest
    fun setConstants() = run {
        PATH_DATA_DIRECTORY = "testData/TestDatabaseOperations"
        MAX_FILE_SIZE = 100U
        SEPARATOR = '='
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
    fun testSet1To10WithoutRepeating() {
        val localDB = Database()
        localDB.clear()
        for (i in 0..10) {
            localDB.set(i.toULong(), i.toString())
        }
        localDB.removeKey(3U)
        localDB.removeKey(5U)
        localDB.removeKey(10U)
        localDB.set(9U, "11")
        for (i in 0..15) {
            localDB.get(i.toULong())
        }
        assertEquals(
            "0\n" +
                    "1\n" +
                    "2\n" +
                    "No such key\n" +
                    "4\n" +
                    "No such key\n" +
                    "6\n" +
                    "7\n" +
                    "8\n" +
                    "11\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key", stream.toString().trim()
        )
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
        for (i in 0..20) {
            localDB.get(i.toULong())
        }
        assertEquals(
            "5\n" +
                    "6\n" +
                    "7\n" +
                    "No such key\n" +
                    "9\n" +
                    "No such key\n" +
                    "11\n" +
                    "12\n" +
                    "13\n" +
                    "14\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key\n" +
                    "No such key", stream.toString().trim()
        )
    }

}