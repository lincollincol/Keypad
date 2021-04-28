package linc.com.keypad

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IntegerRes
import androidx.core.content.ContextCompat

class Keypad @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var clickListener: OnKeypadClickListener? = null
    private var leftKey: CustomKey<*> = CustomKey("*")
    private var rightKey: CustomKey<*> = CustomKey("#")

    private var keypadStyle = KeypadStyle()

    init {
        ScreenManager.init()

        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        orientation = VERTICAL
//        setBackgroundResource(R.color.design_default_color_primary)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initKeypad()
    }

    fun setOnKeypadClickListener(clickListener: OnKeypadClickListener) {
        this.clickListener = clickListener
    }

    fun hideCustomKey(key: Key, hide: Boolean) {
        when(key) {
            Key.LEFT -> leftKey.hide = hide
            Key.RIGHT -> rightKey.hide = hide
        }
    }

    fun setKeyCustomText(key: Key, value: String) {
        when(key) {
            Key.LEFT -> leftKey = CustomKey(value)
            Key.RIGHT -> rightKey = CustomKey(value)
        }
    }

    fun setKeyCustomImage(key: Key, @IntegerRes resource: Int) {
        when(key) {
            Key.LEFT -> leftKey = CustomKey(resource, CustomKey.IMAGE)
            Key.RIGHT -> rightKey = CustomKey(resource, CustomKey.IMAGE)
        }
    }

    fun setKeyContentColorInt(@ColorInt colorInt: Int) {
        keypadStyle.contentColor = colorInt
    }

    fun setKeyContentColorRes(@ColorRes colorRes: Int) {
        keypadStyle.contentColor = ContextCompat.getColor(context, colorRes)
    }

    fun setKeyTextSize(sizeSp: Float) {
        keypadStyle.textSize = sizeSp
    }

    fun setKeyTextStyle(typefaceStyle: Int) {
        keypadStyle.textStyle = typefaceStyle
    }

    fun setKeyTextFont(font: Typeface) {
        keypadStyle.textFont = font
    }

    fun enableKeyRipple(enable: Boolean) {
        keypadStyle.keyRipple = enable
    }

    fun setKeypadColorInt(@ColorInt colorInt: Int) {
        keypadStyle.keypadColor = colorInt
    }

    fun setKeypadColorRes(@ColorRes colorRes: Int) {
        keypadStyle.keypadColor = ContextCompat.getColor(context, colorRes)
    }

    fun setKeypadHeightPercent(percentage: Int) {
        keypadStyle.height = ScreenManager.getHeightByPercent(percentage)
        removeAllViews()
        initKeypad()
    }

    private fun initKeypad() {
        var key = 1
        var section: LinearLayout

        repeat(KEYS_PER_SECTION) { row ->
            section = getSection()
            repeat(KEYS_PER_SECTION) { col ->
                section.addView(getTextKey(key.toString()).apply {
                    setOnClickListener { clickListener?.onKeyClicked(contentInt()) }
                })
                key++
            }
            addView(section)
        }

        // Fill last section
        section = getSection()

        fun addCustomKey(customKey: CustomKey<*>) {
            if(customKey.hide.not()) {
                section.addView(when (customKey.type) {
                    CustomKey.IMAGE -> getImageKey()
                    else -> getTextKey(customKey.value.toString())
                })
            }
        }

        addCustomKey(leftKey)
        section.addView(getTextKey(ZERO_KEY).apply {
            setOnClickListener { clickListener?.onKeyClicked(contentInt()) }
        })
        addCustomKey(rightKey)
        addView(section)
    }

    /**
     * View providers
     */

    private fun getSection() = LinearLayout(context).apply {
        this.orientation = orientation
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(MATCH_PARENT, keypadStyle.getSectionHeight())
        setBackgroundColor(keypadStyle.keypadColor)
    }

    private fun getTextKey(value: String) = TextView(context).apply {
        text = value
        gravity = Gravity.CENTER
        setTextSize(TypedValue.COMPLEX_UNIT_SP, keypadStyle.textSize)
        // Font
        setTypeface(keypadStyle.textFont)
        // Style
        setTypeface(typeface, keypadStyle.textStyle)
        setTextColor(keypadStyle.contentColor)
        layoutParams = LayoutParams(0, MATCH_PARENT).apply {
            weight = KEY_HORIZONTAL_WEIGHT
        }
        if(keypadStyle.keyRipple) {
            val typed = TypedValue()
            context.theme.resolveAttribute(
                    android.R.attr.selectableItemBackgroundBorderless,
                    typed,
                    true
            )
            setBackgroundResource(typed.resourceId)
        }
    }

    private fun getImageKey() = ImageView(context).apply {
        setImageResource(rightKey.value as Int)
        layoutParams = LayoutParams(0, MATCH_PARENT).apply {
            weight = KEY_HORIZONTAL_WEIGHT
        }
        gravity = Gravity.CENTER
        if(keypadStyle.keyRipple) {
            val typed = TypedValue()
            context.theme.resolveAttribute(
                    android.R.attr.selectableItemBackgroundBorderless,
                    typed,
                    true
            )
            setBackgroundResource(typed.resourceId)
        }
    }

    abstract class OnKeypadClickListener {
        abstract fun onKeyClicked(value: Int)

        open fun onLeftKeyClicked() {
            /** Not implemented*/
        }

        open fun onRightKeyClicked() {
            /** Not implemented*/
        }
    }

    companion object {
        private const val ZERO_KEY = "0"
        private const val KEYS_PER_SECTION = 3
        private const val KEY_HORIZONTAL_WEIGHT = 33f
    }
}