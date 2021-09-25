/**
 * @property nameOperation is a name of operation
 * @property key is a key when it needed or empty string when not.
 * @property value is a value when it needed or empty string when not.
 */
data class Operation(var nameOperation : String, val key : String, val value : String)

/** Read command and values from run args
 * @property args run args
 */
fun readArgs(args: Array<String>): Operation {
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
    return Operation(nameOperation, key, value)
}

/**
 * Call function [readArgs] and then call needed function
 * @property args run args
 */
fun startOperation(args : Array<String>) {
    val operation = readArgs(args)
    if (operation.key.contains(SEPARATOR) || operation.value.contains(SEPARATOR)) {
        throwError("Key and value cant contain separator $SEPARATOR\n")
    }
    when(operation.nameOperation) {
        "contains" -> containsDB(operation.key)
        "get" -> getDB(operation.key)
        "set" -> setDB(operation.key, operation.value)
        "remove" -> removeDB(operation.key)
        "size" -> sizeDB()
        "is-empty" -> isEmptyDB()
        "clear" -> clearDB()
        "entries" -> entriesDB()
        "keys" -> keysDB()
        "values" -> valuesDB()
    }
}
