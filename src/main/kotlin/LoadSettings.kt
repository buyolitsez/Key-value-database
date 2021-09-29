import java.io.File

fun setSeparator(value: String) {
    if (value.length != 1) {
        throwError("Wrong separator value($value)", true)
    }
    SEPARATOR = value.first()
}

fun setMaxFileSize(value: String) {
    val intValue = value.toULongOrNull()
    if (intValue == null) {
        throwError("Wrong value of max file size($value)", true)
    } else {
        MAX_FILE_SIZE = intValue
    }
}

fun setSetting(name: String, value: String) {
    when (name) {
        "SEPARATOR" -> setSeparator(value)
        "MAX_FILE_SIZE" -> setMaxFileSize(value)
        "PATH_DATA_DIRECTORY" -> PATH_DATA_DIRECTORY = value
        else -> throwError("Unknown option $name", true)
    }
}

fun loadSettingsFromFile(fileName: String) {
    for (str in File(fileName).readLines()) {
        if (str.isBlank()) continue
        val arg = str.filter { !it.isWhitespace() }
        val optionName = arg.substringBefore('=')
        val optionValue = arg.substringAfter('=')
        setSetting(optionName, optionValue)
    }
}
