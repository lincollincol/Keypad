package linc.com.keypad

internal data class ChangeableWrapper<T>(
        var value: T,
        var state: State
) {
    fun changeDefault(value: T) {
        this.value = value
        this.state = State.CHANGED
    }
    enum class State { CHANGED, DEFAULT }
}