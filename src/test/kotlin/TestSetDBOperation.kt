import java.io.File
import kotlin.test.*

internal class TestSetDBOperation {
    @BeforeTest
    fun setConstants() = run{
        PATH_DATA_DIRECTORY = "testData/TestSetDBOperation/"
        MAX_RECORDS_FILE = 5
    }

    @AfterTest
    fun setAll() = run{
        setDefaultValues()
    }

    @Test
    fun testSet1To10() {
        val localDB = Database()
        for (i in 0..10) {
            localDB.set(i.toULong(), i.toString())
        }
        localDB.uploadPartDatabase()
        assertEquals(
            File("${PATH_DATA_DIRECTORY}1").readText(), "0=0\n" +
                    "1=1\n" +
                    "2=2\n" +
                    "3=3\n"
        )
        assertEquals(
            File("${PATH_DATA_DIRECTORY}2").readText(), "4=4\n" +
                    "5=5\n" +
                    "6=6\n" +
                    "7=7\n"
        )
        assertEquals(
            File("${PATH_DATA_DIRECTORY}3").readText(), "8=8\n" +
                    "9=9\n" +
                    "10=10\n"
        )
        localDB.exit()
    }

    @Test
    fun testReset1To10() {
        val localDB = Database()
        for (i in 0..10) {
            localDB.set(i.toULong(), i.toString())
        }
        for (i in 0..10) {
            localDB.set(i.toULong(), (i + 5).toString())
        }
        localDB.uploadPartDatabase()
        assertEquals(
            File("${PATH_DATA_DIRECTORY}1").readText(), "0=5\n" +
                    "1=6\n" +
                    "2=7\n" +
                    "3=8\n"
        )
        assertEquals(
            File("${PATH_DATA_DIRECTORY}2").readText(), "4=9\n" +
                    "5=10\n" +
                    "6=11\n" +
                    "7=12\n"
        )
        assertEquals(
            File("${PATH_DATA_DIRECTORY}3").readText(), "8=13\n" +
                    "9=14\n" +
                    "10=15\n"
        )
        localDB.exit()
    }

}
