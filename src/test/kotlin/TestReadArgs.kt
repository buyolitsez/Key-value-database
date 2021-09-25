import kotlin.test.*

internal class TestReadArgs {

    @Test
    fun testFind() {
        assertEquals(
            readArgs(arrayOf("find", "a")),
            Operation("find", "a", "")
        )

        assertEquals(
            readArgs(arrayOf("find", "Ab1")),
            Operation("find", "Ab1", "")
        )
    }

    @Test
    fun testChange() {
        assertEquals(
            readArgs(arrayOf("change", "a", "b")),
            Operation("change", "a", "b")
        )

        assertEquals(
            readArgs(arrayOf("change", "Ab1", "B23ea")),
            Operation("change", "Ab1", "B23ea")
        )
    }

    @Test
    fun testIsEmpty() {
        assertEquals(
            readArgs(arrayOf("is-empty")),
            Operation("is-empty", "", "")
        )
    }
}
