import kotlin.system.exitProcess

fun throwError(error: String) {
    System.err.println(error)
    exitProcess(-1)
}