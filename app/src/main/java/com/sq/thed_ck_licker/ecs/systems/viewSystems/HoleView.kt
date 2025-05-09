package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.R

@Composable
fun HoleView(modifier: Modifier, onClickListener: () -> Unit) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .background(color = Color.Magenta)
            .paint(
                painterResource(R.drawable.placeholder),
                contentScale = ContentScale.FillBounds
            )
            .clickable { onClickListener() }
    ) {
        Column(
            modifier = Modifier
                .align(BiasAlignment(0f, 0.7f))
                .fillMaxWidth()
        ) {
            Text(
                text = "hole",
                modifier = Modifier
                    .background(color = Color.Cyan)
                    .fillMaxWidth()
            )
            Text(
                text = "Drop card in hole",
                softWrap = true,
                modifier = Modifier
                    .background(color = Color.Yellow)
                    .fillMaxWidth()
            )
        }
    }
}