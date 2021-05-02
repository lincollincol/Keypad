package linc.com.keypad

data class CustomKey(
        var value: Any,
        val key: Key,
        var type: ContentType = ContentType.TEXT,
        internal var hide: Boolean = false
) {
    enum class Key(internal val position: Int) { LEFT(0), RIGHT(2) }
    enum class ContentType { TEXT, IMAGE }
}