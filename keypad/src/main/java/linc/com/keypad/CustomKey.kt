package linc.com.keypad

data class CustomKey(
        var value: Any,
        val key: Key,
        var type: ContentType = ContentType.TEXT,
        internal var hide: Boolean = false
) {
    enum class Key { LEFT, RIGHT }
    enum class ContentType { TEXT, IMAGE }
}