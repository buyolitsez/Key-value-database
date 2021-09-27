import kotlin.test.*

internal class TestLoadPartDatabaseFromFile {
    @AfterTest
    fun uploadDataBack() = run {
        uploadPartDatabase()
    }

    @Test
    fun testEmptyFile() {
        PATH_DATA_DIRECTORY = "testData/TestLoadPartDatabaseFromFile/testEmptyFile/"
        calculateNumberOfFiles()
        loadPartDatabaseFromFile(1)
        assert(data == HashMap<ULong, String>())
    }

    @Test
    fun testDataWithOddSeparator() {
        PATH_DATA_DIRECTORY = "testData/TestLoadPartDatabaseFromFile/testDataWithOddSeparator/"
        MAX_RECORDS_FILE = 5
        SEPARATOR = '|'
        calculateNumberOfFiles()
        loadPartDatabaseFromFile(1)
        assert(data == hashMapOf(5.toULong() to "2=", 32432432.toULong() to "a=", 3.toULong() to "5=="))
    }

    @Test
    fun testDataWithStandardSeparator() {
        PATH_DATA_DIRECTORY = "testData/TestLoadPartDatabaseFromFile/testDataWithStandardSeparator/"
        MAX_RECORDS_FILE = 5
        SEPARATOR = '='
        calculateNumberOfFiles()
        loadPartDatabaseFromFile(1)
        assert(data == hashMapOf(5.toULong() to "2", 7.toULong() to "a", 3.toULong() to "5"))
    }

}
