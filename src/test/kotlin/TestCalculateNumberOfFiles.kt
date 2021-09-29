import kotlin.test.*
internal class TestCalculateNumberOfFiles {
    @AfterTest
    fun setAll() = run {
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

