package com.sq.thed_ck_licker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.player.HealthBar
import com.sq.thed_ck_licker.ui.theme.TheD_ck_LickerTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheD_ck_LickerTheme {
                val clicks = rememberSaveable() { mutableIntStateOf(0) }
                val workers = rememberSaveable() { mutableIntStateOf(0) }
                val modifier = Modifier

                Scaffold(
                    modifier = modifier
                        .padding(8.dp)
                        .fillMaxSize()
                ) { innerPadding ->
                    Column {
                        HealthBar(
                            (clicks.intValue.toFloat() % 100) / 100f,
                            modifier = modifier.padding(innerPadding)
                        )
                        TheClickButtonAndText(modifier, clicks)
                        HorizontalDivider()
                        TheShop(modifier, clicks, workers)
                        TheWorkers(modifier, clicks, workers)

                    }
                }
            }
        }
    }
}


@Composable
fun TheShop(modifier: Modifier, clicks: MutableIntState, workers: MutableIntState) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("Shop")
        Button(onClick =  {
            if (clicks.intValue >= 10) {
                clicks.intValue -= 10
                workers.intValue++
            }
        }){
            Text("Buy Worker")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TheClickButtonAndText(
    modifier: Modifier = Modifier,
    clicks: MutableState<Int> = mutableIntStateOf(0),
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("Clicks: ${clicks.value}")
        Button(onClick = {
            clicks.value++
            println("clicks ${clicks.value}")
        }) {
            Text("Nyt!")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TheWorkers(
    modifier: Modifier = Modifier,
    clicks: MutableState<Int> = mutableIntStateOf(0),
    workers: MutableState<Int> = mutableIntStateOf(0),
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("workers: ${workers.value}")
    }
    LaunchedEffect(true) {
        while (true) {
//            time = Time.getLatest()
            delay(1000)
            println("aaaaaa")
            clicks.value += workers.value
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    var clicks = rememberSaveable() { mutableIntStateOf(0) }
    val modifier = Modifier
    TheD_ck_LickerTheme {
        Column(modifier = modifier.fillMaxWidth()) {
            HealthBar((clicks.intValue.toFloat() % 100) / 100f, modifier)
            TheClickButtonAndText(modifier, clicks)
        }
    }
}
