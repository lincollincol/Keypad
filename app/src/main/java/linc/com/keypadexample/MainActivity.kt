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

//            setKeyCustomText(CustomKey.Key.RIGHT, "#")
//            setKeyCustomImage(CustomKey.Key.LEFT, R.drawable.ic_coronavirus)
//            hideCustomKey(CustomKey.Key.LEFT, true)

//            applyKeypadConfig(KeypadConfig.getInstance().apply {
//                setKeypadHeightPercent(70)
//                setKeyImageSize(100)
//                setKeyTextSize(24f)
//                setKeyTextStyle(Typeface.BOLD)
//                setKeypadColorRes(android.R.color.holo_purple)
//                setKeyContentColorRes(android.R.color.holo_green_light)
//            })

//            addKeypadClickListener(Keypad.OnKeyClickListener {
//                println("KEY = $it")
//            })

//            addKeypadClickListener(Keypad.OnCustomKeyClickListener {
//                println("KEY = ${it.key} ${it.value}, ${it.type}")
//            })

            /*addKeypadClickListener(object : Keypad.OnKeyClickListener {
                override fun onKeyClick(value: Int) {
                }
            })*/


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