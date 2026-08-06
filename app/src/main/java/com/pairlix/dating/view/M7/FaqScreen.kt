package com.pairlix.dating.view.M7

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.TopBackBtnHeading
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.response.FaqResponse
import com.pairlix.dating.viewModel.M7ViewModel


data class FaqItem(
    val question: String,
    val answer: String)

@Composable
fun FaqScreen(navController: NavController,m7ViewModel:M7ViewModel) {

    var expandedIndex by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val faq by m7ViewModel.getFaq.collectAsState()

    var faqList = remember { mutableStateListOf<FaqResponse.Data?>(null) }

   /* LaunchedEffect (Unit) {
        m7ViewModel.hitGetFaq(
            token = SharedPreference.get(context).accessToken,
            lang = "en"
        )
    }
*/
    val languageManager = LocalLanguageManager.current
    LaunchedEffect(languageManager.currentLanguage) {

        m7ViewModel.hitGetFaq(
            token = SharedPreference.get(context).accessToken,
            lang = if (languageManager.currentLanguage == "ar") "ar" else "en"
        )
    }


    LaunchedEffect(faq) {

        faq.let { state ->

            when (state) {

                is EmpResource.Loading -> {
                    // loader
                }

                is EmpResource.Success -> {

                    CustomLoader.hideLoader()

                    faqList.clear()
                    faqList.addAll(state.value.data?.filterNotNull()?: emptyList())

                    m7ViewModel.resetGetFaq()
                }

                is EmpResource.Failure -> {

                    CustomLoader.hideLoader()

                    state.throwable.let { err ->
                        ErrorUtil.handlerGeneralError(context, err)
                    }

                    m7ViewModel.resetGetFaq()
                }

                EmpResource.Idle -> {
                    CustomLoader.hideLoader()
                }
            }
        }
    }

    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {

        val maxHeight= this.maxHeight

        Column (modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)){


            TopBackBtnHeading(navController, stringResource(R.string.faq_s))

            verticalSpace(30)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(faqList) { index, item ->

                    if (item != null) {
                        FaqCard(
                            data = item,
                            isExpanded = expandedIndex == index,
                            onClick = {
                                expandedIndex = if (expandedIndex == index) -1 else index
                            }
                        )
                    }
                }
            }

        }


    }

}

@Composable
fun FaqCard(
    data: FaqResponse.Data,
    isExpanded: Boolean,
    onClick: () -> Unit
) {

    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(        containerColor = MaterialTheme.colorScheme.surface)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp)
        ) {

            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text =  data?.question?:"",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    modifier = Modifier.weight(1f)
                )

                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation)
                )
            }

            AnimatedVisibility(visible = isExpanded) {

                Column {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = data?.answer?:"",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                        color = Color(0xFF6D6D6D),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}