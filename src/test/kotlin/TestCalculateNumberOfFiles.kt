import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestCalculateNumberOfFiles {
    @BeforeTest
    fun setAllBefore() = run {
        SEPARATOR = '='
        MAX_FILE_SIZE = 7U
    }

    @AfterTest
    fun setAllAfter() = run {
        setDefaultValues()
    }

    @Test
    fun test0Files() {
        PATH_DATA_DIRECTORY = "testData/TestCalculateNumberOfFiles/test0Files"
        val localDB = Database()
        assertEquals(1, localDB.totalCountOfFiles)
        localDB.exit()
    }

    @Test
    fun test1File() {
        PATH_DATA_DIRECTORY = "testData/TestCalculateNumberOfFiles/test1File"
        val localDB = Database()
        assertEquals(1, localDB.totalCountOfFiles)
        localDB.exit()
    }

    @Test
    fun test2File() {
        PATH_DATA_DIRECTORY = "testData/TestCalculateNumberOfFiles/test2Files"
        val localDB = Database()
        assertEquals(2, localDB.totalCountOfFiles)
        localDB.exit()
    }

    @Test
    fun test100File() {
        PATH_DATA_DIRECTORY = "testData/TestCalculateNumberOfFiles/test100Files"
        val localDB = Database()
        assertEquals(100, localDB.totalCountOfFiles)
        localDB.exit()
    }
}

