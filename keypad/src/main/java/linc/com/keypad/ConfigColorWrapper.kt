package linc.com.keypad

internal class ConfigColorWrapper(
        var color: Int,
        var colorSource: ColorSource = ColorSource.INT,
) {
    enum class ColorSource { RES, INT }
}