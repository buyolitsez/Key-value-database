fun getHash(key: String): ULong {
    val coefficient: ULong = 97.toULong()
    val mod: ULong = 576460752303423619.toULong() // big prime module ~= 5e17
    var hash: ULong = 0u
    key.forEach {
        hash = (hash * coefficient + it.code.toULong()) % mod
    }
    return hash
}