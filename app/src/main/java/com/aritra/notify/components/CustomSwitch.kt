package com.aritra.notify.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aritra.notify.R
import com.aritra.notify.screens.settingsScreen.ThemeViewModel

@Composable
fun CustomSwitch(
    width: Dp = 50.dp,
    height: Dp = 35.dp,
    checkedTrackColor: Color = Color(0xFF2c9fff).copy(alpha = 0.7f),
    uncheckedTrackColor: Color = Color.Gray,
    gapBetweenThumbAndTrackEdge: Dp = 4.dp,
    borderWidth: Dp = 2.dp,
    cornerSize: Int = 50,
    iconInnerPadding: Dp = 4.dp,
    thumbSize: Dp = 20.dp,
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {

// this is to disable the ripple effect
    val interactionSource = remember {
        MutableInteractionSource()
    }


    val themeState by themeViewModel.themeState.collectAsState()


    // for moving the thumb
    val alignment by animateAlignmentAsState(
        if (themeState.isDarkMode
        ) 1f else -1f
    )

//    Box(
//        modifier = Modifier
//            .animateContentSize(
//                animationSpec = spring(
//                    dampingRatio = Spring.DampingRatioHighBouncy,
//                    stiffness = Spring.StiffnessMediumLow
//                )
//            ), contentAlignment = Alignment.CenterEnd
//    ) {
//        Text(
//            text = if (themeState.isDarkMode
//            ) "ON" else "OFF",
//            fontWeight = FontWeight.SemiBold,
//
//            modifier = Modifier,
//
//            )
//    }
// outer rectangle with border
    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .border(
                width = borderWidth,
                color = if (themeState.isDarkMode
                ) checkedTrackColor else uncheckedTrackColor,
                shape = RoundedCornerShape(percent = cornerSize)
            )
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                themeViewModel.toggleTheme()

            },
        contentAlignment = Alignment.Center
    ) {

        // this is to add padding at the each horizontal side
        Box(
            modifier = Modifier
                .padding(
                    start = gapBetweenThumbAndTrackEdge,
                    end = gapBetweenThumbAndTrackEdge
                )
                .fillMaxSize(),
            contentAlignment = alignment
        ) {

            // thumb with icon
            Icon(
                if (themeState.isDarkMode
                ) painterResource(id = R.drawable.night_mode) else painterResource(id = R.drawable.light_mode),
                contentDescription = if (themeState.isDarkMode) "Enabled" else "Disabled",
                modifier = Modifier
                    .size(size = thumbSize)
                    .background(
                        color = if (themeState.isDarkMode) checkedTrackColor else uncheckedTrackColor,
                        shape = CircleShape
                    )
                    .padding(all = iconInnerPadding),
                tint = Color.Black
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun animateAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment> {
    val bias by animateFloatAsState(targetBiasValue)
    return derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) }
}