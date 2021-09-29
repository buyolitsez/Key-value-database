import kotlin.system.exitProcess

fun throwError(error: String, exitProcess : Boolean = false) {
    System.err.println(error)
    if (exitProcess) {
        db.exit()
        exitProcess(-1)
    }
}