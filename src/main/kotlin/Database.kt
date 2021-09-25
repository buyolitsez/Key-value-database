import java.io.File
import java.io.InputStream

// 0 means that data is not loaded
var CURRENT_FILE = 0
var data = HashMap<ULong, String>()
var TOTAL_COUNT_OF_FILES = 0

fun startDatabase() {
    calculateNumberOfFiles()
    loadPartDatabaseFromFile(1)
}

/** calculate number of files and write it into [TOTAL_COUNT_OF_FILES]*/
fun calculateNumberOfFiles() {
    TOTAL_COUNT_OF_FILES = File("data/").list().size
}

fun getFileByNum(num : Int) : File {
    val file = File("data/$num")
    if (!file.exists()) {
        throwError("File($num) not exist")
    }
    return file
}

/** Load database part of database with number [num]*/
fun loadPartDatabaseFromFile(num : Int) {
    uploadPartDatabase()
    val file = getFileByNum(num)
    CURRENT_FILE = num;
    val inputStream: InputStream = file.inputStream()
    inputStream.bufferedReader().forEachLine {
        if (it.count { it == SEPARATOR } != 1) {
            throwError("Wrong record\n $it")
        }
        val (key, value) = it.split(SEPARATOR)
        data[key.toULong()] = value
        if (data.size >= MAX_RECORDS_FILE) {
            throwError("File($num) is too big")
        }
    }
}

/** Upload database part [data] to file with number [CURRENT_FILE]*/
fun uploadPartDatabase() {
    if (CURRENT_FILE == 0) {
        return
    }
    val file = getFileByNum(CURRENT_FILE)
    val output = StringBuilder()
    for ((key, value) in data) {
        output.append("$key=$value\n")
    }
    file.writeText(output.toString())
    CURRENT_FILE = 0
    data.clear()
}

fun containsDB(key: String) {

}

fun getDB(key: String) {

}

fun setDB(key: String, value: String) {

}

fun removeDB(key: String) {

}

fun sizeDB() {

}

fun isEmptyDB() {

}

fun clearDB() {

}

fun entriesDB() {

}

fun keysDB() {

}

fun valuesDB() {

}