package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.background
import com.example.myapplication.ui.theme.bottomShadowColor
import com.mustachenko.neumorphic.model.Radius
import com.mustachenko.neumorphic.model.ShadowColor
import com.mustachenko.neumorphic.model.TapType
import com.example.myapplication.ui.theme.topShadowColor
import com.mustachenko.neumorphic.pressable
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val image = ImageVector.vectorResource(id = R.drawable.ic_launcher_background)
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp, 16.dp)
                .pressable(
                    backgroundColor = background,
                    shadowColor = ShadowColor(topShadowColor, bottomShadowColor),
                    radius = Radius(
                        16.dp, 16.dp, 16.dp, 16.dp
                    ),
                    elevation = 8.dp,
                    border = 4.dp,
                    innerShadowSize = 32.dp,
                    tapType = TapType.CLICK,
                    requestInnerShadow = true
                )
        ) {
            Image(
                image,
                "logo",
                Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
                .pressable(
                    backgroundColor = background,
                    shadowColor = ShadowColor(topShadowColor, bottomShadowColor),
                    radius = Radius(
                        8.dp, 8.dp, 8.dp, 8.dp
                    ),
                    elevation = 8.dp,
                    border = 4.dp,
                    innerShadowSize = 8.dp,
                    tapType = TapType.CLICK
                )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "Hello $name!"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}