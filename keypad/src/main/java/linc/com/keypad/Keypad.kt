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
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.ImageViewCompat
import com.google.android.material.ripple.RippleDrawableCompat
import com.google.android.material.ripple.RippleUtils
import com.google.android.material.shape.ShapeAppearanceModel
import java.util.*


class Keypad @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var keyClick: OnKeyClickListener? = null
    private var customClick: OnCustomKeyClickListener? = null

    private var keypadConfig = KeypadConfig.getInstance()
    private var customKeys = mutableListOf(
            CustomKey("*", CustomKey.Key.LEFT),
            CustomKey("#", CustomKey.Key.RIGHT)
    )

    private val keys = linkedMapOf<Array<Int>, View>()

    init {
        ScreenManager.init()
//        ConstraintConnector.setParentLayout(this)
//        orientation = VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        println("FINISH INFLATE")
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
        keys.clear()
        spawnKeys()
        ConstraintConnector.setParentLayout(this)

        repeat(KEYS_PER_SECTION) {
            linearConnect(it, ROW)
            linearConnect(it, COL)
        }

    }

    private fun linearConnect(currentDest: Int, dest: Int) {
        var prevView: View? = null
        keys.filter { it.key[dest] == currentDest }.map { it.value }.forEach { view ->
            when(prevView) {
                null -> when(dest) {
                    ROW -> ConstraintConnector.startToStartOf(view.id, ConstraintSet.PARENT_ID)
                    COL -> ConstraintConnector.topToTopOf(view.id, ConstraintSet.PARENT_ID)
                }
                else -> {
                    when(dest) {
                        ROW -> {
                            ConstraintConnector.startToEndOf(view.id, prevView!!.id)
                            ConstraintConnector.endToStartOf(prevView!!.id, view.id)
                        }
                        COL -> {
                            ConstraintConnector.topToBottomOf(view.id, prevView!!.id)
                            ConstraintConnector.bottomToTopOf(prevView!!.id, view.id)
                        }
                    }
                }
            }
            prevView = view
        }
        when(dest) {
            ROW -> ConstraintConnector.endToEndOf(prevView!!.id, ConstraintSet.PARENT_ID)
            COL -> ConstraintConnector.bottomToBottomOf(prevView!!.id, ConstraintSet.PARENT_ID)
        }
    }

    private fun spawnKeys() {
        var key = 1
        keys.clear()

        // Main 1..9 keys
        repeat(KEYS_PER_SECTION) { row->
            repeat(KEYS_PER_SECTION) { col ->
                addView(getTextKey(key.toString()).apply {
                    setOnClickListener { keyClick?.onKeyClick(contentInt()) }
                    keys[arrayOf(row, col)] = this
                })
                key++
            }
        }
        // Additional keys: 0 + left and right

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
        id = View.generateViewId()
        text = value
        gravity = Gravity.CENTER
        setTextSize(TypedValue.COMPLEX_UNIT_SP, keypadConfig.textSize)
        // Font
        setTypeface(keypadConfig.textFont)
        // Style
        setTypeface(typeface, keypadConfig.textStyle)
        setTextColor(getWrapperColor(keypadConfig.contentColor))
        layoutParams = LayoutParams(WRAP_CONTENT, keypadConfig.getSectionHeight())
        if(keypadConfig.keyRipple) {
            val typed = TypedValue()
            context.theme.resolveAttribute(
                    android.R.attr.selectableItemBackgroundBorderless,
//                    android.R.attr.actionBarItemBackground,
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
//            weight = KEY_HORIZONTAL_WEIGHT
        }
//        gravity = Gravity.CENTER
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

        // keypad limits
        private const val FIRST = 0
        private const val LAST = 2
        private const val INNER = 1

        // positions
        private const val ROW = 0
        private const val COL = 1


    }
}