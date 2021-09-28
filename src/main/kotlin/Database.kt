import java.io.File
import java.io.InputStream

/** Return file from it number */
fun getFileByNum(num: Int): File {
    val file = File("$PATH_DATA_DIRECTORY$num")
    if (!file.exists()) {
        file.createNewFile()
    }
    return file
}

class Database {
    // 0 means that data is not loaded
    private var currentFile = 0
    var data = HashMap<ULong, String>()
    var totalCountOfFiles = 0

    init {
        if (PATH_DATA_DIRECTORY.last() != '/') {
            PATH_DATA_DIRECTORY += "/"
        }
        correctDirectory()
        calculateNumberOfFiles()
        loadPartDatabaseFromFile(1)
    }

    /** Reduce files in path */
    private fun correctDirectory() {
        deleteEmptyFiles()
    }

    private fun deleteEmptyFiles() {
        if (!File(PATH_DATA_DIRECTORY).exists()) {
            File(PATH_DATA_DIRECTORY).mkdir()
        }
        if (File(PATH_DATA_DIRECTORY).list() == null) {
            throwError("Unknown state with directory $PATH_DATA_DIRECTORY", true)
            return
        }
        var currentNumber = 1
        File(PATH_DATA_DIRECTORY).walk().sorted().drop(1).forEach { file ->
            if (file.length() != 0.toLong()) {
                file.renameTo(File("$PATH_DATA_DIRECTORY$currentNumber"))
                currentNumber++
            } else {
                file.delete()
            }
        }
    }

    /** calculate number of files and write it into [totalCountOfFiles]
     * If there is no files, we suppose that there is one
     */
    fun calculateNumberOfFiles() {
        totalCountOfFiles = File(PATH_DATA_DIRECTORY).list()!!.size
        if (totalCountOfFiles == 0) {
            currentFile = 0
            totalCountOfFiles = 1
        }
    }

    /** Create new database file */
    private fun createNewPart() {
        uploadPartDatabase()
        totalCountOfFiles++
        loadPartDatabaseFromFile(totalCountOfFiles)
    }

    /** Load database part of database with number [num]*/
    fun loadPartDatabaseFromFile(num: Int) {
        uploadPartDatabase()
        val file = getFileByNum(num)
        currentFile = num
        val inputStream: InputStream = file.inputStream()
        inputStream.bufferedReader().forEachLine { str ->
            if (str.count { it == SEPARATOR } != 1) {
                throwError("Wrong record\n $str", true)
                return@forEachLine
            }
            val (key, value) = str.split(SEPARATOR)
            data[key.toULong()] = value
            if (data.size >= MAX_RECORDS_FILE) {
                throwError("File($num) is too big", true)
                return@forEachLine
            }
        }
    }

    /** Upload database part [data] to file with number [currentFile]*/
    fun uploadPartDatabase() {
        if (currentFile == 0) {
            return
        }
        val file = getFileByNum(currentFile)
        val output = StringBuilder()
        for ((key, value) in data) {
            output.append("$key$SEPARATOR$value\n")
        }
        file.writeText(output.toString())
        currentFile = 0
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
        loadPartDatabaseFromFile(currentFile % totalCountOfFiles + 1)
    }

    fun exit() {
        outputString("End")
        uploadPartDatabase()
        correctDirectory()
    }

    fun containsKey(key: ULong) {
        val startNum = currentFile
        do {
            if (data.contains(key)) {
                outputString("true")
                return
            }
            getNextPart()
        } while (currentFile != startNum)
        outputString("false")
    }

    fun get(key: ULong) {
        val startNum = currentFile
        do {
            if (data.contains(key)) {
                outputString(data[key].toString())
                return
            }
            getNextPart()
        } while (currentFile != startNum)
        outputString("No such key")
    }

    fun set(key: ULong, value: String) {
        val startNum = currentFile
        // Try to find key
        do {
            if (data.contains(key)) {
                data[key] = value
                return
            }
            getNextPart()
        } while (currentFile != startNum)
        // Try to find not empty data
        do {
            if (data.size != MAX_RECORDS_FILE - 1) {
                break
            }
            getNextPart()
        } while (currentFile != startNum)
        if (data.size == MAX_RECORDS_FILE - 1) {
            createNewPart()
        }
        data[key] = value
    }

    fun removeKey(key: ULong) {
        val startNum = currentFile
        do {
            if (data.contains(key)) {
                data.remove(key)
                return
            }
            getNextPart()
        } while (currentFile != startNum)
    }

    fun size() {
        var totalSize: ULong = 0.toULong()
        val startNum = currentFile
        do {
            totalSize += data.size.toULong()
            getNextPart()
        } while (currentFile != startNum)
        outputString(totalSize.toString())
    }

    fun isEmpty() {
        val startNum = currentFile
        do {
            if (data.isNotEmpty()) {
                outputString("false")
                return
            }
            getNextPart()
        } while (currentFile != startNum)
        outputString("true")
    }

    fun clear() {
        val startNum = currentFile
        do {
            data.clear()
            getNextPart()
        } while (currentFile != startNum)
    }

    fun values() {
        val startNum = currentFile
        do {
            for ((_, value) in data) {
                outputString(value)
            }
            getNextPart()
        } while (currentFile != startNum)
    }
}

