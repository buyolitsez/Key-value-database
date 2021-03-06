import kotlin.system.exitProcess

/**
 * @property nameOperation is a name of operation
 * @property key is a key when it needed or 0 when not.
 * @property value is a value when it needed or empty string when not.
 */
data class Operation(var nameOperation: String, val key: ULong, val value: String)

/** Read command and values from run args
 * @property args run args
 * @return Operation
 */
fun readArgs(args: List<String>): Operation? {
    if (args.isEmpty()) {
        throwError("No args")
        return null
    }
    val nameOperation = NAME_OF_FUNCTIONS[args[0].trim()]
    if (nameOperation == null) {
        throwError("Dont know operation")
        return null
    }
    if (args.size != FUNCTIONS[nameOperation]!! + 1) {
        throwError("Wrong count of data")
        return null
    }
    var key = ""
    var value = ""
    if (args.size > 1) {
        key = args[1]
    }
    if (args.size > 2) {
        value = args[2]
    }
    if (key.contains(SEPARATOR) || value.contains(SEPARATOR)) {
        throwError("Key and value cant contain separator $SEPARATOR\n")
        return null
    }
    return Operation(nameOperation, getHash(key), value)
}

/** Execute single Operation */
fun doOperation(input: List<String>) {
    val operation = readArgs(input) ?: return
    when (operation.nameOperation) {
        "exit" -> {
            db.exit()
            exitProcess(0)
        }
        "containsKey" -> db.containsKey(operation.key)
        "get" -> db.get(operation.key)
        "set" -> db.set(operation.key, operation.value)
        "removeKey" -> db.removeKey(operation.key)
        "size" -> db.size()
        "is-empty" -> db.isEmpty()
        "clear" -> db.clear()
        "values" -> db.outputValues()
        else -> {
            throwError("Forgot to add function"); return
        }
    }
}

/** Reads commands from run args and executes them */

fun startOperationFromArgs(args: Array<String>) {
    var currentIterator = 0
    while (currentIterator < args.size) {
        val nameOperation = args[currentIterator].trim()
        if (!FUNCTIONS.containsKey(nameOperation)) {
            throwError("Dont know $nameOperation operation")
            break
        }
        val needArguments = FUNCTIONS[nameOperation]!!
        if (currentIterator + needArguments > args.size) {
            throwError("Wrong count of data")
            break
        }
        doOperation(args.copyOfRange(currentIterator, currentIterator + needArguments + 1).toList())
        currentIterator += needArguments + 1
    }
    db.exit()
}

/** Reads commands and executes them */

fun startOperation(args: Array<String>) {
    if (args.isNotEmpty()) {
        startOperationFromArgs(args)
        return
    }
    outputStringWithColor("command: ")
    var str: String?
    while (true) {
        str = readLine()
        if (str != null) {
            val input = str.split(' ').filter { it.isNotBlank() }
            doOperation(input)
            outputStringWithColor("command: ")
        }
    }
}

fun outputStringWithColor(string: String) {
    val color = "\u001B[36m"
    val reset = "\u001B[0m"
    print(color + string + reset)
}

