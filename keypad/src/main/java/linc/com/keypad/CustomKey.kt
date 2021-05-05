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
        internal var keyHeight: Overridable<Int> = Overridable.getInstance(0),
        internal var keyWidth: Overridable<Int> = Overridable.getInstance(0),
        internal var keyPadding: Overridable<IntArray> = Overridable.getInstance(IntArray(Constants.SIDE_COUNT) { 0 }),
        internal var keyMargin: Overridable<IntArray> = Overridable.getInstance(IntArray(Constants.SIDE_COUNT) { 0 }),
        internal var contentColor: Overridable<ConfigColorWrapper> = Overridable.getInstance(ConfigColorWrapper(Color.BLACK)),
        internal var hide: Boolean = false
) {

    fun setKeyTextSize(sizeSp: Float) = textSize.setLocal(sizeSp)
    fun setKeyTextStyle(typefaceStyle: Int) = textStyle.setLocal(typefaceStyle)
    fun setKeyTextFont(font: Typeface) = textFont.setLocal(font)
    fun enableKeyRipple(enable: Boolean) = enableKeyRipple.setLocal(enable)

    fun setKeyPadding(padding: Int) = keyPadding.modifyLocal {
        copyDimenValues(it, padding, padding, padding, padding)
    }

    fun setKeyPadding(top: Int, bottom: Int, start: Int, end: Int) = keyPadding.modifyLocal {
        copyDimenValues(it, top, bottom, start, end)
    }

    fun setKeyMargin(margin: Int) = keyMargin.modifyLocal {
        copyDimenValues(it, margin, margin, margin, margin)
    }

    fun setKeyMargin(top: Int, bottom: Int, start: Int, end: Int) = keyMargin.modifyLocal {
        copyDimenValues(it, top, bottom, start, end)
    }

    @Deprecated("Incorrect layout", ReplaceWith("KeypadConfig.setKeyHeight()"), DeprecationLevel.HIDDEN)
    fun setKeyHeight(dp: Int) = keyHeight.setLocal(dp)

    @Deprecated("Incorrect layout", ReplaceWith("KeypadConfig.setKeyWidth()"), DeprecationLevel.HIDDEN)
    fun setKeyWidth(dp: Int) = keyWidth.setLocal(dp)

    @Deprecated("Incorrect layout", ReplaceWith("KeypadConfig.setKeySize()"), DeprecationLevel.HIDDEN)
    fun setKeySize(widthDp: Int, heightDp: Int) {
//        setKeyHeight(heightDp)
//        setKeyWidth(widthDp)
    }

    fun setKeyContentColorInt(@ColorInt colorInt: Int) =
        contentColor.setLocal(ConfigColorWrapper(colorInt, ConfigColorWrapper.ColorSource.INT))

    fun setKeyContentColorRes(@ColorRes colorRes: Int) =
        contentColor.setLocal(ConfigColorWrapper(colorRes, ConfigColorWrapper.ColorSource.RES))

    internal fun updateKey(src: CustomKey) {
        value = src.value
        type = src.type
        textSize.copy(src.textSize)
        textStyle.copy(src.textStyle)
        textFont.copy(src.textFont)
        enableKeyRipple.copy(src.enableKeyRipple)
        contentColor.copy(src.contentColor)
        keyPadding.copy(src.keyPadding)
        keyMargin.copy(src.keyMargin)
//        keyHeight.copy(src.keyHeight)
//        keyWidth.copy(src.keyWidth)
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