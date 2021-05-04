package linc.com.keypad

internal data class ChangeableWrapper<T>(
        var value: T,
        var state: State
) {
    enum class State { CHANGED, DEFAULT }
}