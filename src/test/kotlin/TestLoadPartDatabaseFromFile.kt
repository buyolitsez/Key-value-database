import kotlin.test.*

internal class TestLoadPartDatabaseFromFile {
    @AfterTest
    fun setAll() = run {
        setDefaultValues()
    }

    @Test
    fun testEmptyFile() {
        PATH_DATA_DIRECTORY = "testData/TestLoadPartDatabaseFromFile/testEmptyFile/"
        val localDB = Database()
        assert(localDB.data == HashMap<ULong, String>())
        localDB.exit()
    }

    @Test
    fun testDataWithStandardSeparator() {
        PATH_DATA_DIRECTORY = "testData/TestLoadPartDatabaseFromFile/testDataWithStandardSeparator/"
        MAX_FILE_SIZE = 20U
        SEPARATOR = '='
        val localDB = Database()
        assert(localDB.data == hashMapOf(5.toULong() to "2", 7.toULong() to "a", 3.toULong() to "5"))
        localDB.exit()
    }


    @Test
    fun testDataWithOddSeparator() {
        PATH_DATA_DIRECTORY = "testData/TestLoadPartDatabaseFromFile/testDataWithOddSeparator/"
        MAX_FILE_SIZE = 30U
        SEPARATOR = '|'
        val localDB = Database()
        assert(localDB.data == hashMapOf(5.toULong() to "2=", 32432432.toULong() to "a=", 3.toULong() to "5=="))
        localDB.exit()
    }
}