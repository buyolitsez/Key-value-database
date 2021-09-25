import java.io.File
import java.io.InputStream
import kotlin.system.exitProcess

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

fun getFileByNum(num: Int): File {
    val file = File("data/$num")
    if (!file.exists()) {
        file.createNewFile()
    }
    return file
}

fun createNewPart() {
    uploadPartDatabase()
    TOTAL_COUNT_OF_FILES++
    loadPartDatabaseFromFile(TOTAL_COUNT_OF_FILES)
}

/** Load database part of database with number [num]*/
fun loadPartDatabaseFromFile(num: Int) {
    uploadPartDatabase()
    val file = getFileByNum(num)
    CURRENT_FILE = num
    val inputStream: InputStream = file.inputStream()
    inputStream.bufferedReader().forEachLine { str ->
        if (str.count { it == SEPARATOR } != 1) {
            throwError("Wrong record\n $str")
        }
        val (key, value) = str.split(SEPARATOR)
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

/** Load next database part
 * For example
 * If we have 3 parts
 * After 1 pat is a 2 part
 * After 2 part is a 3 part
 * After 3 part is a 1 part
 */
fun getNextPart() {
    loadPartDatabaseFromFile(CURRENT_FILE % TOTAL_COUNT_OF_FILES + 1)
}

fun exitDB() {
    println("End")
    uploadPartDatabase()
    exitProcess(0)
}

fun containsDB(key: ULong) {
    val startNum = CURRENT_FILE
    do {
        if (data.contains(key)) {
            println("true")
            return
        }
        getNextPart()
    } while (CURRENT_FILE != startNum)
    println("false")
}

fun getDB(key: ULong) {
    val startNum = CURRENT_FILE
    do {
        if (data.contains(key)) {
            println(data[key])
            return
        }
        getNextPart()
    } while (CURRENT_FILE != startNum)
    println("No such key")
}

fun setDB(key: ULong, value: String) {
    val startNum = CURRENT_FILE
    do {
        if (data.contains(key)) {
            data[key] = value
            return
        }
        getNextPart()
    } while (CURRENT_FILE != startNum)
    if (data.size == MAX_RECORDS_FILE - 1) {
        createNewPart()
    }
    data[key] = value
}

fun removeDB(key: ULong) {
    val startNum = CURRENT_FILE
    do {
        if (data.contains(key)) {
            data.remove(key)
            return
        }
        getNextPart()
    } while (CURRENT_FILE != startNum)
}

fun sizeDB() {
    var totalSize: ULong = 0.toULong()
    val startNum = CURRENT_FILE
    do {
        totalSize += data.size.toULong()
        getNextPart()
    } while (CURRENT_FILE != startNum)
    println(totalSize)
}

fun isEmptyDB() {
    val startNum = CURRENT_FILE
    do {
        if (data.isNotEmpty()) {
            println("false")
        }
        getNextPart()
    } while (CURRENT_FILE != startNum)
    println("true")
}

fun clearDB() {
    val startNum = CURRENT_FILE
    do {
        data.clear()
        getNextPart()
    } while (CURRENT_FILE != startNum)
}

fun valuesDB() {
    val startNum = CURRENT_FILE
    do {
        for ((_, value) in data) {
            println(value)
        }
        getNextPart()
    } while (CURRENT_FILE != startNum)
}