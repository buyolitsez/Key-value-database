import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestErrors {
    @AfterTest
    fun setAll() = run {
        setDefaultValues()
    }

    private val standardOut = System.err

    private val stream = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setErr(PrintStream(stream))
    }

    @AfterTest
    fun tearDown() {
        System.setErr(standardOut)
    }


    @Test
    fun testTooBigRecord() {
        val localDB = Database()
        localDB.clear()
        MAX_FILE_SIZE = 1U
        localDB.set(0U, "111111111111111111")
        assertEquals("Too big record, to write in a one file\n 0=111111111111111111", stream.toString().trim())
    }

    @Test
    fun testNoArgs() {
        readArgs(listOf())
        assertEquals("No args", stream.toString().trim())
    }

    @Test
    fun testUnknownOperation() {
        readArgs(listOf("trash"))
        assertEquals("Dont know trash operation", stream.toString().trim())
    }

    @Test
    fun testWrongCountData() {
        readArgs(listOf("set", "1"))
        assertEquals("Wrong count of data", stream.toString().trim())
    }

    @Test
    fun testKeyWithSeparator() {
        SEPARATOR = '='
        readArgs(listOf("get", "1=2"))
        assertEquals("Key and value cant contain separator $SEPARATOR", stream.toString().trim())
    }

    @Test
    fun testValueWithSeparator() {
        SEPARATOR = '='
        readArgs(listOf("set", "1", "2=3"))
        assertEquals("Key and value cant contain separator $SEPARATOR", stream.toString().trim())
    }


}