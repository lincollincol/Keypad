package linc.com.keypad

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

class CustomKey internal constructor(
        internal var value: Any,
        internal var key: Key,
        internal var type: ContentType,
        internal var useGlobalConfig: Boolean,
        internal var padding: Int = 0,
        internal var enableKeyRipple: Boolean = true,
        internal var contentColor: ConfigColorWrapper = ConfigColorWrapper(Color.BLACK),
        internal var hide: Boolean = false
) {

    /*fun setContent(value: Any, key: Key, type: ContentType, useGlobalConfig: Boolean = true) {
        this.value = value
        this.key = key
        this.type = type
        this.useGlobalConfig = useGlobalConfig
    }*/

    fun setKeyPadding(padding: Int) {
        this.padding = padding
    }

    fun enableKeyRipple(enable: Boolean) {
        enableKeyRipple = enable
    }

    fun setKeyContentColorInt(@ColorInt colorInt: Int) {
        contentColor.apply {
            color = colorInt
            colorSource = ConfigColorWrapper.ColorSource.INT
        }
    }

    fun setKeyContentColorRes(@ColorRes colorRes: Int) {
        contentColor.apply {
            color = colorRes
            colorSource = ConfigColorWrapper.ColorSource.RES
        }
    }

    enum class Key(internal val position: Int) {
        LEFT(0), RIGHT(2)
    }

    internal enum class ContentType {
        TEXT, IMAGE
    }

    companion object {
        fun getInstance(value: String, key: Key, useGlobalConfig: Boolean = true) = CustomKey(
                value, key, ContentType.TEXT, useGlobalConfig
        )

        fun getInstance(@DrawableRes value: Int, key: Key, useGlobalConfig: Boolean = true) = CustomKey(
                value, key, ContentType.IMAGE, useGlobalConfig
        )
    }
}