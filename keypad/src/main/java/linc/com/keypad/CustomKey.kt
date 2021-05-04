package linc.com.keypad

import android.graphics.Color
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

class CustomKey private constructor(
        internal var value: Any,
        internal var type: ContentType,
        internal val key: Key,
        internal var textSize: Overridable<Float> = Overridable.getInstance(45f),
        internal var textStyle: Overridable<Int> = Overridable.getInstance(Typeface.NORMAL),
        internal var textFont: Overridable<Typeface> = Overridable.getInstance(Typeface.DEFAULT),
        internal var enableKeyRipple: Overridable<Boolean> = Overridable.getInstance(true),
        internal var contentPadding: Overridable<Int> = Overridable.getInstance(0),
        internal var contentColor: Overridable<ConfigColorWrapper> = Overridable.getInstance(ConfigColorWrapper(Color.BLACK)),
        internal var hide: Boolean = false
) {

    fun setKeyTextSize(sizeSp: Float) = textSize.setLocal(sizeSp)
    fun setKeyTextStyle(typefaceStyle: Int) = textStyle.setLocal(typefaceStyle)
    fun setKeyTextFont(font: Typeface) = textFont.setLocal(font)
    fun enableKeyRipple(enable: Boolean) = enableKeyRipple.setLocal(enable)
    fun setContentPadding(padding: Int) = contentPadding.setLocal(padding)

    fun setKeyContentColorInt(@ColorInt colorInt: Int) =
        contentColor.setLocal(ConfigColorWrapper(colorInt, ConfigColorWrapper.ColorSource.INT))

    fun setKeyContentColorRes(@ColorRes colorRes: Int) =
        contentColor.setLocal(ConfigColorWrapper(colorRes, ConfigColorWrapper.ColorSource.RES))

    internal fun updateKey(src: CustomKey) {
        value = src.value
        type = src.type
        contentPadding.copy(src.contentPadding)
        textSize.copy(src.textSize)
        textStyle.copy(src.textStyle)
        textFont.copy(src.textFont)
        enableKeyRipple.copy(src.enableKeyRipple)
        contentColor.copy(src.contentColor)
    }

    internal fun getKeyContent() : Content {
        return Content(value, key, type)
    }

    data class Content internal constructor(
            val value: Any,
            val key: Key,
            val contentType: ContentType
    )

    enum class Key(internal val position: Int) {
        LEFT(0), RIGHT(2)
    }

    enum class ContentType {
        TEXT, IMAGE
    }

    companion object {
        fun getInstance(value: String, key: Key) =
                CustomKey(value, ContentType.TEXT, key)

        fun getInstance(@DrawableRes value: Int, key: Key) =
                CustomKey(value, ContentType.IMAGE, key)
    }
}