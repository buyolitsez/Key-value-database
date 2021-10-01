import java.io.File

fun getBooleanFromString(value: String): Boolean {
    return when (value) {
        "false" -> false
        "true" -> true
        else -> {
            throwError("Wrong value, should be Boolean type($value)", true); false
        }
    }
}

fun getCharFromString(value: String): Char {
    if (value.length != 1) {
        throwError("Wrong separator value($value)", true)
    }
    return value.first()
}

/** Convert string[value] to ULong or throw exception */
fun getULongFromString(value: String): ULong {
    val intValue = value.toULongOrNull()
    if (intValue == null) {
        throwError("Wrong value, should be ULong type($value)", true)
    }
    return intValue!!
}

fun setSetting(name: String, value: String) {
    when (name) {
        "OUTPUT" -> OUTPUT = getBooleanFromString(value)
        "SEPARATOR" -> SEPARATOR = getCharFromString(value)
        "MAX_FILE_SIZE" -> MAX_FILE_SIZE = getULongFromString(value)
        "PATH_DATA_DIRECTORY" -> PATH_DATA_DIRECTORY = value
        "MODULE" -> MODULE = getULongFromString(value)
        "COEFFICIENT" -> COEFFICIENT = getULongFromString(value)
        else -> throwError("Unknown option $name", true)
    }
}

fun loadSettingsFromFile(fileName: String) {
    for (str in File(fileName).readLines().map { it.substringBefore('#') }) {
        if (str.isBlank()) continue
        val arg = str.filter { !it.isWhitespace() }
        val optionName = arg.substringBefore('=')
        val optionValue = arg.substringAfter('=')
        setSetting(optionName, optionValue)
    }
}
