package com.mir.repgit.ui.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mir.repgit.R
import com.mir.repgit.tools.SizeManager
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun WelcomeButton(modifier: Modifier,
                  expanded:Boolean,
                  onClick: () -> Unit,
                  ) {
    val sizeManager = SizeManager(
        LocalConfiguration.current.screenHeightDp,
        LocalConfiguration.current.screenWidthDp
    )
    val coroutineScope = rememberCoroutineScope()

    val secondCircleRadius = remember {
        Animatable(85.dp.value)
    }

    val offset = remember {
        Animatable(
            Offset.Zero, Offset.VectorConverter
        )
    }

    var close by remember {
        mutableStateOf(false)
    }
    AnimatedVisibility(
        visible =  !close,
        exit = fadeOut() +shrinkHorizontally(tween(1000))
    ) {
        Box(
            modifier = modifier.offset {
                IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt())
            },
            contentAlignment = Alignment.Center
        ) {

            IconButton(
                onClick = {
                    onClick.invoke()
                    coroutineScope.launch {
                        secondCircleRadius.animateTo(1200.dp.value)
                        offset.animateTo(
                            sizeManager.giveNeed(
                                compactSize = Offset(
                                    x =  50.dp.value,
                                    y =-sizeManager.center.y/2f
                                ),
                                mediumSize = Offset(
                                    x =  100.dp.value,
                                    y = -sizeManager.center.y/1.6f
                                ),
                                expandedSize = Offset(
                                    x = 150.dp.value,
                                    y = -sizeManager.center.y/1.5f
                                ),

                                )
                        )
                        secondCircleRadius.animateTo(0.dp.value)
                         close = expanded
//                        offset.animateTo(Offset.Zero )
//                        isExpanded = false
//                        close = isExpanded
                    }
                },
                modifier = Modifier
                    .zIndex(1f)
                    .matchParentSize()
                    .drawBehind {
                        drawCircle(color = Color(0xFF10194E), radius = if (secondCircleRadius.value==0f)0f else secondCircleRadius.value+20f)
                        drawCircle(
                            color = Color.Black, alpha = 0.1f,
                            radius = if (secondCircleRadius.value==0f)0f else secondCircleRadius.value + 5f
                        )
                        drawCircle(
                            color = Color(0xFF192259),
                            radius = secondCircleRadius.value
                        )
                    }
            ) {
                Image(
                    modifier = Modifier.size(secondCircleRadius.value.dp),
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "big search"
                )

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewWelcomeButton() {
    var expandedWelcome by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        WelcomeButton(modifier = Modifier.fillMaxSize(0.5f),
            expandedWelcome
        ) { expandedWelcome = !expandedWelcome }
    }
}