package mir.anika1d.repgit.ui.composable.layout

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.onConsumedWindowInsetsChanged
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonSkippableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import mir.anika1d.core.resources.AppResources
import mir.anika1d.repgit.core.data.state.SearchFilter
import mir.anika1d.repgit.core.ui.theme.LocalPallet
import kotlin.math.max
import kotlin.math.min


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
@NonSkippableComposable
fun SearchField(
    modifier: Modifier,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    shape: Shape = RoundedCornerShape(22f),
    searchState: LazyListState = rememberLazyListState(),
    focusManager: FocusManager = LocalFocusManager.current,
    searchText: String,
    isFocus: Boolean,
    content: LazyListScope.() -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    onChangeSearchValue: (String) -> Unit,
    expandedMode: Boolean,
    onExpandedModeChange: (Boolean) -> Unit,
    itemsMode: List<SearchFilter>,
    selectedModeItem: SearchFilter,
    onItemModeChange: (SearchFilter) -> Unit


) {
    val localPallet = LocalPallet.current
    val interactionSource = remember { MutableInteractionSource() }
    val animationProgress: State<Float> = animateFloatAsState(
        targetValue = if (isFocus) 1f else 0f,
        animationSpec = if (isFocus)
            tween(
                durationMillis = 600,
                delayMillis = 100,
                easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f),
            ) else tween(
            durationMillis = 350,
            delayMillis = 100,
            easing = CubicBezierEasing(0.0f, 1.0f, 0.0f, 1.0f),
        ), label = ""
    )

    val density = LocalDensity.current

    val defaultInputFieldShape = SearchBarDefaults.inputFieldShape
    val defaultFullScreenShape = SearchBarDefaults.fullScreenShape
    val useFullScreenShape by remember {
        derivedStateOf(structuralEqualityPolicy()) { animationProgress.value == 1f }
    }
    val animatedShape = remember(useFullScreenShape, shape) {
        when {
            shape == defaultInputFieldShape ->
                GenericShape { size, _ ->
                    val radius = with(density) {
                        (28.dp * (1 - animationProgress.value)).toPx()
                    }
                    addRoundRect(RoundRect(size.toRect(), CornerRadius(radius)))
                }

            useFullScreenShape -> defaultFullScreenShape
            else -> shape
        }
    }


    val unconsumedInsets =
        remember { androidx.compose.foundation.layout.MutableWindowInsets() }
    val topPadding = remember(density) {
        derivedStateOf {
            8.dp +
                    unconsumedInsets.asPaddingValues(density).calculateTopPadding()
        }
    }
    val windowInsets = WindowInsets(0, 0, 0, 0)

    Surface(
        shape = animatedShape,
        color = localPallet.darkBlue.copy(alpha = 0.6f),
        contentColor = contentColorFor(localPallet.darkBlue),
        modifier = modifier
            .zIndex(1f)
            .onConsumedWindowInsetsChanged { consumedInsets ->
                unconsumedInsets.insets = windowInsets.exclude(consumedInsets)
            }
            .consumeWindowInsets(unconsumedInsets)
            .layout { measurable, constraints ->
                val animatedTopPadding =
                    lerp(topPadding.value, 0.dp, animationProgress.value).roundToPx()

                val startWidth = max(constraints.minWidth, 360.dp.roundToPx())
                    .coerceAtMost(min(constraints.maxWidth, 720.dp.roundToPx()))
                val startHeight =
                    max(
                        constraints.minHeight,
                        SearchBarDefaults.InputFieldHeight.roundToPx()
                    )
                        .coerceAtMost(constraints.maxHeight)
                val endWidth = constraints.maxWidth
                val endHeight = constraints.maxHeight

                val width = lerp(startWidth, endWidth, animationProgress.value)
                val height =
                    lerp(
                        startHeight,
                        endHeight,
                        animationProgress.value
                    ) + animatedTopPadding

                val placeable = measurable.measure(
                    Constraints
                        .fixed(width, height)
                        .offset(vertical = -animatedTopPadding)
                )
                layout(width, height) {
                    placeable.placeRelative(0, animatedTopPadding)
                }
            }
            .border(
                width = 2.dp,
                color = localPallet.mint,
                shape = shape
            )
            .clip(shape)

    ) {
        Column {
            val focusRequester = remember { FocusRequester() }
            val searchSemantics = "search_bar_custom"
            val suggestionsAvailableSemantics = "suggestions_available"
            val textColor = LocalTextStyle.current.color.takeOrElse {
                colors.disabledTextColor
            }

            Row(modifier = Modifier.drawBehind {
                drawLine(
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    color = localPallet.mint,
                    alpha = 1f,
                    strokeWidth = 2.dp.toPx()
                )
            }, verticalAlignment = Alignment.CenterVertically) {
                DropDownModeSearch(
                    expanded = expandedMode,
                    onExpandedChange = onExpandedModeChange,
                    items = itemsMode.map { it.value },
                    selectedItem = selectedModeItem.value,
                    onItemChange = { onItemModeChange.invoke(SearchFilter.of(it)) },
                    modifier = Modifier
                        .padding(top = 4.dp, start = 4.dp)
                        .fillMaxWidth(0.2f)
                        .height(SearchBarDefaults.InputFieldHeight)
                        .background(Color.Transparent)
                        .drawBehind {

                            drawLine(
                                color = localPallet.mint,
                                strokeWidth = 1.dp.toPx(),
                                start = Offset(this.size.width, 0f),
                                end = Offset(this.size.width, size.height),
                            )
                        }
                )
                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        onChangeSearchValue(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(SearchBarDefaults.InputFieldHeight)
                        .fillMaxWidth()
                        .drawBehind {
                            drawArc(
                                topLeft = Offset(size.width - center.x / 2, 0f),
                                useCenter = false,
                                startAngle = 80f,
                                sweepAngle = -160f,
                                color = localPallet.mint,
                                size = Size(center.x / 2, size.height),
                                style = Stroke(width = 2.dp.toPx()),
                            )
                        }
                        .clip(RoundedCornerShape(22f))
                        .focusRequester(focusRequester)
                        .onFocusChanged { if (it.isFocused) onActiveChange(true) }
                        .semantics {
                            contentDescription = searchSemantics
                            if (isFocus) {
                                stateDescription = suggestionsAvailableSemantics
                            }
                            onClick {
                                focusRequester.requestFocus()
                                true
                            }
                        },
                    enabled = true,
                    textStyle = LocalTextStyle.current.merge(TextStyle(color = textColor)),
                    cursorBrush = SolidColor(colors.cursorColor),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        focusManager.clearFocus()
                        onSearch(searchText)
                    }
                    ),
                    interactionSource = interactionSource,
                    decorationBox = @Composable { innerTextField ->

                        TextFieldDefaults.DecorationBox(
                            value = searchText,
                            innerTextField = innerTextField,
                            enabled = true,
                            singleLine = true,
                            visualTransformation = VisualTransformation.None,
                            interactionSource = interactionSource,
                            shape = SearchBarDefaults.inputFieldShape,
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = localPallet.darkBlue,
                                focusedContainerColor = localPallet.darkBlue,
                                unfocusedPlaceholderColor = localPallet.mint,
                                focusedPlaceholderColor = localPallet.mint,
                                focusedTextColor = localPallet.dirtyWhite,
                                unfocusedTextColor = localPallet.dirtyWhite,
                                cursorColor = localPallet.dirtyWhite,

                                ),

                            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(),
                        )
                    }
                )
                IconButton(modifier = Modifier.fillMaxWidth(), onClick = {
                    if (!isFocus) onActiveChange(true)
                    focusManager.clearFocus()
                    onSearch(searchText)
                }) {
                    Image(
                        painter = painterResource(id = AppResources.Drawable.SEARCH.id),
                        contentDescription = "search"
                    )
                }
            }
            val showResults by remember {
                derivedStateOf(structuralEqualityPolicy()) { animationProgress.value > 0 }
            }
            if (showResults) {
                LazyColumn(
                    content = content,
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = animationProgress.value }
                        .animateContentSize(),
                    state = searchState,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
            }
        }
    }

    val isFocused = interactionSource.collectIsFocusedAsState().value
    val shouldClearFocus = !isFocus && isFocused
    LaunchedEffect(isFocus) {
        if (shouldClearFocus) {
            delay(100)
            focusManager.clearFocus()
        }
    }
    BackHandler(enabled = isFocus) {
        onActiveChange(false)
    }


}
