package com.pairlix.dating.view.newAccountRegistrationScreen

import android.text.style.AlignmentSpan
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.utils.SingletonObject
import ir.kaaveh.sdpcompose.sdp


@Composable
fun ProfileApprovedStatusScreen(navController: NavController) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()

    ) {
        val max = this.maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(top = 20.dp)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())

        ) {
            verticalSpace(30)
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.page_1),
                contentDescription = "page_1"
            )
            verticalSpace(30)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.your_profile_wasn_t_approved),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.axiforma_bold))
            )

            verticalSpace(30)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.please_fix_the_following_issues_before_your_profile_can_be_shown_to_other_members),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.axiforma_regular))
            )


            verticalSpace(20)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painterResource(R.drawable.blue1),
                    contentDescription = "verify",
                    modifier = Modifier.size(32.dp),
                )

                horizontalSpace(20)

                Column() {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.face_scan),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_bold))
                    )

                    verticalSpace(10)

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.approved),
                        color = Color(0xFF49ADF4),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )

                }

            }


            verticalSpace(20)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painterResource(R.drawable.cross_red),
                    contentDescription = "verify",
                    modifier = Modifier.size(30.dp),
                )

                horizontalSpace(20)

                Column() {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.id_proof),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_bold))
                    )

                    verticalSpace(10)

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.id_proof_not_approved_yet),
                        color = Color(0xFFEB0031),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )

                }

            }


        }

        AppButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.sdp),
            text = stringResource(R.string.Continue) ,
            onClick = {
                SingletonObject.isSkip=false
               navController.navigate(Screen.UploadIdScreen.route)
            }
        )

    }

}