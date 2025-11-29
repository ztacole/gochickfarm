package com.zetta.gochickfarm.ui.navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Grass
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.sign

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector
) {
    object Dashboard : BottomNavItem(
        route = Screen.Dashboard.route,
        icon = Icons.Rounded.Home
    )

    object Animals : BottomNavItem(
        route = Screen.AnimalList.route,
        icon = Icons.Rounded.Pets
    )

    object Feeds : BottomNavItem(
        route = Screen.FeedList.route,
        icon = Icons.Rounded.Grass
    )

    object Transactions : BottomNavItem(
        route = Screen.TransactionList.route,
        icon = Icons.Rounded.MonetizationOn
    )
}

@Composable
fun BottomNavigationBar(
    onNavigate: (String) -> Unit,
    currentDestination: NavDestination?
) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Animals,
        BottomNavItem.Feeds,
        BottomNavItem.Transactions,
    )

    val density = LocalDensity.current
    var itemWidth by remember { mutableStateOf(0.dp) }

    val selectedIndex = items.indexOfFirst { item ->
        currentDestination?.hierarchy?.any { it.route == item.route } == true
    }.coerceAtLeast(0)

    var hasLaidOut by remember { mutableStateOf(false) }
    var lastClickTime by remember { mutableLongStateOf(0) }

    NavigationBar(
        modifier = Modifier
            .onGloballyPositioned {
                val totalWidth = it.size.width / density.density
                itemWidth = (totalWidth / items.size).dp
                hasLaidOut = true
            }
            .drawWithCache {
                onDrawBehind {
                    drawLine(
                        Color.Black.copy(alpha = 0.1f),
                        Offset(0f, 0f),
                        Offset(size.width, 0f),
                        1.dp.toPx()
                    )
                }
            },
        containerColor = Color.Transparent
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomStart
        ) {
            // 🟢 Indicator is now its own composable
            if (hasLaidOut && itemWidth > 0.dp) {
                StretchyIndicator(
                    selectedIndex = selectedIndex,
                    itemWidth = itemWidth,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }

            Row(Modifier.fillMaxWidth()) {
                items.forEachIndexed { index, item ->
                    val selected =
                        currentDestination?.hierarchy?.any { it.route == item.route } == true
                    val selectedGradientColors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent
                    )

                    val isSelected by rememberUpdatedState(selected)

                    val showHighlight by produceState(initialValue = false, key1 = isSelected) {
                        if (isSelected) {
                            delay(400)
                            value = true
                        } else {
                            value = false
                        }
                    }

                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.route) },
                        selected = selected,
                        onClick = {
                            val currentTime = System.currentTimeMillis()
                            if (index != selectedIndex && currentTime - lastClickTime > 500) {
                                lastClickTime = currentTime
                                onNavigate(item.route)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .drawWithCache {
                                val gradient = Brush.verticalGradient(selectedGradientColors)
                                onDrawWithContent {
                                    drawContent()
                                    if (showHighlight) {
                                        drawLine(
                                            gradient,
                                            start = Offset(0f, 0f),
                                            end = Offset(size.width, 0f),
                                            strokeWidth = with(density) { 48.dp.toPx() }
                                        )
                                    }
                                }
                            },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.outline,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun StretchyIndicator(
    selectedIndex: Int,
    itemWidth: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val indicatorOffset = remember { Animatable(0f) }
    var isFirstDraw by remember { mutableStateOf(true) }

    LaunchedEffect(selectedIndex, itemWidth) {
        if (itemWidth == 0.dp) return@LaunchedEffect

        val targetOffset = with(density) { (itemWidth * selectedIndex).toPx() }

        if (isFirstDraw) {
            // 👇 Instantly jump to position at startup (no lag)
            indicatorOffset.snapTo(targetOffset)
            isFirstDraw = false
        } else {
            // 👇 Smooth slide animation
            indicatorOffset.animateTo(
                targetValue = targetOffset,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            )
        }
    }

    Box(
        modifier = modifier
            .offset { IntOffset(indicatorOffset.value.roundToInt(), 0) }
            .width(itemWidth)
            .height(4.dp)
            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            .background(color)
    )
}
