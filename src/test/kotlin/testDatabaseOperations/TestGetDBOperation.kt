package testDatabaseOperations

import Database
import MAX_FILE_SIZE
import PATH_DATA_DIRECTORY
import setDefaultValues
import kotlin.test.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class TestGetDBOperation {
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
        for (i in 0..20) {
            localDB.get(i.toULong())
        }
        assertEquals(
            "0\n" +
                    "1\n" +
                    "2\n" +
                    "3\n" +
                    "4\n" +
                    "5\n" +
                    "6\n" +
                    "7\n" +
                    "8\n" +
                    "9\n" +
                    "10\n" +
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
        for (i in 0..20) {
            localDB.get(i.toULong())
        }
        assertEquals(
            "5\n" +
                    "6\n" +
                    "7\n" +
                    "8\n" +
                    "9\n" +
                    "10\n" +
                    "11\n" +
                    "12\n" +
                    "13\n" +
                    "14\n" +
                    "15\n" +
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