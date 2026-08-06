package com.pairlix.dating.view.newAccountRegistrationScreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pairlix.dating.R

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Practice() {

    val list = listOf<Items>(
        Items("Name", "Rajesh"),
        Items("Age", "23"),
        Items("Gender", "Male"),
        Items("Gender", "Male"),

        )


    val habits = listOf(
        HabitItem(R.drawable.smoking_im,  "Smoking",  "Never"),
        HabitItem(R.drawable.smoking_im, "Drinking", "Never"),
        HabitItem(R.drawable.smoking_im,  "Workout",  "4X/Week"),
    )



    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface

            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),


            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                TextHeading(text = stringResource(R.string.education))
                Spacer(Modifier.height(8.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(list.size) { index ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                list[index].item,
                                color = Color(0xff6D6D6D),
                                fontSize = 14.sp,
                                fontFamily = FontFamily(
                                    Font(R.font.axiforma_regular)
                                )
                            )
                            Text(
                                list[index].value,
                                modifier = Modifier.weight(1f),
                                color = Color(0xff000000),
                                fontSize = 14.sp,
                                fontFamily = FontFamily(
                                    Font(R.font.axiforma_regular)
                                ),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }

            }


        }
        Spacer(Modifier.height(20.dp))
        HabitsCard(habits = habits)

    }
}

@Composable
fun HabitsCard(
    habits: List<HabitItem>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Title
            Text(
                text = stringResource(R.string.habits),
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium))
            )

            Spacer(Modifier.height(8.dp))

            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = false
            ) {
                items(habits) { habit ->
                    HabitTile(habit = habit)
                }
            }
        }
    }
}

@Composable
fun HabitTile(habit: HabitItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFD9C8FF),  // lavender
                        Color(0xFFEFD8FF),  // soft pink-purple
                        Color(0xFFFFEFF8)   // light peach
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(habit.icon),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp),
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = habit.label,
            color = Color(0xff6D6D6D),
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_regular))
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = habit.value,
            color = Color(0xff590988),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.axiforma_regular))
        )
    }
}
data class Items(
    val item: String,
    val value: String
)
data class HabitItem(
    @DrawableRes val icon: Int,
    val label: String,
    val value: String
)

@Composable
fun TextHeading(text: String) {
    Text(
        text,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 16.sp,
        fontFamily = FontFamily(
            Font(R.font.axiforma_medium)
        )
    )
}


//Column(
//modifier = Modifier
//.weight(1f)
//.background(
//brush = Brush.linearGradient(
//colors = listOf(
//Color(0xFFD9C8FF),  // lavender
//Color(0xFFEFD8FF),  // soft pink-purple
//Color(0xFFFFEFF8)
//)
//),
//shape = RoundedCornerShape(8.dp)
//)
//.padding(horizontal = 12.dp, vertical = 8.dp)
//) {
//    Image(
//        painter = painterResource(R.drawable.smoking_im),
//        contentDescription = null,
//        modifier = Modifier
//            .size(24.dp),
//    )
//    Spacer(Modifier.height(8.dp))
//    Text(
//        "Smoking", color = Color(0xff6D6D6D),
//        fontSize = 12.sp,
//        fontFamily = FontFamily(Font(R.font.axiforma_regular))
//    )
//    Spacer(Modifier.height(8.dp))
//    Text(
//        "Never", color = Color(0xff590988),
//        fontSize = 16.sp,
//        fontFamily = FontFamily(Font(R.font.axiforma_regular))
//    )
//}
