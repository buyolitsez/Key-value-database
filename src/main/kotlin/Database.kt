import java.io.File
import java.io.InputStream

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

    /** Return file from it number */
    private fun getFileByNum(num: Int): File {
        val file = File("$PATH_DATA_DIRECTORY$num")
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    /** get size of record in bytes
    KEY=VALUE\n = key. Length + separator + value. Length + new line character
     * */
    private fun getSizeOfRecord(key: ULong): ULong {
        require(data.containsKey(key)) { "data must contains key($key)" }
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
        val listFromPath = File(PATH_DATA_DIRECTORY).list()
        require(listFromPath != null) { "Unknown state with directory $PATH_DATA_DIRECTORY" }
        totalCountOfFiles = listFromPath.size
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
        require(totalCountOfFiles > 0)
        require(currentFile >= 0)
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

    private fun iterateOverDatabase(
        key: ULong,
        value: String,
        checkStatement: (ULong, String) -> Boolean,
        DoIf: (Boolean) -> Unit
    ) {
        val startNum = currentFile
        do {
            if (checkStatement(key, value)) {
                DoIf(true)
                return
            }
            getNextPart()
        } while (currentFile != startNum)
        DoIf(false)
    }

    /** Output "true" if [key] is in database, and "false" otherwise*/
    fun containsKey(key: ULong): Boolean {
        var result = false
        iterateOverDatabase(key, "-", fun(key: ULong, _: String): Boolean {
            return data.contains(key)
        }, fun(statement: Boolean) {
            result = statement
        })
        outputString(result.toString())
        return result
    }

    /** Output value, is [key] is in database, and "No such key" otherwise. */
    fun get(key: ULong): String {
        var result = "No such key"
        iterateOverDatabase(key, "-", fun(key: ULong, _: String): Boolean {
            return data.contains(key)
        }, fun(statement: Boolean) {
            if (statement) {
                result = data[key]!!.toString()
                outputString(result)
            } else {
                outputString(result)
                result = ""
            }
        })
        return result
    }

    /** Set [key] to [value] in database*/
    fun set(key: ULong, value: String) {
        if (getSizeOfRecord(key, value) >= MAX_FILE_SIZE) {
            throwError("Too big record, to write in a one file\n $key=$value")
            return
        }
        var notFullPart = 0
        iterateOverDatabase(key, value, fun(key: ULong, value: String): Boolean {
            if (data.contains(key)) {
                data[key] = value
                return true
            }
            if (currentSizeInBytes + getSizeOfRecord(key, value) < MAX_FILE_SIZE) {
                notFullPart = currentFile
            }
            return false
        }, fun(statement: Boolean) {
            if (!statement) {
                if (notFullPart == 0) {
                    createNewPart()
                } else {
                    uploadPartDatabase()
                    loadPartDatabaseFromFile(notFullPart)
                }
                data[key] = value
                currentSizeInBytes += getSizeOfRecord(key, value)
            }
        })
    }

    /** Remove value by [key]
     * If there is no [key], we don't do anything.*/
    fun removeKey(key: ULong) {
        iterateOverDatabase(key, "-", fun(key: ULong, _: String): Boolean {
            return data.contains(key)
        }, fun(statement: Boolean) {
            if (statement) {
                currentSizeInBytes -= getSizeOfRecord(key)
                data.remove(key)
            }
        })
    }

    /** Output size of database*/
    fun size(): ULong {
        var totalSize: ULong = 0U
        iterateOverDatabase(0U, "-", fun(key: ULong, _: String): Boolean {
            totalSize += data.size.toULong()
            return false
        },
            fun(_: Boolean) {})
        outputString(totalSize.toString())
        return totalSize
    }

    /** Output "true" if database is empty and "false" otherwise. */
    fun isEmpty(): Boolean {
        var result = true
        iterateOverDatabase(0U, "-", fun(_: ULong, _: String): Boolean { return data.isNotEmpty() },
            fun(statement: Boolean) {
                result = !statement
            })
        outputString(result.toString())
        return result
    }

    /** Remove all records in database*/
    fun clear() {
        iterateOverDatabase(0U, "-", fun(_: ULong, _: String): Boolean {
            data.clear()
            return false
        }, fun(_: Boolean) {})
        correctDirectory()
        totalCountOfFiles = 1
    }

    /** Output all database values */
    fun outputValues() {
        iterateOverDatabase(0U, "-", fun(_: ULong, _: String): Boolean {
            for ((_, value) in data) {
                outputString(value)
            }
            return false
        }, fun(_: Boolean) {})
    }
}

