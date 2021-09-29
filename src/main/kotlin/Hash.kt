fun getHash(key: String): ULong {
    val coefficient: ULong = 97U
    val mod: ULong = 576460752303423619U // big prime module ~= 5e17
    var hash: ULong = 0U
    key.forEach {
        hash = (hash * coefficient + it.code.toULong()) % mod
    }
    return hash
}