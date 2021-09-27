/**
 * @property nameOperation is a name of operation
 * @property key is a key when it needed or empty string when not.
 * @property value is a value when it needed or empty string when not.
 */
data class Operation(var nameOperation: String, val key: ULong, val value: String)

/** Read command and values from run args
 * @property args run args
 */
fun readArgs(args: List<String>): Operation {
    if (args.isEmpty()) {
        throwError("No args")
    }
    val nameOperation = args[0].trim()
    if (!FUNCTIONS.containsKey(nameOperation)) {
        throwError("Dont know $nameOperation operation")
    }
    if (args.size != FUNCTIONS[nameOperation]?.plus(1)) {
        throwError("Wrong count of data")
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
    }
    return Operation(nameOperation, getHash(key), value)
}

/** Just read commands and do it */

fun startOperation() {
    var str: String?
    while (true) {
        str = readLine()
        if (str == null) {
            continue
        }
        val args = str.split(' ').filter { it.isNotBlank() }
        val operation = readArgs(args)
        when (operation.nameOperation) {
            "exit" -> db.exit()
            "contains" -> db.contains(operation.key)
            "get" -> db.get(operation.key)
            "set" -> db.set(operation.key, operation.value)
            "remove" -> db.remove(operation.key)
            "size" -> db.size()
            "is-empty" -> db.isEmpty()
            "clear" -> db.clear()
            "values" -> db.values()
            else -> throwError("Forgot to add function")
        }
    }
}
