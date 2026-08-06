package com.pairlix.dating.view.M4

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.valentinilk.shimmer.shimmer

@Composable
fun SkeletonItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray)
            .shimmer()
    )
}

@Composable
fun SkeletonGrid() {
    Column {
        repeat(6) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SkeletonItem(
                    modifier = Modifier.weight(1f)
                )

                SkeletonItem(
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun SkeletonChatItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.5f))
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(120.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .height(10.dp)
                    .width(180.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f))
            )
        }
    }
}

@Composable
fun ChatSkeletonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        repeat(20) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile image skeleton
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0))
                )

                horizontalSpace(10)

                Column(modifier = Modifier.weight(1f)) {
                    // Name skeleton
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE0E0E0))
                    )
                    verticalSpace(8)
                    // Message skeleton
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE0E0E0))
                    )
                }

                // Time skeleton
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE0E0E0))
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFEDF1F3)
            )
        }
    }
}