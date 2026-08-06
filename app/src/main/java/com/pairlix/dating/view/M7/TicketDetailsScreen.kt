package com.pairlix.dating.view.M7

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.formatDate
import com.pairlix.dating.viewModel.M7ViewModel

@Composable
fun TicketDetailsScreen(navController: NavController,m7ViewModel:M7ViewModel) {

    var searchText by remember { mutableStateOf("") }
    val ticket by m7ViewModel.selectedTicket.collectAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {

        val maxHeight = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
               .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .statusBarsPadding()
        ) {


            TopBackBtnHeading(navController, stringResource(R.string.tickets))
            verticalSpace(20)


            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.weight(1f),
                              text = "Title-${ticket?.titleName ?: ""}",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        )

                        Text(
                            modifier = Modifier,
                            text = when (ticket?.status) {
                                0 -> stringResource(R.string.pending)
                                1 -> stringResource(R.string.resolved)
                                else -> ""
                            },
                            color = if (ticket?.status==0) Color(0xFF153EC5) else Color.Green,
                            fontSize = 10.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                            // light gray like your image
                        )

                    }

                    verticalSpace(10)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "  ${stringResource(R.string.ticket_id)} ${ticket?.ticketId ?: "N/A"}",
                            color = Color(0xFF6D6D6D),
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_medium))
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                modifier = Modifier,
                                text = formatDate(ticket?.createdAt ?: ""),
                                color = Color(0xFF6D6D6D),
                                fontSize = 10.sp,
                                fontFamily = FontFamily(Font(R.font.axiforma_medium))
                            )
                            horizontalSpace(5)
                            Image(
                                painter = painterResource(R.drawable.calendar_date),
                                contentDescription = "",
                                modifier = Modifier.size(14.dp)
                            )


                        }
                    }


                }

            }


            verticalSpace(20)


            Text(
                modifier = Modifier,
                text = stringResource(R.string.description),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium))
            )

            verticalSpace(10)

            Text(
                modifier = Modifier,
                text = ticket?.description ?: "N/A",
                color = Color(0xFF6D6D6D),
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
            )


            verticalSpace(30)
            Text(
                modifier = Modifier,
                text = stringResource(R.string.ticket_type),
                                color = MaterialTheme.colorScheme.onBackground,

                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium))
            )

            verticalSpace(10)

            Text(
                modifier = Modifier,
                text = ticket?.ticketType ?: "N/A",
                color = Color(0xFF6D6D6D),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
            )


            verticalSpace(30)
            val attachments = ticket?.attachImage?.filterNotNull() ?: emptyList()

            if(!attachments.isNullOrEmpty()) {
                Text(
                    modifier = Modifier,
                    text = stringResource(R.string.attachment),
                    color = MaterialTheme.colorScheme.onBackground,

                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium))
                )
                verticalSpace(10)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 2000.dp)
                ) {
                    items(attachments) { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = "Attachment",
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                verticalSpace(20)
            }

            if(ticket?.status==1){
            Text(
                modifier = Modifier,
                text = stringResource(R.string.replied_by_admin),
                                color = MaterialTheme.colorScheme.onBackground,

                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_medium))
            )

            verticalSpace(10)

            Text(
                modifier = Modifier,
                text = ticket?.resolvedReason ?: "",
                color = Color(0xFF6D6D6D),
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
            )
        }}
    }
}
