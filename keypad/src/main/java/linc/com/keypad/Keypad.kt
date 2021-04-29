package linc.com.keypad

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.text.Layout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.google.android.material.ripple.RippleDrawableCompat
import com.google.android.material.ripple.RippleUtils
import com.google.android.material.shape.ShapeAppearanceModel


class Keypad @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private var keyClick: OnKeyClickListener? = null
    private var customClick: OnCustomKeyClickListener? = null

    private var keypadConfig = KeypadConfig.getInstance()
    private var customKeys = mutableListOf(
            CustomKey("*", CustomKey.Key.LEFT),
            CustomKey("#", CustomKey.Key.RIGHT)
    )

    init {
        ScreenManager.init()
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        orientation = VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initKeypad()
    }

    fun addKeypadClickListener(clickListener: OnCustomKeyClickListener) {
        customClick = clickListener
    }

    fun addKeypadClickListener(clickListener: OnKeyClickListener) {
        keyClick = clickListener
    }

    fun hideCustomKey(key: CustomKey.Key, hide: Boolean) {
        customKeys.find { it.key == key }?.hide = hide
    }

    fun setKeyCustomText(key: CustomKey.Key, value: String) {
        customKeys.find { it.key == key }?.let {
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

    fun applyKeypadConfig(config: KeypadConfig) {
        keypadConfig = config
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
                    setOnClickListener { keyClick?.onKeyClick(contentInt()) }
                })
                key++
            }
            addView(section)
        }

        // Fill last section
        section = getSection()

        fun addCustomKey(key: CustomKey.Key) {
            val customKey = customKeys.find { it.key == key }!!
            if(customKey.hide.not()) {
                section.addView(when (customKey.type) {
                    CustomKey.ContentType.IMAGE -> getImageKey(customKey.value as Int)
                    else -> getTextKey(customKey.value.toString())
                }.apply {
                    setOnClickListener { customClick?.onCustomKeyClick(customKey) }
                })
            } else {
                section.addView(getTextKey(EMPTY_KEY))
            }
        }

        addCustomKey(CustomKey.Key.LEFT)
        section.addView(getTextKey(ZERO_KEY).apply {
            setOnClickListener { keyClick?.onKeyClick(contentInt()) }
        })
        addCustomKey(CustomKey.Key.RIGHT)
        addView(section)
    }

    /**
     * View providers
     */

    private fun getSection() = LinearLayout(context).apply {
        this.orientation = orientation
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(MATCH_PARENT, keypadConfig.getSectionHeight())
        setBackgroundColor(getWrapperColor(keypadConfig.keypadColor))
    }

    private fun getTextKey(value: String) = TextView(context).apply {
        text = value
        gravity = Gravity.CENTER
        setTextSize(TypedValue.COMPLEX_UNIT_SP, keypadConfig.textSize)
        // Font
        setTypeface(keypadConfig.textFont)
        // Style
        setTypeface(typeface, keypadConfig.textStyle)
        setTextColor(getWrapperColor(keypadConfig.contentColor))
        layoutParams = LayoutParams(0, MATCH_PARENT).apply {
//        layoutParams = LayoutParams(0, WRAP_CONTENT).apply {
            weight = KEY_HORIZONTAL_WEIGHT
        }
        if(keypadConfig.keyRipple) {
            val typed = TypedValue()
            context.theme.resolveAttribute(
//                    android.R.attr.selectableItemBackground,
                    android.R.attr.actionBarItemBackground,
                    typed,
                    true
            )
            setBackgroundResource(typed.resourceId)
        }

    }

    private fun getImageKey(@DrawableRes resource: Int) = ImageView(context).apply {
        setImageResource(resource)
        val back = ContextCompat.getDrawable(context, resource).apply {
//            scaleX = 2.5f
//            scaleY = 2.5f
        }
//        background = getBackgroundDrawable(Color.RED, background)

        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(
                getWrapperColor(keypadConfig.contentColor)
        ))
        layoutParams = LayoutParams(0, WRAP_CONTENT).apply {
//        layoutParams = LayoutParams(keypadConfig.imageSize, keypadConfig.imageSize).apply {
            weight = KEY_HORIZONTAL_WEIGHT
        }
        gravity = Gravity.CENTER
        if(keypadConfig.keyRipple) {
            val typed = TypedValue()
            context.theme.resolveAttribute(
//                    android.R.attr.selectableItemBackgroundBorderless,
                    R.drawable.ripple,
                    typed,
                    true
            )
            setBackgroundResource(typed.resourceId)
        }
    }

    fun getBackgroundDrawable(pressedColor: Int, backgroundDrawable: Drawable?): RippleDrawable {
        return RippleDrawable(getPressedState(pressedColor)!!, backgroundDrawable, null).apply {
//            setBounds(0, 0, 2, 2)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                radius = 5
            }
        }
    }

    fun getPressedState(pressedColor: Int): ColorStateList? {
        return ColorStateList(arrayOf(intArrayOf()), intArrayOf(pressedColor))
    }

    private fun getWrapperColor(wrapper: KeypadConfig.ConfigColorWrapper) = when(wrapper.colorSource) {
        KeypadConfig.ConfigColorWrapper.ColorSource.INT -> wrapper.color
        else -> ContextCompat.getColor(context, wrapper.color)
    }

    fun interface OnKeyClickListener {
        fun onKeyClick(value: Int)
    }

    fun interface OnCustomKeyClickListener {
        fun onCustomKeyClick(key: CustomKey)
    }

    companion object {
        private const val ZERO_KEY = "0"
        private const val EMPTY_KEY = ""
        private const val KEYS_PER_SECTION = 3
        private const val KEY_HORIZONTAL_WEIGHT = 33f
    }
}