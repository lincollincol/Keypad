package linc.com.keypad

internal data class Overridable<T>(
        private var parentValue: T,
        private var childValue: T,
        private var overridden: Boolean = false
) {

    fun getValue() = when {
        overridden -> childValue
        else ->  parentValue
    }

    fun setGlobal(value: T) {
        if(overridden.not())
            parentValue = value
    }

    fun setLocal(value: T) {
        childValue = value
        overridden = true
    }

    fun modifyLocal(block: (T) -> Unit) {
        block.invoke(childValue)
        overridden = true
    }

    fun copy(src: Overridable<T>) {
        if(src.overridden)
            setLocal(src.childValue)
    }

    companion object {
        fun <T> getInstance(parentValue: T) = Overridable(parentValue, parentValue)
    }
}
