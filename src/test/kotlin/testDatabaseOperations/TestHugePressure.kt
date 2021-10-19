package testDatabaseOperations

import Database
import MAX_FILE_SIZE
import PATH_DATA_DIRECTORY
import SEPARATOR
import setDefaultValues
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.random.Random
import kotlin.random.nextULong
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class TestHugePressure {
    private val countOfOperations = 10000
    private val valueLen = 100
    private val standardOut = System.out
    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @BeforeTest
    fun setConstants() = run {
        PATH_DATA_DIRECTORY = "testData/TestDatabaseOperations"
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
    fun testRandomlySetAndGetBigFileSize() {
        MAX_FILE_SIZE = 134217728U
        val localDB = Database()
        localDB.clear()
        var databaseSize = 1U
        repeat(countOfOperations) {
            val key = Random.nextULong() % (databaseSize * 2U)
            if (Random.nextBoolean()) {
                databaseSize++
                localDB.set(key, getRandomString())
            } else {
                localDB.get(key)
            }
        }
        localDB.clear()
        localDB.exit()
    }

    private fun getRandomString(): String {
        val res = StringBuilder()
        repeat(valueLen) {
            res.append((Random.nextInt() % 26 + 'a'.code).toChar())
        }
        return res.toString()
    }
}