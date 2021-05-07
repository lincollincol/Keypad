package linc.com.keypad

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import linc.com.keypad.Constants.ADDITIONAL_ROW
import linc.com.keypad.Constants.COL
import linc.com.keypad.Constants.EMPTY_KEY
import linc.com.keypad.Constants.KEYS_PER_SECTION
import linc.com.keypad.Constants.ROW
import linc.com.keypad.Constants.SIDE_COUNT
import linc.com.keypad.Constants.ZERO
import linc.com.keypad.Constants.ZERO_KEY
import java.util.function.Consumer


class KeypadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    // TODO: 04.05.21 xml attrs support
    // TODO: 06.05.21 add letters for key

    private var keyClick: OnKeyClickListener? = null
    private var customClick: OnCustomKeyClickListener? = null
    private var keypadConfig = KeypadConfig.getInstance()
    private val keys = linkedMapOf<Array<Int>, View>()

    init {
        ScreenManager.init()

//        val attributes = context.obtainStyledAttributes(attrs, R.styleable. KeypadView)


        /*

        internal var keyHeight: Int = 0,
        internal var keyWidth: Int = 0,

        internal var keyPadding: IntArray = IntArray(SIDE_COUNT) { 0 },
        internal var keyMargin: IntArray = IntArray(SIDE_COUNT) { 0 },

        internal var contentColor: ConfigColorWrapper = ConfigColorWrapper(Color.BLACK),
        internal var contentBackground: ChangeableWrapper<Int> = ChangeableWrapper(0, ChangeableWrapper.State.DEFAULT),
        internal var keypadHeight: ChangeableWrapper<Int> = ChangeableWrapper(0, ChangeableWrapper.State.DEFAULT),
        internal var keypadWidth: ChangeableWrapper<Int> = ChangeableWrapper(0, ChangeableWrapper.State.DEFAULT),
         */

        /*attributes.getFloat(R.styleable.KeypadView_keyTextSize, 35f)
        attributes.getInteger(R.styleable.KeypadView_keyTextStyle, Typeface.NORMAL)
        attributes.getBoolean(R.styleable.KeypadView_keyEnableRipple, true)
        attributes.getInteger(R.styleable.KeypadView_keyHeight, 0)
        attributes.getInteger(R.styleable.KeypadView_keyWidth, 0)

        val keyPadding: IntArray = IntArray(SIDE_COUNT) { 0 }
        attributes.getInteger(R.styleable.KeypadView_keyPadding, 0)
        attributes.getInteger(R.styleable.KeypadView_keyPaddingTop, 0)
        attributes.getInteger(R.styleable.KeypadView_keyPaddingBottom, 0)
        attributes.getInteger(R.styleable.KeypadView_keyPaddingStart, 0)
        attributes.getInteger(R.styleable.KeypadView_keyPaddingEnd, 0)

        val keyMargin = IntArray(SIDE_COUNT)
        attributes.getInteger(R.styleable.KeypadView_keyMargin, 0)
        attributes.getInteger(R.styleable.KeypadView_keyMarginTop, 0)
        attributes.getInteger(R.styleable.KeypadView_keyMarginBottom, 0)
        attributes.getInteger(R.styleable.KeypadView_keyMarginStart, 0)
        attributes.getInteger(R.styleable.KeypadView_keyMarginEnd, 0)

        attributes.getColor(R.styleable.KeypadView_contentColor, Color.BLACK)
        attributes.getColor(R.styleable.KeypadView_contentBackground, 0)
        attributes.getInteger(R.styleable.KeypadView_keypadHeight, 0)
        attributes.getInteger(R.styleable.KeypadView_keypadWidth, 0)

        attributes.recycle()*/

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

    fun applyKeypadConfig(config: KeypadConfig.() -> Unit) {
        config(keypadConfig)
        removeAllViews()
        initKeypad()
    }

    fun applyKeypadConfig(config: ApplyKeypadConfig) {
        config.apply(keypadConfig)
        removeAllViews()
        initKeypad()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutParams.apply {
            if(keypadConfig.keypadWidth.state == ChangeableWrapper.State.CHANGED)
                width = keypadConfig.keypadWidth.value
            if(keypadConfig.keypadHeight.state == ChangeableWrapper.State.CHANGED)
                height = keypadConfig.keypadHeight.value
        }
    }

    private fun initKeypad() {
        keys.clear()
        spawnViews()
        repeat(KEYS_PER_SECTION) {
            linearConnect(it, ROW)
            linearConnect(it, COL)
        }
        linearConnect(ADDITIONAL_ROW, ROW)
        keys.clear()
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
                    CustomKey.ContentType.IMAGE -> getImageKey(customKey.value as Int, key)
                    else -> getTextKey(customKey.value.toString(), key)
                }.apply {
                    setOnClickListener { customClick?.onCustomKeyClick(customKey.getKeyContent()) }
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

        // Update connector
        ConstraintConnector.setParentLayout(this)
    }

    /**
     * View providers
     */

    private fun getTextKey(value: String, key: CustomKey.Key? = null) = applyConfig(TextView(context), key) {
        it.text = value
        it.gravity = Gravity.CENTER
        it.setTextSize(TypedValue.COMPLEX_UNIT_SP, when (key) {
            null -> keypadConfig.keyTextSize
            else -> keypadConfig.getCustomKey(key)!!.keyTextSize.getValue()
        })
        it.setTypeface(when (key) {
            null -> keypadConfig.keyTextFont
            else -> keypadConfig.getCustomKey(key)!!.keyTextFont.getValue()
        })
        it.setTypeface(it.typeface, when (key) {
            null -> keypadConfig.keyTextStyle
            else -> keypadConfig.getCustomKey(key)!!.keyTextStyle.getValue()
        })
        it.setTextColor(getWrapperColor(when (key) {
            null -> keypadConfig.contentColor
            else -> keypadConfig.getCustomKey(key)!!.contentColor.getValue()
        }))
    }

    private fun getImageKey(@DrawableRes resource: Int, key: CustomKey.Key? = null) = applyConfig(ImageView(context), key) {
        it.setImageResource(resource)
        ImageViewCompat.setImageTintList(it, ColorStateList.valueOf(
                getWrapperColor(when (key) {
                    null -> keypadConfig.contentColor
                    else -> keypadConfig.getCustomKey(key)!!.contentColor.getValue()
                })
        ))
    }

    private fun <T : View> applyConfig(view: T, key: CustomKey.Key? = null, block: (view: T) -> Unit): T {
        view.apply {
            id = View.generateViewId()
            val size = when(key) {
                null -> keypadConfig.keyWidth.toDp(resources) to
                        keypadConfig.keyHeight.toDp(resources)
                else -> keypadConfig.getCustomKey(key)!!.keyWidth.getValue() to
                        keypadConfig.getCustomKey(key)!!.keyHeight.getValue()
            }

            layoutParams = LayoutParams(size.first, size.second).apply {
                setMargins(when(key) {
                    null -> keypadConfig.keyMargin
                    else -> keypadConfig.getCustomKey(key)!!.keyMargin.getValue()
                })
            }

            setPadding(when(key) {
                null -> keypadConfig.keyPadding
                else -> keypadConfig.getCustomKey(key)!!.keyPadding.getValue()
            })

            val enableRipple = when(key) {
                null -> keypadConfig.keyEnableRipple
                else -> keypadConfig.getCustomKey(key)!!.keyEnableRipple.getValue()
            }

            // Set background or ripple
            when(keypadConfig.contentBackground.state) {
                ChangeableWrapper.State.CHANGED -> setBackgroundResource(keypadConfig.contentBackground.value)
                else -> if(enableRipple) enableBorderlessRipple()
            }
        }
        block(view)
        return view
    }

    private fun getWrapperColor(wrapper: ConfigColorWrapper) = when(wrapper.colorSource) {
        ConfigColorWrapper.ColorSource.INT -> wrapper.color
        else -> ContextCompat.getColor(context, wrapper.color)
    }

    fun interface OnKeyClickListener {
        fun onKeyClick(value: Int)
    }

    fun interface OnCustomKeyClickListener {
        fun onCustomKeyClick(key: CustomKey.Content)
    }

    fun interface ApplyKeypadConfig {
        fun apply(keypadConfig: KeypadConfig)
    }

}