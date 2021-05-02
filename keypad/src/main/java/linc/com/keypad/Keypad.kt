package linc.com.keypad

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat


class Keypad @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var keyClick: OnKeyClickListener? = null
    private var customClick: OnCustomKeyClickListener? = null

    private var keypadConfig = KeypadConfig.getInstance()


    private val keys = linkedMapOf<Array<Int>, View>()

    init {
        ScreenManager.init()
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

    fun applyKeypadConfig(config: KeypadConfig) {
        keypadConfig = config
        removeAllViews()
        initKeypad()
    }

    private fun initKeypad() {
        keys.clear()
        spawnViews()

        repeat(KEYS_PER_SECTION) {
            linearConnect(it, ROW)
            linearConnect(it, COL)
        }
        linearConnect(ADDITIONAL_ROW, ROW)
    }

    private fun linearConnect(currentDest: Int, dest: Int) {
        var prevView: View? = null
        keys.filter { it.key[dest] == currentDest }.map { it.value }.forEach { view ->
            when(prevView) {
                null -> when (dest) {
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

    private fun spawnViews() {
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
        fun addCustomKey(key: CustomKey.Key) {
            val customKey = keypadConfig.getCustomKey(key)!!
            if(customKey.hide.not()) {
                addView(when (customKey.type) {
                    CustomKey.ContentType.IMAGE -> getImageKey(customKey.value as Int)
                    else -> getTextKey(customKey.value.toString())
                }.apply {
                    setOnClickListener { customClick?.onCustomKeyClick(customKey) }
                    keys[arrayOf(ADDITIONAL_ROW, key.position)] = this
                })
            } else {
                addView(getTextKey(EMPTY_KEY).apply {
                    keys[arrayOf(ADDITIONAL_ROW, key.position)] = this
                })
            }
        }

        addCustomKey(CustomKey.Key.LEFT)
        addView(getTextKey(ZERO_KEY).apply {
            setOnClickListener { keyClick?.onKeyClick(contentInt()) }
            keys[arrayOf(ADDITIONAL_ROW, ZERO)] = this
        })
        addCustomKey(CustomKey.Key.RIGHT)

        // Refresh connector
        ConstraintConnector.setParentLayout(this)
    }

    /**
     * View providers
     */

    private fun getSection() = LinearLayout(context).apply {
        this.orientation = orientation
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(MATCH_PARENT, keypadConfig.getSectionHeight())
//        setBackgroundColor(getWrapperColor(keypadConfig.keypadColor))
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
        layoutParams = LayoutParams(0, keypadConfig.getSectionHeight())
        if(keypadConfig.enableKeyRipple) {
            val typed = TypedValue()
            context.theme.resolveAttribute(
                    android.R.attr.selectableItemBackgroundBorderless,
                    typed,
                    true
            )
            setBackgroundResource(typed.resourceId)
        }

    }

    private fun getImageKey(@DrawableRes resource: Int) = ImageView(context).apply {
        id = View.generateViewId()
        setImageResource(resource)

        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(
                getWrapperColor(keypadConfig.contentColor)
        ))
        layoutParams = LayoutParams(0, keypadConfig.getSectionHeight())
        if(keypadConfig.enableKeyRipple) {
            val typed = TypedValue()
            context.theme.resolveAttribute(
                    R.attr.selectableItemBackgroundBorderless,
//                    R.attr.selectableItemBackground,
                    typed,
                    true
            )
            setBackgroundResource(typed.resourceId)
        }
    }

    private fun getWrapperColor(wrapper: ConfigColorWrapper) = when(wrapper.colorSource) {
        ConfigColorWrapper.ColorSource.INT -> wrapper.color
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

        private const val ROW = 0
        private const val COL = 1

        private const val ADDITIONAL_ROW = 3
        private const val ZERO = 1

    }
}