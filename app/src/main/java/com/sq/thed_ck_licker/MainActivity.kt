package com.sq.thed_ck_licker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sq.thed_ck_licker.player.HealthBar
import com.sq.thed_ck_licker.ui.theme.TheD_ck_LickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheD_ck_LickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HealthBar(0f, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheD_ck_LickerTheme {
        HealthBar(0f)
    }
}