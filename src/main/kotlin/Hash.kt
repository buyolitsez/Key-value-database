fun getHash(key: String): ULong {
    var hash: ULong = 0U
    key.forEach {
        hash = (hash * COEFFICIENT + it.code.toULong()) % MODULE
    }
    return hash
}