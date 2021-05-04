package linc.com.keypad

import android.graphics.Color
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import linc.com.keypad.Constants.BOTTOM
import linc.com.keypad.Constants.END
import linc.com.keypad.Constants.SIDE_COUNT
import linc.com.keypad.Constants.START
import linc.com.keypad.Constants.TOP

class KeypadConfig private constructor(
        // TODO: 04.05.21 validate params
        internal var textSize: Float = 45f,
        internal var textStyle: Int = Typeface.NORMAL,
        internal var textFont: Typeface = Typeface.DEFAULT,
        internal var enableKeyRipple: Boolean = true,
        internal var contentColor: ConfigColorWrapper = ConfigColorWrapper(Color.BLACK),
        internal var contentPadding: IntArray = IntArray(SIDE_COUNT) { 0 },
        internal var contentMargin: IntArray = IntArray(SIDE_COUNT) { 0 },
        internal var contentBackground: ChangeableWrapper<Int> = ChangeableWrapper(0, ChangeableWrapper.State.DEFAULT),
        internal var height: ChangeableWrapper<Int> = ChangeableWrapper(0, ChangeableWrapper.State.DEFAULT),
        internal var width: ChangeableWrapper<Int> = ChangeableWrapper(0, ChangeableWrapper.State.DEFAULT),
) {

    // TODO: 04.05.21 hardcoded to const
    private var customKeys = mutableListOf(
            CustomKey.getInstance("*", CustomKey.Key.LEFT),
            CustomKey.getInstance("#", CustomKey.Key.RIGHT)
    )

    fun hideCustomKey(key: CustomKey.Key, hide: Boolean) {
        getCustomKey(key)?.hide = hide
    }

    fun setCustomKey(customKey: CustomKey) {
        getCustomKey(customKey.key)?.updateKey(customKey)
    }

    fun setKeyTextSize(sizeSp: Float) {
        textSize = sizeSp
        customKeys.forEach { it.textSize.setGlobal(sizeSp) }
    }

    fun setKeyTextStyle(typefaceStyle: Int) {
        textStyle = typefaceStyle
        customKeys.forEach { it.textStyle.setGlobal(typefaceStyle) }
    }

    fun setKeyTextFont(font: Typeface) {
        textFont = font
        customKeys.forEach { it.textFont.setGlobal(font) }
    }

    fun enableKeyRipple(enable: Boolean) {
        enableKeyRipple = enable
        customKeys.forEach { it.enableKeyRipple.setGlobal(enable) }
    }

    fun setKeypadHeightPercent(percent: Int) {
        height.apply {
            value = ScreenManager.getHeightByPercent(percent)
            state = ChangeableWrapper.State.CHANGED
        }
    }

    fun setKeypadWidthPercent(percent: Int) {
        width.apply {
            value = ScreenManager.getWidthByPercent(percent)
            state = ChangeableWrapper.State.CHANGED
        }
    }

    fun setContentPadding(padding: Int) {
        copyDimenValues(contentPadding, padding, padding, padding, padding)
        customKeys.forEach { it.contentPadding.setGlobal(contentPadding) }
    }

    fun setContentPadding(top: Int, bottom: Int, start: Int, end: Int) {
        copyDimenValues(contentPadding, top, bottom, start, end)
        customKeys.forEach { it.contentPadding.setGlobal(contentPadding) }
    }

    fun setContentMargin(margin: Int) {
        copyDimenValues(contentMargin, margin, margin, margin, margin)
        customKeys.forEach { it.contentMargin.setGlobal(contentMargin) }
    }

    fun setContentMargin(top: Int, bottom: Int, start: Int, end: Int) {
        copyDimenValues(contentMargin, top, bottom, start, end)
        customKeys.forEach { it.contentMargin.setGlobal(contentMargin) }
    }

    fun setKeyContentColorInt(@ColorInt colorInt: Int) {
        val color = ConfigColorWrapper(colorInt, ConfigColorWrapper.ColorSource.INT)
        contentColor = color
        customKeys.forEach { it.contentColor.setGlobal(color) }
    }

    fun setKeyContentColorRes(@ColorRes colorRes: Int) {
        val color = ConfigColorWrapper(colorRes, ConfigColorWrapper.ColorSource.RES)
        contentColor = color
        customKeys.forEach { it.contentColor.setGlobal(color) }
    }

    fun setKeyContentBackground(@DrawableRes drawable: Int) {
        contentBackground.apply {
            value = drawable
            state = ChangeableWrapper.State.CHANGED
        }
    }

    internal fun getCustomKey(key: CustomKey.Key) = customKeys.find { it.key == key }

    companion object {
        fun getInstance() = KeypadConfig()
    }
}
