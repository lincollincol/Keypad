package linc.com.keypad

import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

class KeypadConfig private constructor(
        internal var contentColor: ConfigColorWrapper = ConfigColorWrapper(Color.BLACK),
        internal var textSize: Float = 45f,
        internal var textStyle: Int = Typeface.NORMAL,
        internal var textFont: Typeface = Typeface.DEFAULT,
        internal var imageScaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER,
        internal var enableKeyRipple: Boolean = true,
        private var height: Int = WRAP_CONTENT,
        // TODO: 29.04.21 handle width
        private var width: Int = MATCH_PARENT,

        // TODO: 28.04.21 keyDrawableBackground
) {

    private var customKeys = mutableListOf(
            CustomKey.getInstance("*", CustomKey.Key.LEFT),
            CustomKey.getInstance("#", CustomKey.Key.RIGHT)
    )

    fun hideCustomKey(key: CustomKey.Key, hide: Boolean) {
        getCustomKey(key)?.hide = hide
    }

    fun setKeyCustomText(key: CustomKey.Key, value: String) {
        getCustomKey(key)?.let {
            it.value = value
            it.type = CustomKey.ContentType.TEXT
        }
    }

    fun setKeyCustomImage(key: CustomKey.Key, @DrawableRes resource: Int) {
        customKeys.find { it.key == key }?.let {
            it.value = resource
            it.type = CustomKey.ContentType.IMAGE
        }
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

    fun setKeyTextSize(sizeSp: Float) {
        textSize = sizeSp
    }

    fun setKeyTextStyle(typefaceStyle: Int) {
        textStyle = typefaceStyle
    }

    fun setKeyTextFont(font: Typeface) {
        textFont = font
    }

    fun setKeyImageScaleType(scale :ImageView.ScaleType) {
        imageScaleType = scale
    }

    fun enableKeyRipple(enable: Boolean) {
        enableKeyRipple = enable
    }

    fun setKeypadHeightPercent(percentage: Int) {
        height = ScreenManager.getHeightByPercent(when(percentage) {
            in 0..100 -> percentage
            else -> 100
        })
    }

    internal fun getKeypadHeight() = height

    internal fun getSectionHeight() = when (height) {
        WRAP_CONTENT -> height
        else -> height / 4
    }

    internal fun getCustomKey(key: CustomKey.Key) = customKeys.find { it.key == key }

    companion object {
        fun getInstance() = KeypadConfig()
    }
}
