import kotlin.test.*

internal class TestReadArgs {

    @Test
    fun testGet() {
        assertEquals(
            readArgs(listOf("get", "a")),
            Operation("get", getHash("a"), "")
        )

        assertEquals(
            readArgs(listOf("get", "Ab1")),
            Operation("get", getHash("Ab1"), "")
        )
    }

    @Test
    fun testChange() {
        assertEquals(
            readArgs(listOf("set", "a", "b")),
            Operation("set", getHash("a"), "b")
        )

        assertEquals(
            readArgs(listOf("set", "Ab1", "B23ea")),
            Operation("set", getHash("Ab1"), "B23ea")
        )
    }

    @Test
    fun testIsEmpty() {
        assertEquals(
            readArgs(listOf("is-empty")),
            Operation("is-empty", getHash(""), "")
        )
    }
}
