/** Name of function and count of arguments*/
val FUNCTIONS = mapOf(
    "exit" to 0,
    "containsKey" to 1,
    "get" to 1,
    "set" to 2,
    "removeKey" to 1,
    "size" to 0,
    "is-empty" to 0,
    "clear" to 0,
    "values" to 0
)

/** Convert equal function but with different names to one name */
val NAME_OF_FUNCTIONS = mapOf(
    "exit" to "exit",

    "containsKey" to "containsKey",
    "find" to "containsKey",

    "get" to "get",

    "set" to "set",
    "change" to "set",

    "removeKey" to "removeKey",
    "remove" to "removeKey",
    "delete" to "removeKey",

    "size" to "size",
    "len" to "size",
    "length" to "size",

    "is-empty" to "is-empty",
    "isEmpty" to "is-empty",
    "empty" to "is-empty",

    "clear" to "clear",
    "flush" to "clear",

    "values" to "values",
    "value" to "values"
)

/** Enable output for all functions */
var OUTPUT = true

/** Separator in every file*/
var SEPARATOR = '='

/** Maximum size of file in Bytes */
var MAX_FILE_SIZE: ULong = 3000000000000U

var PATH_DATA_DIRECTORY = "data/"

/** module for calculating hash of strings */
var MODULE: ULong = 576460752303423619U // big prime module ~= 5e17

/** coefficient for calculating hash of strings */
var COEFFICIENT: ULong = 97U

fun setDefaultValues() {
    loadSettingsFromFile("settings")
}

