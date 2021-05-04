package linc.com.keypad

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.core.widget.ImageViewCompat


class KeypadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    // TODO: 04.05.21 xml attrs support
    // TODO: 04.05.21 key size => wrap, 0dp

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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutParams.apply {
            if(keypadConfig.width.state == ChangeableWrapper.State.CHANGED)
                width = keypadConfig.width.value
            if(keypadConfig.height.state == ChangeableWrapper.State.CHANGED)
                height = keypadConfig.height.value
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

    private fun getTextKey(value: String, key: CustomKey.Key? = null) = TextView(context).apply {
        id = View.generateViewId()
        text = value
        gravity = Gravity.CENTER
        setTextSize(
            TypedValue.COMPLEX_UNIT_SP, when (key) {
                null -> keypadConfig.textSize
                else -> keypadConfig.getCustomKey(key)!!.textSize.getValue()
            }
        )
        setTypeface(
            when (key) {
                null -> keypadConfig.textFont
                else -> keypadConfig.getCustomKey(key)!!.textFont.getValue()
            }
        )
        setTypeface(
            typeface, when (key) {
                null -> keypadConfig.textStyle
                else -> keypadConfig.getCustomKey(key)!!.textStyle.getValue()
            }
        )
        setTextColor(
            getWrapperColor(
                when (key) {
                    null -> keypadConfig.contentColor
                    else -> keypadConfig.getCustomKey(key)!!.contentColor.getValue()
                }
            )
        )

        layoutParams = LayoutParams(0, 0).apply {
            setMargins(when(key) {
                null -> keypadConfig.contentMargin
                else -> keypadConfig.getCustomKey(key)!!.contentMargin.getValue()
            })
        }

        setPadding(when(key) {
            null -> keypadConfig.contentPadding
            else -> keypadConfig.getCustomKey(key)!!.contentPadding.getValue()
        })

        val enableRipple = when(key) {
            null -> keypadConfig.enableKeyRipple
            else -> keypadConfig.getCustomKey(key)!!.enableKeyRipple.getValue()
        }

        // Set background or ripple
        when(keypadConfig.contentBackground.state) {
            ChangeableWrapper.State.CHANGED -> setBackgroundResource(keypadConfig.contentBackground.value)
            else -> if(enableRipple) enableBorderlessRipple()
        }

    }

    private fun getImageKey(@DrawableRes resource: Int, key: CustomKey.Key? = null) = ImageView(
        context
    ).apply {
        id = View.generateViewId()
        setImageResource(resource)

        ImageViewCompat.setImageTintList(
            this, ColorStateList.valueOf(
                getWrapperColor(
                    when (key) {
                        null -> keypadConfig.contentColor
                        else -> keypadConfig.getCustomKey(key)!!.contentColor.getValue()
                    }
                )
            )
        )

        // TODO: 04.05.21 size instead of 0, 0
        layoutParams = LayoutParams(0, 0).apply {
            val mar = when(key) {
                null -> {
                    println("PARENT")
                    keypadConfig.contentMargin
                }
                else -> {
                    println("CHILD")
                    keypadConfig.getCustomKey(key)!!.contentMargin.getValue()
                }
            }
            println(mar.contentToString())
            setMargins(mar)
        }

        setPadding(when(key) {
            null -> keypadConfig.contentPadding
            else -> keypadConfig.getCustomKey(key)!!.contentPadding.getValue()
        })

        val enableRipple = when(key) {
            null -> keypadConfig.enableKeyRipple
            else -> keypadConfig.getCustomKey(key)!!.enableKeyRipple.getValue()
        }

        // Set background or ripple
        when(keypadConfig.contentBackground.state) {
            ChangeableWrapper.State.CHANGED -> setBackgroundResource(keypadConfig.contentBackground.value)
            else -> if(enableRipple) enableBorderlessRipple()
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
        fun onCustomKeyClick(key: CustomKey.Content)
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