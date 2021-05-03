package linc.com.keypad

import android.graphics.Color
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

class CustomKey private constructor(
        val key: Key,
        internal var padding: Overridable<Int> = Overridable.getInstance(0),
        internal var textSize: Overridable<Float> = Overridable.getInstance(45f),
        internal var textStyle: Overridable<Int> = Overridable.getInstance(Typeface.NORMAL),
        internal var textFont: Overridable<Typeface> = Overridable.getInstance(Typeface.DEFAULT),
        internal var enableKeyRipple: Overridable<Boolean> = Overridable.getInstance(true),
        internal var contentColor: Overridable<ConfigColorWrapper> = Overridable.getInstance(ConfigColorWrapper(Color.BLACK)),
        internal var hide: Boolean = false
) {

    var value = Any()
        private set
    var type = ContentType.TEXT
        private set

    fun setKeyPadding(padding: Int) = this.padding.setLocal(padding)
    fun setKeyTextSize(sizeSp: Float) = textSize.setLocal(sizeSp)
    fun setKeyTextStyle(typefaceStyle: Int) = textStyle.setLocal(typefaceStyle)
    fun setKeyTextFont(font: Typeface) = textFont.setLocal(font)
    fun enableKeyRipple(enable: Boolean) = enableKeyRipple.setLocal(enable)

    fun setKeyContentColorInt(@ColorInt colorInt: Int) =
        contentColor.setLocal(ConfigColorWrapper(colorInt, ConfigColorWrapper.ColorSource.INT))

    fun setKeyContentColorRes(@ColorRes colorRes: Int) =
        contentColor.setLocal(ConfigColorWrapper(colorRes, ConfigColorWrapper.ColorSource.RES))

    internal fun updateKey(src: CustomKey) {
        value = src.value
        type = src.type
        padding.copy(src.padding)
        textSize.copy(src.textSize)
        textStyle.copy(src.textStyle)
        textFont.copy(src.textFont)
        enableKeyRipple.copy(src.enableKeyRipple)
        contentColor.copy(src.contentColor)
    }

    enum class Key(internal val position: Int) {
        LEFT(0), RIGHT(2)
    }

    enum class ContentType {
        TEXT, IMAGE
    }

    companion object {
        fun getInstance(value: String, key: Key) = CustomKey(key).apply {
            this.value = value
            this.type = ContentType.TEXT
        }

        fun getInstance(@DrawableRes value: Int, key: Key) = CustomKey(key).apply {
            this.value = value
            this.type = ContentType.IMAGE
        }
    }
}