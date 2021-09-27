import java.io.File
import kotlin.test.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal class TestContainsDBOperation {
    private val standardOut = System.out
    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(stream))
    }

    @BeforeTest
    fun setConstants() = run {
        PATH_DATA_DIRECTORY = "testData/TestContainsDBOperation/"
        MAX_RECORDS_FILE = 5
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
    fun testSet1To10AndThenCheck() {
        val localDB = Database()
        for (i in 0..10) {
            localDB.set(i.toULong(), i.toString())
        }
        for (i in 0..20) {
            localDB.contains(i.toULong())
        }
        assertEquals(
            "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false", stream.toString().trim()
        )
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
        for (i in 0..20) {
            localDB.contains(i.toULong())
        }
        System.setOut(standardOut)
        assertEquals(
            "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "true\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false\n" +
                    "false", stream.toString().trim()
        )
    }

}
