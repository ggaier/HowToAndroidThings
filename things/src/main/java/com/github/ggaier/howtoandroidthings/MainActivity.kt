package com.github.ggaier.howtoandroidthings

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var mLedGpio: Gpio
    private val mHandler = Handler()
    private val mBlinkRunnable = Runnable {
        blink()
    }

    private fun MainActivity.blink() {
        mLedGpio.value = !mLedGpio.value
        mHandler.postDelayed(mBlinkRunnable, 1000L)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val manager = PeripheralManager.getInstance()
        manager.gpioList.forEach {
            Log.d(TAG, "Gpio pin name: $it")
        }
        mLedGpio = manager.openGpio("BCM6")
        mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        mHandler.postDelayed(mBlinkRunnable, 1000L)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(mBlinkRunnable)
        mLedGpio.close()
    }


}
