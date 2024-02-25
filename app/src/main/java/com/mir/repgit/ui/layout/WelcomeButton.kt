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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mir.repgit.R
import com.mir.repgit.tools.SizeManager
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun WelcomeButton(modifier: Modifier,
                  expanded:Boolean,
                  onClick: () -> Unit,
                  ) {
    val density= LocalDensity.current.density

    val sizeManager = SizeManager(
        LocalConfiguration.current.screenHeightDp*density.roundToInt(),
        LocalConfiguration.current.screenWidthDp*density.roundToInt()
    )
    val coroutineScope = rememberCoroutineScope()


    val secondCircleRadius = remember {
        Animatable(
            sizeManager.giveNeed(compactSize = sizeManager.center.x.dp/2.dp * density,
                expandedSize = sizeManager.center.x.dp/10.dp *density,
                mediumSize = sizeManager.center.x.dp/2.dp *density
        ))
    }

    val offset = remember {
        Animatable(
            Offset.Zero, Offset.VectorConverter
        )
    }
    var close by remember (Unit){
        mutableStateOf(expanded)
    }
    AnimatedVisibility(
        visible = close,
        exit = fadeOut() +shrinkHorizontally(tween(10))
    ) {
            IconButton(
                modifier = modifier
                    .zIndex(1f)
                    .offset {
                        IntOffset(offset.value.x.dp.roundToPx(), offset.value.y.dp.roundToPx())
                    }
                    .drawBehind {
                        drawCircle(
                            color = Color(0xFF10194E),
                            radius = if (secondCircleRadius.value == 0f) 0f else secondCircleRadius.value.dp.toPx() + 20.dp.toPx()
                        )
                        drawCircle(
                            color = Color.Black,
                            alpha = 0.1f,
                            radius = if (secondCircleRadius.value == 0f) 0f else secondCircleRadius.value.dp.toPx() + 5.dp.toPx()
                        )
                        drawCircle(
                            color = Color(0xFF192259), radius = secondCircleRadius.value.dp.toPx()
                        )
                    },
                onClick = {
                    onClick.invoke()
                    coroutineScope.launch {


                        secondCircleRadius.animateTo(
                            sizeManager.giveNeed(compactSize =  sizeManager.center.x * density,
                                expandedSize = sizeManager.center.x.dp/4.dp *density,
                                mediumSize =  sizeManager.center.x * density,
                            )



                           )
                        offset.animateTo(
                            sizeManager.giveNeed(
                                compactSize = Offset(
                                    x = sizeManager.center.x *density,
                                    y = - sizeManager.center.y *density
                                ),
                                mediumSize = Offset(
                                    x = sizeManager.center.x - 40f*density,
                                    y = - sizeManager.center.y *density
                                ),
                                expandedSize = Offset(
                                    x = sizeManager.center.x/2 - 40f*density,
                                    y = - sizeManager.center.y/10f*density
                                ),

                                )
                        )
                        secondCircleRadius.animateTo(0.dp.value)
                        close = false

                    }
                },
            ) {
                Image(
                    modifier = Modifier.size(secondCircleRadius.value.dp),
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "big search"
                )

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