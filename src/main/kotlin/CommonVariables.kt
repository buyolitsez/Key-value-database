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

/** Separator in every file*/
var SEPARATOR = '='

/** Maximum size of file in Bytes */
var MAX_FILE_SIZE = 30

var PATH_DATA_DIRECTORY = "data/"

fun setDefaultValues() {
    SEPARATOR = '='
    MAX_FILE_SIZE = 30
    PATH_DATA_DIRECTORY = "data/"
}