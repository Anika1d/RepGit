package com.mir.repgit.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mir.repgit.ui.layout.BackgroundContainer
import com.mir.repgit.ui.layout.ItemRep
import com.mir.repgit.ui.layout.SearchField
import com.mir.repgit.ui.layout.WelcomeButton

@Composable
fun MainSearchWindow() {

    var value by remember {
        mutableStateOf("")
    }
    var active by remember {
        mutableStateOf(false)
    }
    var expandedWelcome by remember {
        mutableStateOf(false)
    }
    BackgroundContainer(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AnimatedVisibility(visible = expandedWelcome,
                enter = fadeIn() + slideInVertically (tween(800))  ) {


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(contentAlignment = Alignment.TopCenter){
                        IconButton(modifier = Modifier.defaultMinSize(20.dp,80.dp),onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "",
                                tint = Color(0xFFFAFAFA)
                            )
                        }
                    }
                    SearchField(
                        modifier = Modifier.fillMaxWidth(0.9f)
                        ,

                        value = value,
                        onValueChange = {
                            value = it
                        },
                        onActiveChange = { active = it },
                        active = active,
                        onSearch = {},
                        content = {
                            items(5){
                                ItemRep(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentPadding = PaddingValues(8.dp),
                                ) {

                                }
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.size(100.dp))

            WelcomeButton(
                modifier = Modifier
                    .fillMaxSize(0.5f),
                expanded=expandedWelcome,
                onClick={
                    expandedWelcome = !expandedWelcome
                }

            )

        }
    }
}