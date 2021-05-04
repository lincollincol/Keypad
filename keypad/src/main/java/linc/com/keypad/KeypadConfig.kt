package linc.com.keypad

import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Size

class KeypadConfig private constructor(
        internal var textSize: Float = 45f,
        internal var textStyle: Int = Typeface.NORMAL,
        internal var textFont: Typeface = Typeface.DEFAULT,
        internal var enableKeyRipple: Boolean = true,
        internal var contentPadding: Int = 0,
        internal var contentColor: ConfigColorWrapper = ConfigColorWrapper(Color.BLACK),
        internal var height: SizeWrapper = SizeWrapper(0, SizeWrapper.State.DEFAULT),
        internal var width: SizeWrapper = SizeWrapper(0, SizeWrapper.State.DEFAULT),

        // TODO: 28.04.21 keyDrawableBackground
) {

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
            state = SizeWrapper.State.CHANGED
        }
    }

    fun setKeypadWidthPercent(percent: Int) {
        width.apply {
            value = ScreenManager.getWidthByPercent(percent)
            state = SizeWrapper.State.CHANGED
        }
    }

    fun setContentPadding(padding: Int) {
        contentPadding = padding
        customKeys.forEach { it.contentPadding.setGlobal(padding) }
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

    internal fun getCustomKey(key: CustomKey.Key) = customKeys.find { it.key == key }

    companion object {
        fun getInstance() = KeypadConfig()
    }
}
