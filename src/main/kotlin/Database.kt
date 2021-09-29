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
    // 0 means that data is not loaded yet
    private var currentFile = 0
    var data = HashMap<ULong, String>()
    var totalCountOfFiles = 0
    private var currentSizeInBytes: ULong = 0U

    init {
        if (PATH_DATA_DIRECTORY.last() != '/') {
            PATH_DATA_DIRECTORY += "/"
        }
        correctDirectory()
        calculateNumberOfFiles()
        loadPartDatabaseFromFile(1)
    }

    /** get size of record in bytes
    KEY=VALUE\n = key. Length + separator + value. Length + new line character
     * */
    private fun getSizeOfRecord(key: ULong): ULong {
        return key.toString().length.toULong() + 1U + data[key]!!.length.toULong() + 1U
    }

    private fun getSizeOfRecord(key: ULong, value: String): ULong {
        return key.toString().length.toULong() + 1U + value.length.toULong() + 1U
    }

    /** Reduce files in path */
    private fun correctDirectory() {
        deleteEmptyFiles()
    }

    /** Delete empty files in [PATH_DATA_DIRECTORY] */
    private fun deleteEmptyFiles() {
        if (!File(PATH_DATA_DIRECTORY).exists()) {
            File(PATH_DATA_DIRECTORY).mkdir()
        }
        if (File(PATH_DATA_DIRECTORY).list() == null) {
            throwError("Unknown state with directory $PATH_DATA_DIRECTORY", exitProcess = true)
        }
        var currentNumberOfFiles = 1
        // drop directory name from the list, then sort file names(like number 1,2,3...,10,...), and then iterate on them
        File(PATH_DATA_DIRECTORY).walk().drop(1).sortedWith(kotlin.Comparator { num1, num2 ->
            if (num1.name.toIntOrNull() == null || num2.name.toIntOrNull() == null) {
                return@Comparator if (num1.name < num2.name) -1 else 1
            }
            return@Comparator if (num1.name.toInt() < num2.name.toInt()) -1 else 1
        }).forEach { file ->
            if (file.length() != 0.toLong()) {
                file.renameTo(File("$PATH_DATA_DIRECTORY$currentNumberOfFiles"))
                currentNumberOfFiles++
            } else {
                file.delete()
            }
        }
    }

    /** Calculate number of files and write it into [totalCountOfFiles]
     * If there is no files, we suppose that there is one(we'll create it later)
     */
    private fun calculateNumberOfFiles() {
        // We can use !! and there is can't be an error, otherwise we'll throw exception in [deleteEmptyFiles] function
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

    /** Load database part from file with number [num]*/
    private fun loadPartDatabaseFromFile(num: Int) {
        val file = getFileByNum(num)
        currentFile = num
        val inputStream: InputStream = file.inputStream()
        inputStream.bufferedReader().forEachLine { str ->
            if (str.count { it == SEPARATOR } != 1) {
                throwError("Error with separator\n $str", exitProcess = true)
            }
            val (key, value) = str.split(SEPARATOR)
            data[key.toULong()] = value
            currentSizeInBytes += getSizeOfRecord(key.toULong(), value)
        }
        if (currentSizeInBytes >= MAX_FILE_SIZE) {
            throwError("File($num) is too big", exitProcess = true)
        }
    }

    /** Upload database part [data] to file with number [currentFile]*/
    fun uploadPartDatabase() {
        // database part is not loaded
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
        currentSizeInBytes = 0U
        data.clear()
    }

    /** Load next database part in a cycle
     * For example
     * If we have 3 parts
     * After 1 part is a 2 part
     * After 2 part is a 3 part
     * After 3 part is a 1 part
     *
     * And if we have only 1 part
     * After 1 part is a 1 part
     */
    private fun getNextPart() {
        val wasNumFile = currentFile
        uploadPartDatabase()
        loadPartDatabaseFromFile(wasNumFile % totalCountOfFiles + 1)
    }

    /** Save database and then exit it*/
    fun exit() {
        outputString("End")
        uploadPartDatabase()
        correctDirectory()
    }

    /** Output "true" if [key] is in database, and "false" otherwise*/
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

    /** Output value, is [key] is in database, and "No such key" otherwise. */
    fun get(key: ULong) {
        val startNum = currentFile
        do {
            if (data.contains(key)) {
                outputString(data[key]!!)
                return
            }
            getNextPart()
        } while (currentFile != startNum)
        outputString("No such key")
    }

    /** Set [key] to [value] in database*/
    fun set(key: ULong, value: String) {
        val startNum = currentFile
        var notFullPart = 0
        // Try to find key
        do {
            if (data.contains(key)) {
                data[key] = value
                return
            }
            if (currentSizeInBytes + getSizeOfRecord(key, value) < MAX_FILE_SIZE) {
                notFullPart = currentFile
            }
            getNextPart()
        } while (currentFile != startNum)
        if (notFullPart == 0) {
            createNewPart()
        } else {
            uploadPartDatabase()
            loadPartDatabaseFromFile(notFullPart)
        }
        data[key] = value
        currentSizeInBytes += getSizeOfRecord(key, value)
    }

    /** Remove value by [key]
     * If there is no [key], we don't do anything.*/
    fun removeKey(key: ULong) {
        val startNum = currentFile
        do {
            if (data.contains(key)) {
                currentSizeInBytes -= getSizeOfRecord(key)
                data.remove(key)
                return
            }
            getNextPart()
        } while (currentFile != startNum)
    }

    /** Output size of database*/
    fun size() {
        var totalSize: ULong = 0U
        val startNum = currentFile
        do {
            totalSize += data.size.toULong()
            getNextPart()
        } while (currentFile != startNum)
        outputString(totalSize.toString())
    }

    /** Output "true" if database is empty and "false" otherwise. */
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

    /** Remove all records in database*/
    fun clear() {
        val startNum = currentFile
        do {
            data.clear()
            getNextPart()
        } while (currentFile != startNum)
        correctDirectory()
        totalCountOfFiles = 1
    }

    /** Output all database values */
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

