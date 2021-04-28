package linc.com.keypad

internal data class CustomKey<T>(
    val value: T,
    val type: Int = TEXT,
    var hide: Boolean = false
) {
    internal companion object {
        const val TEXT = 0
        const val IMAGE = 1
    }
}