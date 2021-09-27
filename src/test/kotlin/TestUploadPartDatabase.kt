import java.io.File
import kotlin.test.*

internal class TestUploadPartDatabase {
    @BeforeTest
    fun getNullData() = run {
        PATH_DATA_DIRECTORY = "testData/TestUploadPartDatabase/"
        MAX_RECORDS_FILE = 5
        calculateNumberOfFiles()
        getNextPart()
    }

    @AfterTest
    fun setAllBack() = run{
        SEPARATOR = '='
    }

    @Test
    fun testEmptyData() {
        data = hashMapOf()
        uploadPartDatabase()
        assertEquals(
            File("${PATH_DATA_DIRECTORY}1").readText(), ""
        )
    }

    @Test
    fun testDataWithStandardSeparator() {
        data = hashMapOf(5.toULong() to "2", 7.toULong() to "a", 3.toULong() to "5")
        uploadPartDatabase()
        assertEquals(
            File("${PATH_DATA_DIRECTORY}1").readText(), "3=5\n" +
                    "5=2\n" +
                    "7=a\n"
        )
    }

    @Test
    fun testDataWithOddSeparator() {
        data = hashMapOf(5.toULong() to "2=", 32432432.toULong() to "a=", 3.toULong() to "5==")
        SEPARATOR = '|'
        uploadPartDatabase()
        assertEquals(
            File("${PATH_DATA_DIRECTORY}1").readText(), "3|5==\n" +
                    "5|2=\n" +
                    "32432432|a=\n")
    }
}
