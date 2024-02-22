package com.mir.repgit.screens.repository

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mir.repgit.R
import com.mir.repgit.ui.layout.BackgroundContainer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryScreen() {
    val sheetState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    BackgroundContainer(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            color = Color(0xFF1DC7AC),
                            alpha = 1f,
                            strokeWidth = 2.dp.toPx()
                        )
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(modifier = Modifier, onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color(0xFFFAFAFA)
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    text = "Repository",
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }

            BottomSheetScaffold(
                scaffoldState = sheetState,
                sheetContent = {
                    LazyColumn(
                        content = {
                            items(30) {
                                Spacer(modifier = Modifier.size(20.dp))
                                Text(
                                    text = "text", modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color(0x337B22A5)
                                        )
                                        .padding(4.dp),
                                    style = TextStyle(fontSize = 20.sp)
                                )
                            }
                        },
                        contentPadding = PaddingValues(8.dp),
                    )
                },
                sheetContainerColor = Color(0xE610194E),
                sheetContentColor = Color(0xFF1DC7AC),
                contentColor = Color.Transparent,
                containerColor = Color.Transparent,


                ) {
                Row(
                    Modifier.padding(
                        paddingValues = PaddingValues(
                            horizontal = 8.dp,
                            vertical = 16.dp
                        )
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_default),
                        contentDescription = "avatar default"
                    )
                    Column {
                        Text(text = " name", color = Color.White)
                        Text(text = " Description", color = Color.White)
                    }
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRepositoryScreen() {
    RepositoryScreen()
}