package linc.com.keypad

import android.graphics.Color
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import linc.com.keypad.Constants.SIDE_COUNT

class KeypadConfig private constructor(
        // TODO: 04.05.21 validate params
        internal var textSize: Float = 45f,
        internal var textStyle: Int = Typeface.NORMAL,
        internal var textFont: Typeface = Typeface.DEFAULT,
        internal var enableKeyRipple: Boolean = true,
        internal var keyHeight: Int = 0,
        internal var keyWidth: Int = 0,
        internal var keyPadding: IntArray = IntArray(SIDE_COUNT) { 0 },
        internal var keyMargin: IntArray = IntArray(SIDE_COUNT) { 0 },
        internal var contentColor: ConfigColorWrapper = ConfigColorWrapper(Color.BLACK),
        internal var contentBackground: ChangeableWrapper<Int> = ChangeableWrapper(0, ChangeableWrapper.State.DEFAULT),
        internal var keypadHeight: ChangeableWrapper<Int> = ChangeableWrapper(0, ChangeableWrapper.State.DEFAULT),
        internal var keypadWidth: ChangeableWrapper<Int> = ChangeableWrapper(0, ChangeableWrapper.State.DEFAULT),

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
        keypadHeight.changeDefault(ScreenManager.getHeightByPercent(percent))
    }

    fun setKeypadWidthPercent(percent: Int) {
        keypadWidth.changeDefault(ScreenManager.getWidthByPercent(percent))
    }

    fun setKeyPadding(padding: Int) {
        copyDimenValues(keyPadding, padding, padding, padding, padding)
        customKeys.forEach { it.keyPadding.setGlobal(keyPadding) }
    }

    fun setKeyPadding(top: Int, bottom: Int, start: Int, end: Int) {
        copyDimenValues(keyPadding, top, bottom, start, end)
        customKeys.forEach { it.keyPadding.setGlobal(keyPadding) }
    }

    fun setKeyMargin(margin: Int) {
        copyDimenValues(keyMargin, margin, margin, margin, margin)
        customKeys.forEach { it.keyMargin.setGlobal(keyMargin) }
    }

    fun setKeyMargin(top: Int, bottom: Int, start: Int, end: Int) {
        copyDimenValues(keyMargin, top, bottom, start, end)
        customKeys.forEach { it.keyMargin.setGlobal(keyMargin) }
    }

    fun setKeyHeight(dp: Int) {
        keyHeight = dp
        customKeys.forEach { it.keyHeight.setGlobal(dp) }
    }

    fun setKeyWidth(dp: Int) {
        keyWidth = dp
        customKeys.forEach { it.keyWidth.setGlobal(dp) }
    }

    fun setKeySize(widthDp: Int, heightDp: Int) {
        setKeyHeight(heightDp)
        setKeyWidth(widthDp)
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
        contentBackground.changeDefault(drawable)
    }

    internal fun getCustomKey(key: CustomKey.Key) = customKeys.find { it.key == key }

    companion object {
        fun getInstance() = KeypadConfig()
    }
}
