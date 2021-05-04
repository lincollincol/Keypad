package linc.com.keypad

internal data class ConfigColorWrapper(
        var color: Int,
        var colorSource: ColorSource = ColorSource.INT,
) {
    enum class ColorSource { RES, INT }
}