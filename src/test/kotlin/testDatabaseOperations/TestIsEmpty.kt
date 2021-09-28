package testDatabaseOperations

import Database
import MAX_RECORDS_FILE
import PATH_DATA_DIRECTORY
import setDefaultValues
import kotlin.test.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class TestIsEmpty {
    private val standardOut = System.out
    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @BeforeTest
    fun setConstants() = run {
        PATH_DATA_DIRECTORY = "testData/TestDatabaseOperations"
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
    fun testNotEmpty() {
        val localDB = Database()
        localDB.clear()
        localDB.set(0.toULong(), "0")
        localDB.set(1.toULong(), "1")
        localDB.removeKey(1.toULong())
        localDB.removeKey(1.toULong())
        localDB.removeKey(2.toULong())
        localDB.isEmpty()
        assertEquals(stream.toString().trim(), "false")
    }

    @Test
    fun testIsEmpty() {
        val localDB = Database()
        localDB.clear()
        localDB.set(0.toULong(), "0")
        localDB.set(1.toULong(), "1")
        localDB.removeKey(1.toULong())
        localDB.removeKey(0.toULong())
        localDB.isEmpty()
        assertEquals(stream.toString().trim(), "true")
    }

}