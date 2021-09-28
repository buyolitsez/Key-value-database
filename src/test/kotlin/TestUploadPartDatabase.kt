import java.io.File
import kotlin.test.*

internal class TestUploadPartDatabase {
    @BeforeTest
    fun setConstants() = run {
        PATH_DATA_DIRECTORY = "testData/TestUploadPartDatabase/"
        MAX_FILE_SIZE = 10
        File("${PATH_DATA_DIRECTORY}1").writeText("") // Clear output file
    }

    @AfterTest
    fun setAll() = run {
        setDefaultValues()
    }

    @Test
    fun testEmptyData() {
        val localDB = Database()
        localDB.data = hashMapOf()
        localDB.uploadPartDatabase()
        assertEquals(
            File("${PATH_DATA_DIRECTORY}1").readText(), ""
        )
        localDB.exit()
    }

    @Test
    fun testDataWithStandardSeparator() {
        val localDB = Database()
        localDB.data = hashMapOf(5.toULong() to "2", 7.toULong() to "a", 3.toULong() to "5")
        localDB.uploadPartDatabase()
        assertEquals(
            File("${PATH_DATA_DIRECTORY}1").readText(), "3=5\n" +
                    "5=2\n" +
                    "7=a\n"
        )
        localDB.exit()
    }

    @Test
    fun testDataWithOddSeparator() {
        val localDB = Database()
        SEPARATOR = '|'
        localDB.data = hashMapOf(5.toULong() to "2=", 32432432.toULong() to "a=", 3.toULong() to "5==")
        localDB.uploadPartDatabase()
        assertEquals(
            File("${PATH_DATA_DIRECTORY}1").readText(), "3|5==\n" +
                    "5|2=\n" +
                    "32432432|a=\n"
        )
        localDB.exit()
    }
}
