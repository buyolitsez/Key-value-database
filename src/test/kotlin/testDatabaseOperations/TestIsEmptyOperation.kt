package testDatabaseOperations

import Database
import MAX_FILE_SIZE
import PATH_DATA_DIRECTORY
import setDefaultValues
import kotlin.test.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class TestIsEmptyOperation {
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
    fun testNotEmpty() {
        val localDB = Database()
        localDB.clear()
        localDB.set(0U, "0")
        localDB.set(1U, "1")
        localDB.removeKey(1U)
        localDB.removeKey(1U)
        localDB.removeKey(2U)
        localDB.isEmpty()
        assertEquals(stream.toString().trim(), "false")
    }

    @Test
    fun testIsEmpty() {
        val localDB = Database()
        localDB.clear()
        localDB.set(0U, "0")
        localDB.set(1U, "1")
        localDB.removeKey(1U)
        localDB.removeKey(0U)
        localDB.isEmpty()
        assertEquals(stream.toString().trim(), "true")
    }

}