/**
 * @property nameOperation is a name of operation
 * @property key is a key when it needed or empty string when not.
 * @property value is a value when it needed or empty string when not.
 */
data class Operation(var nameOperation: String, val key: ULong, val value: String)

/** Read command and values from run args
 * @property args run args
 */
fun readArgs(args: List<String>): Operation? {
    if (args.isEmpty()) {
        throwError("No args")
        return null
    }
    val nameOperation = args[0].trim()
    if (!FUNCTIONS.containsKey(nameOperation)) {
        throwError("Dont know $nameOperation operation")
        return null
    }
    if (args.size != FUNCTIONS[nameOperation]?.plus(1)) {
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

enum class STATE {
    CONTINUE, RETURN
}

fun doOperation(input: List<String>) : STATE {
    val operation = readArgs(input) ?: return STATE.CONTINUE
    when (operation.nameOperation) {
        "exit" -> {db.exit(); return STATE.RETURN}
        "containsKey" -> db.containsKey(operation.key)
        "get" -> db.get(operation.key)
        "set" -> db.set(operation.key, operation.value)
        "removeKey" -> db.removeKey(operation.key)
        "size" -> db.size()
        "is-empty" -> db.isEmpty()
        "clear" -> db.clear()
        "values" -> db.values()
        else -> {throwError("Forgot to add function"); return STATE.RETURN}
    }
    return STATE.CONTINUE
}

/** Read commands from run args and do them */

fun startOperationFromArgs(args : Array<String>) {
    var currentIterator = 0
    while (currentIterator < args.size) {
        val nameOperation = args[currentIterator].trim()
        if (!FUNCTIONS.containsKey(nameOperation)) {
            throwError("Dont know $nameOperation operation")
            return
        }
        val needArguments = FUNCTIONS[nameOperation]!!
        if (currentIterator + needArguments > args.size) {
            throwError("Wrong count of data")
            return
        }
        doOperation(args.copyOfRange(currentIterator, currentIterator + needArguments + 1).toList())
        currentIterator += needArguments + 1
    }
    db.exit()
}

/** Just read commands and do it */

fun startOperation(args : Array<String>) {
    if (args.isNotEmpty()) {
        startOperationFromArgs(args)
        return
    }
    var str: String?
    while (true) {
        str = readLine()
        if (str == null) {
            continue
        }
        val input = str.split(' ').filter { it.isNotBlank() }
        val state = doOperation(input)
        if (state == STATE.RETURN) {
            return
        }
    }
}
