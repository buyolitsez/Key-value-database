import java.io.File
import java.io.InputStream
import kotlin.system.exitProcess

/** Return file from it number */
fun getFileByNum(num: Int): File {
    val file = File("$PATH_DATA_DIRECTORY$num")
    if (!file.exists()) {
        file.createNewFile()
    }
    return file
}

class Database() {
    // 0 means that data is not loaded
    var CURRENT_FILE = 0
    var data = HashMap<ULong, String>()
    var TOTAL_COUNT_OF_FILES = 0
    init{
        if (PATH_DATA_DIRECTORY.last() != '/') {
            PATH_DATA_DIRECTORY += "/"
        }
        calculateNumberOfFiles()
        loadPartDatabaseFromFile(1)
    }

    /** calculate number of files and write it into [TOTAL_COUNT_OF_FILES]
     * If there is no files, we suppose that there is one
     */
    fun calculateNumberOfFiles() {
        if (!File(PATH_DATA_DIRECTORY).exists()) {
            File(PATH_DATA_DIRECTORY).mkdir()
        }
        if (File(PATH_DATA_DIRECTORY).list() == null) {
            throwError("Cant calculate number of files in $PATH_DATA_DIRECTORY")
        } else {
            TOTAL_COUNT_OF_FILES = File(PATH_DATA_DIRECTORY).list()!!.size
            if (TOTAL_COUNT_OF_FILES == 0) {
                CURRENT_FILE = 0
                TOTAL_COUNT_OF_FILES = 1
            }
        }
    }

    /** Create new database file */
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
            output.append("$key$SEPARATOR$value\n")
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
    private fun getNextPart() {
        loadPartDatabaseFromFile(CURRENT_FILE % TOTAL_COUNT_OF_FILES + 1)
    }

    fun exit() {
        OutputString("End")
        uploadPartDatabase()
        exitProcess(0)
    }

    fun contains(key: ULong) {
        val startNum = CURRENT_FILE
        do {
            if (data.contains(key)) {
                OutputString("true")
                return
            }
            getNextPart()
        } while (CURRENT_FILE != startNum)
        OutputString("false")
    }

    fun get(key: ULong) {
        val startNum = CURRENT_FILE
        do {
            if (data.contains(key)) {
                OutputString(data[key].toString())
                return
            }
            getNextPart()
        } while (CURRENT_FILE != startNum)
        OutputString("No such key")
    }

    fun set(key: ULong, value: String) {
        val startNum = CURRENT_FILE
        // Try to find key
        do {
            if (data.contains(key)) {
                data[key] = value
                return
            }
            getNextPart()
        } while (CURRENT_FILE != startNum)
        // Try to find not empty data
        do {
            if (data.size != MAX_RECORDS_FILE - 1) {
                break
            }
            getNextPart()
        } while (CURRENT_FILE != startNum)
        if (data.size == MAX_RECORDS_FILE - 1) {
            createNewPart()
        }
        data[key] = value
    }

    fun remove(key: ULong) {
        val startNum = CURRENT_FILE
        do {
            if (data.contains(key)) {
                data.remove(key)
                return
            }
            getNextPart()
        } while (CURRENT_FILE != startNum)
    }

    fun size() {
        var totalSize: ULong = 0.toULong()
        val startNum = CURRENT_FILE
        do {
            totalSize += data.size.toULong()
            getNextPart()
        } while (CURRENT_FILE != startNum)
        OutputString(totalSize.toString())
    }

    fun isEmpty() {
        val startNum = CURRENT_FILE
        do {
            if (data.isNotEmpty()) {
                OutputString("false")
                return
            }
            getNextPart()
        } while (CURRENT_FILE != startNum)
        OutputString("true")
    }

    fun clear() {
        val startNum = CURRENT_FILE
        do {
            data.clear()
            getNextPart()
        } while (CURRENT_FILE != startNum)
    }

    fun values() {
        val startNum = CURRENT_FILE
        do {
            for ((_, value) in data) {
                OutputString(value)
            }
            getNextPart()
        } while (CURRENT_FILE != startNum)
    }
}

