package linc.com.keypad

internal data class SizeWrapper(
        var value: Int,
        var state: State
) {
    enum class State { CHANGED, DEFAULT }
}