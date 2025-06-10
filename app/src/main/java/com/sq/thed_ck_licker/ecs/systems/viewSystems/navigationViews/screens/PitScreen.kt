package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.viewModels.PitViewModel

@Composable
fun PitScreen(modifier: Modifier, pitViewModel: PitViewModel = hiltViewModel()) {



    Box(
        modifier = modifier
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
            modifier = modifier
                .align(BiasAlignment(0f, 0.7f))
                .fillMaxWidth()
        ) {
            Text(
                text = "Pit",
                modifier = modifier
                    .background(color = Color.Cyan)
                    .fillMaxWidth()
            )
            Text(
                text = "Drop card in the pit",
                softWrap = true,
                modifier = modifier
                    .background(color = Color.Yellow)
                    .fillMaxWidth()
            )
        }
    }
}