package testDatabaseOperations

import Database
import MAX_RECORDS_FILE
import PATH_DATA_DIRECTORY
import setDefaultValues
import kotlin.test.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class TestRemoveKeyDBOperation {
    private val standardOut = System.out
    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @BeforeTest
    fun setConstants() = run {
        PATH_DATA_DIRECTORY = "testData/TestRemoveDBOperation/"
        MAX_RECORDS_FILE = 5
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
                    "9\n" +
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
        localDB.removeKey(3.toULong())
        localDB.removeKey(5.toULong())
        localDB.removeKey(10.toULong())
        localDB.removeKey(11.toULong())
        localDB.removeKey(13.toULong())
        localDB.removeKey(15.toULong())
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