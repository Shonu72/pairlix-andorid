package com.pairlix.dating.view.home


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import ir.kaaveh.sdpcompose.sdp

// ─── Shimmer brush ────────────────────────────────────────────────────────────

@Composable
fun shimmerBrush(
    shimmerColors: List<Color> = listOf(
        Color(0xFFE0E0E0).copy(alpha = 0.6f),
        Color(0xFFF5F5F5).copy(alpha = 0.9f),
        Color(0xFFE0E0E0).copy(alpha = 0.6f),
    )
): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 400f, 0f),
        end = Offset(translateAnim, 0f)
    )
}

// Dark-mode aware shimmer
@Composable
fun adaptiveShimmerBrush(): Brush {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    return if (isDark) {
        shimmerBrush(
            listOf(
                Color(0xFF2A2A2A).copy(alpha = 0.8f),
                Color(0xFF3D3D3D).copy(alpha = 0.9f),
                Color(0xFF2A2A2A).copy(alpha = 0.8f),
            )
        )
    } else {
        shimmerBrush()
    }
}

// Simple helper: Color.luminance() analogue
fun Color.luminance(): Float {
    return (red * 0.299f + green * 0.587f + blue * 0.114f)
}


@Composable
fun SkeletonBox(
    modifier: Modifier,
    brush: Brush = adaptiveShimmerBrush(),
    shape: RoundedCornerShape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(brush)
    )
}

@Composable
fun SkeletonCircle(
    size: androidx.compose.ui.unit.Dp,
    brush: Brush = adaptiveShimmerBrush()
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(brush)
    )
}

// ─── Main skeleton ────────────────────────────────────────────────────────────

/**
 * Drop this in place of your real HomeScreen content while [isLoading] is true.
 *
 * Usage in HomeScreen:
 *
 *   var hasLoadedOnce by remember { mutableStateOf(false) }
 *
 *   LaunchedEffect(viewModel.getMatchList.isNotEmpty()) {
 *       if (viewModel.getMatchList.isNotEmpty()) hasLoadedOnce = true
 *   }
 *
 *   if (!hasLoadedOnce) {
 *       HomeSkeletonScreen()
 *   } else {
 *       // your real content
 *   }
 */
@Composable
fun HomeSkeletonScreen() {
    val brush = adaptiveShimmerBrush()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {

        // ── Top bar ──────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 14.sdp, end = 14.sdp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Filter icon placeholder
            SkeletonBox(
                modifier = Modifier.size(36.dp),
                brush = brush,
                shape = RoundedCornerShape(40.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Boost button placeholder
            SkeletonCircle(size = 40.dp, brush = brush)

            Spacer(modifier = Modifier.width(12.sdp))

            // Notification bell placeholder
            SkeletonBox(
                modifier = Modifier.size(36.dp),
                brush = brush,
                shape = RoundedCornerShape(40.dp)
            )
        }

        // ── Profile card ─────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(brush)
        ) {

            // ── Pager dots ────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 110.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (index == 0) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = if (index == 0) 0.9f else 0.4f))
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Match % circle placeholder
                SkeletonCircle(
                    size = 40.dp,
                    brush = shimmerBrush(
                        listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.White.copy(alpha = 0.5f),
                            Color.White.copy(alpha = 0.3f),
                        )
                    )
                )
            }

            // ── Profile info at bottom ─────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
            ) {
                // Plan icon placeholder
                SkeletonBox(
                    modifier = Modifier
                        .size(width = 50.dp, height = 24.dp),
                    brush = shimmerBrush(
                        listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.2f),
                        )
                    ),
                    shape = RoundedCornerShape(6.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Name line
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .height(22.dp),
                    brush = shimmerBrush(
                        listOf(
                            Color.White.copy(alpha = 0.25f),
                            Color.White.copy(alpha = 0.45f),
                            Color.White.copy(alpha = 0.25f),
                        )
                    ),
                    shape = RoundedCornerShape(6.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Location line
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SkeletonCircle(
                        size = 18.dp,
                        brush = shimmerBrush(
                            listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.White.copy(alpha = 0.35f),
                                Color.White.copy(alpha = 0.2f),
                            )
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    SkeletonBox(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(14.dp),
                        brush = shimmerBrush(
                            listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.White.copy(alpha = 0.35f),
                                Color.White.copy(alpha = 0.2f),
                            )
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Profession tag
                SkeletonBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(28.dp),
                    brush = shimmerBrush(
                        listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.2f),
                        )
                    ),
                    shape = RoundedCornerShape(48.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Education tag
                SkeletonBox(
                    modifier = Modifier
                        .width(100.dp)
                        .height(28.dp),
                    brush = shimmerBrush(
                        listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.2f),
                        )
                    ),
                    shape = RoundedCornerShape(48.dp)
                )
            }

            // ── Active badge placeholder (top-left) ────────────────────────
            SkeletonBox(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 10.dp, top = 10.dp)
                    .width(70.dp)
                    .height(26.dp),
                brush = shimmerBrush(
                    listOf(
                        Color.White.copy(alpha = 0.2f),
                        Color.White.copy(alpha = 0.35f),
                        Color.White.copy(alpha = 0.2f),
                    )
                ),
                shape = RoundedCornerShape(48.dp)
            )
        }

        // ── Action buttons row ────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.sdp, vertical = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonCircle(size = 45.sdp, brush = brush)
            Spacer(modifier = Modifier.width(25.sdp))
            SkeletonCircle(size = 45.sdp, brush = brush)
            Spacer(modifier = Modifier.width(25.sdp))
            SkeletonCircle(size = 45.sdp, brush = brush)
        }

        Spacer(modifier = Modifier.height(50.sdp))
    }
}




@Composable
fun SkeletonGrid() {
    val shimmerItems = 6 // shows 3 rows of 2

    Column(modifier = Modifier.fillMaxWidth()) {
        repeat(shimmerItems / 2) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(160.sdp)
                            .clip(RoundedCornerShape(12.dp))
                            .shimmer()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        // Bottom info bar skeleton
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Name row skeleton
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White.copy(alpha = 0.4f))
                            )
                            // Location row skeleton
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White.copy(alpha = 0.3f))
                            )
                        }
                    }
                }
            }
        }
    }
}
