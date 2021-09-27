/** Name of function and count of arguments*/
val FUNCTIONS = mapOf(
    "exit" to 0,
    "contains" to 1,
    "get" to 1,
    "set" to 2,
    "remove" to 1,
    "size" to 0,
    "is-empty" to 0,
    "clear" to 0,
    "values" to 0
)

/** Separator in every file*/
var SEPARATOR = '='

/** The count of lines in each file less than [MAX_RECORDS_FILE]*/
var MAX_RECORDS_FILE = 3

var PATH_DATA_DIRECTORY = "data/"

fun setDefaultValues() {
    SEPARATOR = '='
    MAX_RECORDS_FILE = 3
    PATH_DATA_DIRECTORY = "data/"
}