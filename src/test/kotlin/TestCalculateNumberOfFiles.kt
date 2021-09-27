import kotlin.test.*

internal class TestCalculateNumberOfFiles {
    @Test
    fun test0Files() {
        PATH_DATA_DIRECTORY = "testData/TestReadArgs/test0Files"
        calculateNumberOfFiles()
        assertEquals(1, TOTAL_COUNT_OF_FILES)
    }

    @Test
    fun test1File() {
        PATH_DATA_DIRECTORY = "testData/TestReadArgs/test1File"
        calculateNumberOfFiles()
        assertEquals(1, TOTAL_COUNT_OF_FILES)
    }

    @Test
    fun test2File() {
        PATH_DATA_DIRECTORY = "testData/TestReadArgs/test2Files"
        calculateNumberOfFiles()
        assertEquals(2, TOTAL_COUNT_OF_FILES)
    }

    @Test
    fun test100File() {
        PATH_DATA_DIRECTORY = "testData/TestReadArgs/test100Files"
        calculateNumberOfFiles()
        assertEquals(100, TOTAL_COUNT_OF_FILES)
    }
}
