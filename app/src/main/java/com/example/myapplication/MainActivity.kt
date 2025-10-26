package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

import org.json.JSONObject


class MainActivity : ComponentActivity(), Orientation.Listener {

    private lateinit var mOrientation: Orientation

    val jsonData = JSONObject("""{
        "steering": 0,
        "throttle": 0,
        "brake": 0,
        "gearUp": 0,
        "gearDown": 0
    }""")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    buton(gearChange = ::jsonGearChange)
                    SliderExample(throttleChange = ::jsonChangeThrottle)
                }
            }
        }

        mOrientation = Orientation(this)

    }

    override fun onStart() {
        super.onStart()
        mOrientation.startListening(this)
    }

    override fun onStop() {
        super.onStop()
        mOrientation.stopListening()
    }


    override fun onOrientationChanged(pitch: Float, roll: Float) {
        //Log.d("", "pitch: $pitch  |  roll: $roll")
        val rollA = (roll * 100).toInt()
        //var roll2Decimal = rollA / 100.0
        //Log.d("PhoneOutput", "<leftStick>$rollA")
        jsonData.put("steering", rollA)
        sendJson()
    }


    var lastUpdateTime = System.nanoTime()
    val targetHz = 500
    val timePerHz = 1_000_000_000L / targetHz
    fun sendJson() {
        val now = System.nanoTime()

        if (now - lastUpdateTime >= timePerHz)
        {
            lastUpdateTime = now
            //Log.d("dsakldjsakdljas","dksajldlaksjdklsaj")
            Log.d("PhoneOutput", "$jsonData");
            //Log.d("jjjdasdasdasasdasadsdasads", "${jsonData.toString()}");
        }

    }


    fun jsonChangeThrottle(throttlebrake: String, throttlePos: Float)
    {
        //Log.d("fdsjklfsadkjl","$throttlePos")
        //jsonData.put("throttle", throttlePos)
        jsonData.put(throttlebrake, throttlePos)
        sendJson()
    }

    fun jsonGearChange(updown: String, state: Int)
    {
        jsonData.put(updown, state)
        sendJson()
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}




@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun buton(modifier: Modifier = Modifier, gearChange: (updown: String, state: Int) -> Unit)
{
    Button(

        onClick = { Log.d("PhoneInput", "10") },
        modifier = Modifier
            .offset(100.dp, 250.dp)
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        gearChange("gearUp", 1)

                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        gearChange("gearUp", 0)
                    }
                }
                true
            }

    ) {
        Text("Gear Up")
    }


    Button(

        onClick = { Log.d("PhoneInput", "10") },
        modifier = Modifier
            .offset(100.dp, 350.dp)
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        gearChange("gearDown", 1)

                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        gearChange("gearDown", 0)
                    }
                }
                true
            }

    ) {
        Text("Gear Down")
    }

}




@Composable
fun SliderExample(throttleChange: (throttlebrake: String, throttlePos: Float) -> Unit) {
    var sliderThrottle by remember {mutableStateOf(0f)}
    var sliderBrake by remember {mutableStateOf(0f)}

    Slider(
        value = sliderThrottle,
        valueRange = 0f..255f,
        onValueChange = {
            sliderThrottle = it
            throttleChange("throttle", sliderThrottle)
        },
        onValueChangeFinished = {
            sliderThrottle = 0f
            throttleChange("throttle", sliderThrottle)
        },
        modifier = Modifier
            .offset(0.dp,550.dp)
            .padding(70.dp)
    )

    Slider(
        value = sliderBrake,
        valueRange = 0f..255f,
        onValueChange = {
            sliderBrake = it
            throttleChange("brake", sliderBrake)
        },
        onValueChangeFinished = {
            sliderBrake = 0f
            throttleChange("brake", sliderBrake)
        },
        modifier = Modifier
            .offset(0.dp,100.dp)
            .padding(70.dp)
    )

}

