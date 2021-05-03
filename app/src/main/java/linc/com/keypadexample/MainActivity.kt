package linc.com.keypadexample

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import linc.com.keypad.CustomKey
import linc.com.keypad.Keypad
import linc.com.keypad.KeypadConfig

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Keypad>(R.id.keypad).apply {

            applyKeypadConfig(KeypadConfig.getInstance().apply {
                setKeypadHeightPercent(50)
//                setKeyImageSize(100)
//                setKeyTextSize(24f)
                setKeyTextStyle(Typeface.BOLD)
//                setKeypadColorRes(android.R.color.holo_purple)
                setKeyContentColorRes(android.R.color.white)
                setCustomKey(CustomKey.getInstance("+", CustomKey.Key.LEFT).apply {
//                    setKeyContentColorInt(Color.GREEN)
                    setKeyTextStyle(Typeface.ITALIC)

                })
                setCustomKey(CustomKey.getInstance(R.drawable.ic_coronavirus, CustomKey.Key.RIGHT).apply {

                    setKeyContentColorInt(Color.CYAN)
                })
//                hideCustomKey(CustomKey.Key.LEFT, false)
            })

            addKeypadClickListener(Keypad.OnKeyClickListener {
                println("KEY = $it")
            })

            addKeypadClickListener(Keypad.OnCustomKeyClickListener {

                println("KEY = ${it.key} ${it.value}, ${it.type}")
            })

            /*setOnKeypadClickListener(object : Keypad.OnKeypadClickListener() {
                override fun onKeyClicked(value: Int) {
                    println(value)
                }

                override fun onLeftKeyClicked() {
                    super.onLeftKeyClicked()
                    println("LEFT")
                }

                override fun onRightKeyClicked() {
                    super.onRightKeyClicked()
                    println("RIGHT")
                }
            })*/
        }
    }
}