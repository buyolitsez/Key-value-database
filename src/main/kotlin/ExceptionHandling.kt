fun throwError(error: String, exitDatabase : Boolean = false) {
    System.err.println(error)
    if (exitDatabase) {
        db.exit()
    }
}