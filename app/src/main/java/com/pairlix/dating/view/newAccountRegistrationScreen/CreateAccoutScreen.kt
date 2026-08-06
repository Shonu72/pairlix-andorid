package com.pairlix.dating.view.newAccountRegistrationScreen

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.abi.simplecountrypicker.DialogCountryPicker
import com.pairlix.dating.MainActivity
import com.pairlix.dating.R
import com.pairlix.dating.ReusedComponents.AppButton
import com.pairlix.dating.ReusedComponents.CustomDatePicker
import com.pairlix.dating.ReusedComponents.CustomInputField
import com.pairlix.dating.ReusedComponents.CustomRadioButton
import com.pairlix.dating.ReusedComponents.SearchBar
import com.pairlix.dating.ReusedComponents.SingleImagePicker
import com.pairlix.dating.ReusedComponents.getUserCountry
import com.pairlix.dating.ReusedComponents.horizontalSpace
import com.pairlix.dating.ReusedComponents.noInitialSpace
import com.pairlix.dating.ReusedComponents.showToast
import com.pairlix.dating.ReusedComponents.verticalSpace
import com.pairlix.dating.helper.CustomLoader
import com.pairlix.dating.helper.EmpResource
import com.pairlix.dating.helper.ErrorUtil
import com.pairlix.dating.helper.SharedPreference
import com.pairlix.dating.helper.getCountryNameFromCode
import com.pairlix.dating.helper.isValidEmail
import com.pairlix.dating.helper.isValidPhone
import com.pairlix.dating.navigation.Screen
import com.pairlix.dating.requests.CreateAccountRequest
import com.pairlix.dating.requests.UpdateProfileRequest
import com.pairlix.dating.response.GetCountryCodeResponse
import com.pairlix.dating.response.PreviewProfileResponse
import com.pairlix.dating.utils.SingletonObject
import com.pairlix.dating.viewModel.AuthViewModel
import okhttp3.MultipartBody
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import com.pairlix.dating.ReusedComponents.CityBottomSheetSelector
import com.pairlix.dating.ReusedComponents.CountryBottomSheetSelector
import com.pairlix.dating.requests.ModerateContentRequest
import com.pairlix.dating.response.UploadDocumentFileResponse
import com.pairlix.dating.view.allLoginScreen.GoogleLoginData
import com.pairlix.dating.view.allLoginScreen.generateToken
import com.pairlix.dating.viewModel.AuthViewModel.UploadSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.rememberCoroutineScope
import com.gravito.waiter_.Localization.Const

//with moderate
fun String.normalizeForSearch(): String {
    return java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "") // remove accents
        .replace(Regex("['''ʼ`]"), "'")  // normalize all apostrophe variants to standard '
        .lowercase()
        .trim()
}

fun UpdateCreateAccoutObserver(
    viewModel: AuthViewModel,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController,
) {
    viewModel.updateProfile.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Failure -> {
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
                CustomLoader.hideLoader()
            }

            EmpResource.Loading -> {
                CustomLoader.showLoader(context as Activity?)
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()

                if (state.value.success == true) {

                    context.showToast(state.value.message ?: "")
                    navController.popBackStack()
                    SingletonObject.isFromEditProfile = false
                    state.value.success = false
                }
            }

            EmpResource.Idle -> {}


            else -> {
                // no-op
            }
        }
    }


}

@Composable
fun PreviewProfileObserverOnce(
    viewModel: AuthViewModel, onData: (PreviewProfileResponse.Data) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val observer = Observer<EmpResource<PreviewProfileResponse>> { state ->
            if (state is EmpResource.Success && state.value.success == true) {
                onData(state.value.data!!)
                state.value.success = false
            }
        }

        viewModel.getPreviewProfile.observe(lifecycleOwner, observer)

        onDispose {

            viewModel.getPreviewProfile.removeObserver(observer)
        }
    }
}

fun createAccountObserver(
    context: MainActivity,
    viewModel: AuthViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController,
) {
    viewModel.createAccount.observe(lifecycleOwner) {
        when (it) {
            is EmpResource.Failure -> {
                it.throwable?.let { it1 ->
                    ErrorUtil.handlerGeneralError(context, it1)
                }
                CustomLoader.hideLoader()
            }

            EmpResource.Loading -> {
                CustomLoader.showLoader(context)
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()
                if (it.value.success == true) {
                    SingletonObject.isComeFromRegister = true
                    navController.navigate(Screen.OtpScreen.route)
                    SharedPreference.get(context).accessToken =
                        it.value.data?.accessToken.toString()
                    SingletonObject.accessToken= it.value.data?.accessToken.toString()

                    viewModel.clearSocialUniqueId()
                    it.value.success = false
                }
            }

            else -> {

            }
        }
    }
    viewModel.getCityByCountryCode.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Loading -> {
            }

            is EmpResource.Failure -> {
                CustomLoader.hideLoader()
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()
                if (state.value.success == true) {

                    viewModel.cityList1.clear()
                    state.value.data?.cities.let {
                        it?.let { elements ->
                            viewModel.cityList1.addAll(
                                elements
                            )
                        }
                    }

                    state.value.success = false
                }
            }

            EmpResource.Idle -> {}


        }
    }
}

@Composable
fun UploadObserverOnce(
    viewModel: AuthViewModel, context: MainActivity
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val observer = Observer<EmpResource<UploadDocumentFileResponse>> {
            when (it) {
                EmpResource.Idle -> {}

                EmpResource.Loading -> {
                    viewModel.isUploading.value = true
                }

                is EmpResource.Success -> {
                    viewModel.isUploading.value = false

                    val uploadedData = it.value.data?.firstOrNull()

                    viewModel.profileImage.value = uploadedData?.documentImageUrl ?: ""


                    /*viewModel.hitModerateContent(
                        access_token = SharedPreference.get(context).accessToken,
                        request = ModerateContentRequest(
                            imageKey = listOf(uploadedData?.documentImageKey ?: "")
                        )
                    )*/
                }

                is EmpResource.Failure -> {
                    viewModel.isUploading.value = false
                    ErrorUtil.handlerGeneralError(context, it.throwable)


                }
            }
        }

        viewModel.uploadImageFile.observe(lifecycleOwner, observer)

        onDispose {
            viewModel.resetData()
            viewModel.uploadImageFile.removeObserver(observer)
        }
    }
}

//  CustomLoader.showLoader(context)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(navController: NavController, viewModel: AuthViewModel) {
    val context = LocalContext.current
    val languageManager = LocalLanguageManager.current
    var countryNameEn by remember { mutableStateOf("") }
    var countryNameAr by remember { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var firstName by rememberSaveable { mutableStateOf("") }
    var mobileNo by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("male") }
    var countryCode by rememberSaveable { mutableStateOf(Const.countryCode) }
    var dialCode by rememberSaveable { mutableStateOf("") }
    var countryShort by rememberSaveable { mutableStateOf("") }
    var countryName by rememberSaveable { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var  token by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedCityName by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedCity by remember { mutableStateOf<GetCountryCodeResponse.Data.City?>(null) }
    val isImageUploading by viewModel.isUploading.collectAsState()
    val isEditMode = SingletonObject.isFromEditProfile
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() }
    val isValidEmail = isEmailValid(email)
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    val isMobileValid = mobileNo.length in 7..15
    val showError = mobileNo.isNotEmpty() && !isMobileValid
    //val countryList = stringArrayResource(R.array.country_list).toList()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Add these near your other remember states
    var selectedLatitude by remember { mutableStateOf<Double?>(null) }
    var selectedLongitude by remember { mutableStateOf<Double?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val latLng by viewModel.latLngFlow.collectAsStateWithLifecycle()
    val uniqueId = viewModel.socialUniqueId
    Log.d("CreateAccount", "UniqueId = $uniqueId")

    val blockedItemList = listOf<BlockedItem>(
        BlockedItem(R.drawable.multiple_face_ic, stringResource(R.string.multiple_face)),
        BlockedItem(R.drawable.ai_generated_ic, stringResource(R.string.ai_generated)),
        BlockedItem(R.drawable.weapon_ic, stringResource(R.string.weapons)),
        BlockedItem(R.drawable.face_ic_red, stringResource(R.string.face_covered)),
        BlockedItem(R.drawable.hidden_face_ic, stringResource(R.string.hidden)),
        BlockedItem(R.drawable.drug_ic, stringResource(R.string.drugs)),)
    val passItemList = listOf<BlockedItem>(
        BlockedItem(R.drawable.original_face_ic, stringResource(R.string.clear_face)),
        BlockedItem(R.drawable.original_face_ic, stringResource(R.string.original_image)),
        BlockedItem(R.drawable.authentic_ic, stringResource(R.string.authentic_expression)),
    )
    val countryList = listOf(
        Country("Afghanistan","أفغانستان","AF"),
        Country("Albania","ألبانيا","AL"),
        Country("Algeria","الجزائر","DZ"),
        Country("Andorra","أندورا","AD"),
        Country("Angola","أنغولا","AO"),
        Country("Antigua and Barbuda","أنتيغوا وبربودا","AG"),
        Country("Argentina","الأرجنتين","AR"),
        Country("Armenia","أرمينيا","AM"),
        Country("Australia","أستراليا","AU"),
        Country("Austria","النمسا","AT"),
        Country("Azerbaijan","أذربيجان","AZ"),
        Country("Bahamas","الباهاما","BS"),
        Country("Bahrain","البحرين","BH"),
        Country("Bangladesh","بنغلاديش","BD"),
        Country("Barbados","بربادوس","BB"),
        Country("Belarus","بيلاروس","BY"),
        Country("Belgium","بلجيكا","BE"),
        Country("Belize","بليز","BZ"),
        Country("Benin","بنين","BJ"),
        Country("Bhutan","بوتان","BT"),
        Country("Bolivia","بوليفيا","BO"),
        Country("Bosnia and Herzegovina","البوسنة والهرسك","BA"),
        Country("Botswana","بوتسوانا","BW"),
        Country("Brazil","البرازيل","BR"),
        Country("Brunei","بروناي","BN"),
        Country("Bulgaria","بلغاريا","BG"),
        Country("Burkina Faso","بوركينا فاسو","BF"),
        Country("Burundi","بوروندي","BI"),
        Country("Cabo Verde","الرأس الأخضر","CV"),
        Country("Cambodia","كمبوديا","KH"),
        Country("Cameroon","الكاميرون","CM"),
        Country("Canada","كندا","CA"),
        Country("Central African Republic","جمهورية أفريقيا الوسطى","CF"),
        Country("Chad","تشاد","TD"),
        Country("Chile","تشيلي","CL"),
        Country("China","الصين","CN"),
        Country("Colombia","كولومبيا","CO"),
        Country("Comoros","جزر القمر","KM"),
        Country("Congo (Republic of the Congo)","جمهورية الكونغو","CG"),
        Country("Congo (Democratic Republic of the Congo)","جمهورية الكونغو الديمقراطية","CD"),
        Country("Costa Rica","كوستاريكا","CR"),
        Country("Croatia","كرواتيا","HR"),
        Country("Cuba","كوبا","CU"),
        Country("Cyprus","قبرص","CY"),
        Country("Czech Republic","التشيك","CZ"),
        Country("Denmark","الدنمارك","DK"),
        Country("Djibouti","جيبوتي","DJ"),
        Country("Dominica","دومينيكا","DM"),
        Country("Dominican Republic","جمهورية الدومينيكان","DO"),
        Country("Ecuador","الإكوادور","EC"),
        Country("Egypt","مصر","EG"),
        Country("El Salvador","السلفادور","SV"),
        Country("Equatorial Guinea","غينيا الاستوائية","GQ"),
        Country("Eritrea","إريتريا","ER"),
        Country("Estonia","إستونيا","EE"),
        Country("Eswatini","إسواتيني","SZ"),
        Country("Ethiopia","إثيوبيا","ET"),
        Country("Fiji","فيجي","FJ"),
        Country("Finland","فنلندا","FI"),
        Country("France","فرنسا","FR"),
        Country("Gabon","الغابون","GA"),
        Country("Gambia","غامبيا","GM"),
        Country("Georgia","جورجيا","GE"),
        Country("Germany","ألمانيا","DE"),
        Country("Ghana","غانا","GH"),
        Country("Greece","اليونان","GR"),
        Country("Grenada","غرينادا","GD"),
        Country("Guatemala","غواتيمالا","GT"),
        Country("Guinea","غينيا","GN"),
        Country("Guinea-Bissau","غينيا بيساو","GW"),
        Country("Guyana","غيانا","GY"),
        Country("Haiti","هايتي","HT"),
        Country("Honduras","هندوراس","HN"),
        Country("Hungary","المجر","HU"),
        Country("Iceland","آيسلندا","IS"),
        Country("India","الهند","IN"),
        Country("Indonesia","إندونيسيا","ID"),
        Country("Iran","إيران","IR"),
        Country("Iraq","العراق","IQ"),
        Country("Ireland","أيرلندا","IE"),
        Country("Israel","إسرائيل","IL"),
        Country("Italy","إيطاليا","IT"),
        Country("Jamaica","جامايكا","JM"),
        Country("Japan","اليابان","JP"),
        Country("Jordan","الأردن","JO"),
        Country("Kazakhstan","كازاخستان","KZ"),
        Country("Kenya","كينيا","KE"),
        Country("Kiribati","كيريباتي","KI"),
        Country("Kuwait","الكويت","KW"),
        Country("Kyrgyzstan","قيرغيزستان","KG"),
        Country("Laos","لاوس","LA"),
        Country("Latvia","لاتفيا","LV"),
        Country("Lebanon","لبنان","LB"),
        Country("Lesotho","ليسوتو","LS"),
        Country("Liberia","ليبيريا","LR"),
        Country("Libya","ليبيا","LY"),
        Country("Liechtenstein","ليختنشتاين","LI"),
        Country("Lithuania","ليتوانيا","LT"),
        Country("Luxembourg","لوكسمبورغ","LU"),
        Country("Madagascar","مدغشقر","MG"),
        Country("Malawi","مالاوي","MW"),
        Country("Malaysia","ماليزيا","MY"),
        Country("Maldives","المالديف","MV"),
        Country("Mali","مالي","ML"),
        Country("Malta","مالطا","MT"),
        Country("Marshall Islands","جزر مارشال","MH"),
        Country("Mauritania","موريتانيا","MR"),
        Country("Mauritius","موريشيوس","MU"),
        Country("Mexico","المكسيك","MX"),
        Country("Micronesia","ميكرونيزيا","FM"),
        Country("Moldova","مولدوفا","MD"),
        Country("Monaco","موناكو","MC"),
        Country("Mongolia","منغوليا","MN"),
        Country("Montenegro","الجبل الأسود","ME"),
        Country("Morocco","المغرب","MA"),
        Country("Mozambique","موزمبيق","MZ"),
        Country("Myanmar","ميانمار","MM"),
        Country("Namibia","ناميبيا","NA"),
        Country("Nauru","ناورو","NR"),
        Country("Nepal","نيبال","NP"),
        Country("Netherlands","هولندا","NL"),
        Country("New Zealand","نيوزيلندا","NZ"),
        Country("Nicaragua","نيكاراغوا","NI"),
        Country("Niger","النيجر","NE"),
        Country("Nigeria","نيجيريا","NG"),
        Country("North Korea","كوريا الشمالية","KP"),
        Country("North Macedonia","مقدونيا الشمالية","MK"),
        Country("Norway","النرويج","NO"),
        Country("Oman","عُمان","OM"),
        Country("Pakistan","باكستان","PK"),
        Country("Palau","بالاو","PW"),
        Country("Panama","بنما","PA"),
        Country("Papua New Guinea","بابوا غينيا الجديدة","PG"),
        Country("Paraguay","باراغواي","PY"),
        Country("Peru","بيرو","PE"),
        Country("Philippines","الفلبين","PH"),
        Country("Poland","بولندا","PL"),
        Country("Portugal","البرتغال","PT"),
        Country("Qatar","قطر","QA"),
        Country("Romania","رومانيا","RO"),
        Country("Russia","روسيا","RU"),
        Country("Rwanda","رواندا","RW"),
        Country("Saint Kitts and Nevis","سانت كيتس ونيفيس","KN"),
        Country("Saint Lucia","سانت لوسيا","LC"),
        Country("Saint Vincent and the Grenadines","سانت فنسنت والغرينادين","VC"),
        Country("Samoa","ساموا","WS"),
        Country("San Marino","سان مارينو","SM"),
        Country("São Tomé and Príncipe","ساو تومي وبرينسيبي","ST"),
        Country("Saudi Arabia","المملكة العربية السعودية","SA"),
        Country("Senegal","السنغال","SN"),
        Country("Serbia","صربيا","RS"),
        Country("Seychelles","سيشل","SC"),
        Country("Sierra Leone","سيراليون","SL"),
        Country("Singapore","سنغافورة","SG"),
        Country("Slovakia","سلوفاكيا","SK"),
        Country("Slovenia","سلوفينيا","SI"),
        Country("Solomon Islands","جزر سليمان","SB"),
        Country("Somalia","الصومال","SO"),
        Country("South Africa","جنوب أفريقيا","ZA"),
        Country("South Korea","كوريا الجنوبية","KR"),
        Country("South Sudan","جنوب السودان","SS"),
        Country("Spain","إسبانيا","ES"),
        Country("Sri Lanka","سريلانكا","LK"),
        Country("Sudan","السودان","SD"),
        Country("Suriname","سورينام","SR"),
        Country("Sweden","السويد","SE"),
        Country("Switzerland","سويسرا","CH"),
        Country("Syria","سوريا","SY"),
        Country("Taiwan","تايوان","TW"),
        Country("Tajikistan","طاجيكستان","TJ"),
        Country("Tanzania","تنزانيا","TZ"),
        Country("Thailand","تايلاند","TH"),
        Country("Timor-Leste","تيمور الشرقية","TL"),
        Country("Togo","توغو","TG"),
        Country("Tonga","تونغا","TO"),
        Country("Trinidad and Tobago","ترينيداد وتوباغو","TT"),
        Country("Tunisia","تونس","TN"),
        Country("Turkey","تركيا","TR"),
        Country("Turkmenistan","تركمانستان","TM"),
        Country("Tuvalu","توفالو","TV"),
        Country("Uganda","أوغندا","UG"),
        Country("Ukraine","أوكرانيا","UA"),
        Country("United Arab Emirates","الإمارات العربية المتحدة","AE"),
        Country("United Kingdom","المملكة المتحدة","GB"),
        Country("United States","الولايات المتحدة","US"),
        Country("Uruguay","أوروغواي","UY"),
        Country("Uzbekistan","أوزبكستان","UZ"),
        Country("Vanuatu","فانواتو","VU"),
        Country("Vatican City","الفاتيكان","VA"),
        Country("Venezuela","فنزويلا","VE"),
        Country("Vietnam","فيتنام","VN"),
        Country("Yemen","اليمن","YE"),
        Country("Zambia","زامبيا","ZM"),
        Country("Zimbabwe","زيمبابوي","ZW")
    )

    val countryIsoMap = mapOf(
        stringResource(R.string.country_afghanistan) to "AF",
        stringResource(R.string.country_albania) to "AL",
        stringResource(R.string.country_algeria) to "DZ",
        stringResource(R.string.country_andorra) to "AD",
        stringResource(R.string.country_angola) to "AO",
        stringResource(R.string.country_antigua_and_barbuda) to "AG",
        stringResource(R.string.country_argentina) to "AR",
        stringResource(R.string.country_armenia) to "AM",
        stringResource(R.string.country_australia) to "AU",
        stringResource(R.string.country_austria) to "AT",
        stringResource(R.string.country_azerbaijan) to "AZ",
        stringResource(R.string.country_bahamas) to "BS",
        stringResource(R.string.country_bahrain) to "BH",
        stringResource(R.string.country_bangladesh) to "BD",
        stringResource(R.string.country_barbados) to "BB",
        stringResource(R.string.country_belarus) to "BY",
        stringResource(R.string.country_belgium) to "BE",
        stringResource(R.string.country_belize) to "BZ",
        stringResource(R.string.country_benin) to "BJ",
        stringResource(R.string.country_bhutan) to "BT",
        stringResource(R.string.country_bolivia) to "BO",
        stringResource(R.string.country_bosnia_and_herzegovina) to "BA",
        stringResource(R.string.country_botswana) to "BW",
        stringResource(R.string.country_brazil) to "BR",
        stringResource(R.string.country_brunei) to "BN",
        stringResource(R.string.country_bulgaria) to "BG",
        stringResource(R.string.country_burkina_faso) to "BF",
        stringResource(R.string.country_burundi) to "BI",
        stringResource(R.string.country_cabo_verde) to "CV",
        stringResource(R.string.country_cambodia) to "KH",
        stringResource(R.string.country_cameroon) to "CM",
        stringResource(R.string.country_canada) to "CA",
        stringResource(R.string.country_central_african_republic) to "CF",
        stringResource(R.string.country_chad) to "TD",
        stringResource(R.string.country_chile) to "CL",
        stringResource(R.string.country_china) to "CN",
        stringResource(R.string.country_colombia) to "CO",
        stringResource(R.string.country_comoros) to "KM",
        stringResource(R.string.country_congo_republic) to "CG",
        stringResource(R.string.country_congo_democratic) to "CD",
        stringResource(R.string.country_costa_rica) to "CR",
        stringResource(R.string.country_croatia) to "HR",
        stringResource(R.string.country_cuba) to "CU",
        stringResource(R.string.country_cyprus) to "CY",
        stringResource(R.string.country_czech_republic) to "CZ",
        stringResource(R.string.country_denmark) to "DK",
        stringResource(R.string.country_djibouti) to "DJ",
        stringResource(R.string.country_dominica) to "DM",
        stringResource(R.string.country_dominican_republic) to "DO",
        stringResource(R.string.country_ecuador) to "EC",
        stringResource(R.string.country_egypt) to "EG",
        stringResource(R.string.country_el_salvador) to "SV",
        stringResource(R.string.country_equatorial_guinea) to "GQ",
        stringResource(R.string.country_eritrea) to "ER",
        stringResource(R.string.country_estonia) to "EE",
        stringResource(R.string.country_eswatini) to "SZ",
        stringResource(R.string.country_ethiopia) to "ET",
        stringResource(R.string.country_fiji) to "FJ",
        stringResource(R.string.country_finland) to "FI",
        stringResource(R.string.country_france) to "FR",
        stringResource(R.string.country_gabon) to "GA",
        stringResource(R.string.country_gambia) to "GM",
        stringResource(R.string.country_georgia) to "GE",
        stringResource(R.string.country_germany) to "DE",
        stringResource(R.string.country_ghana) to "GH",
        stringResource(R.string.country_greece) to "GR",
        stringResource(R.string.country_grenada) to "GD",
        stringResource(R.string.country_guatemala) to "GT",
        stringResource(R.string.country_guinea) to "GN",
        stringResource(R.string.country_guinea_bissau) to "GW",
        stringResource(R.string.country_guyana) to "GY",
        stringResource(R.string.country_haiti) to "HT",
        stringResource(R.string.country_honduras) to "HN",
        stringResource(R.string.country_hungary) to "HU",
        stringResource(R.string.country_iceland) to "IS",
        stringResource(R.string.country_india) to "IN",
        stringResource(R.string.country_indonesia) to "ID",
        stringResource(R.string.country_iran) to "IR",
        stringResource(R.string.country_iraq) to "IQ",
        stringResource(R.string.country_ireland) to "IE",
        stringResource(R.string.country_israel) to "IL",
        stringResource(R.string.country_italy) to "IT",
        stringResource(R.string.country_jamaica) to "JM",
        stringResource(R.string.country_japan) to "JP",
        stringResource(R.string.country_jordan) to "JO",
        stringResource(R.string.country_kazakhstan) to "KZ",
        stringResource(R.string.country_kenya) to "KE",
        stringResource(R.string.country_kiribati) to "KI",
        stringResource(R.string.country_kuwait) to "KW",
        stringResource(R.string.country_kyrgyzstan) to "KG",
        stringResource(R.string.country_laos) to "LA",
        stringResource(R.string.country_latvia) to "LV",
        stringResource(R.string.country_lebanon) to "LB",
        stringResource(R.string.country_lesotho) to "LS",
        stringResource(R.string.country_liberia) to "LR",
        stringResource(R.string.country_libya) to "LY",
        stringResource(R.string.country_liechtenstein) to "LI",
        stringResource(R.string.country_lithuania) to "LT",
        stringResource(R.string.country_luxembourg) to "LU",
        stringResource(R.string.country_madagascar) to "MG",
        stringResource(R.string.country_malawi) to "MW",
        stringResource(R.string.country_malaysia) to "MY",
        stringResource(R.string.country_maldives) to "MV",
        stringResource(R.string.country_mali) to "ML",
        stringResource(R.string.country_malta) to "MT",
        stringResource(R.string.country_marshall_islands) to "MH",
        stringResource(R.string.country_mauritania) to "MR",
        stringResource(R.string.country_mauritius) to "MU",
        stringResource(R.string.country_mexico) to "MX",
        stringResource(R.string.country_micronesia) to "FM",
        stringResource(R.string.country_moldova) to "MD",
        stringResource(R.string.country_monaco) to "MC",
        stringResource(R.string.country_mongolia) to "MN",
        stringResource(R.string.country_montenegro) to "ME",
        stringResource(R.string.country_morocco) to "MA",
        stringResource(R.string.country_mozambique) to "MZ",
        stringResource(R.string.country_myanmar) to "MM",
        stringResource(R.string.country_namibia) to "NA",
        stringResource(R.string.country_nauru) to "NR",
        stringResource(R.string.country_nepal) to "NP",
        stringResource(R.string.country_netherlands) to "NL",
        stringResource(R.string.country_new_zealand) to "NZ",
        stringResource(R.string.country_nicaragua) to "NI",
        stringResource(R.string.country_niger) to "NE",
        stringResource(R.string.country_nigeria) to "NG",
        stringResource(R.string.country_north_korea) to "KP",
        stringResource(R.string.country_north_macedonia) to "MK",
        stringResource(R.string.country_norway) to "NO",
        stringResource(R.string.country_oman) to "OM",
        stringResource(R.string.country_pakistan) to "PK",
        stringResource(R.string.country_palau) to "PW",
        stringResource(R.string.country_panama) to "PA",
        stringResource(R.string.country_papua_new_guinea) to "PG",
        stringResource(R.string.country_paraguay) to "PY",
        stringResource(R.string.country_peru) to "PE",
        stringResource(R.string.country_philippines) to "PH",
        stringResource(R.string.country_poland) to "PL",
        stringResource(R.string.country_portugal) to "PT",
        stringResource(R.string.country_qatar) to "QA",
        stringResource(R.string.country_romania) to "RO",
        stringResource(R.string.country_russia) to "RU",
        stringResource(R.string.country_rwanda) to "RW",
        stringResource(R.string.country_saint_kitts_and_nevis) to "KN",
        stringResource(R.string.country_saint_lucia) to "LC",
        stringResource(R.string.country_saint_vincent_and_the_grenadines) to "VC",
        stringResource(R.string.country_samoa) to "WS",
        stringResource(R.string.country_san_marino) to "SM",
        stringResource(R.string.country_sao_tome_and_principe) to "ST",
        stringResource(R.string.country_saudi_arabia) to "SA",
        stringResource(R.string.country_senegal) to "SN",
        stringResource(R.string.country_serbia) to "RS",
        stringResource(R.string.country_seychelles) to "SC",
        stringResource(R.string.country_sierra_leone) to "SL",
        stringResource(R.string.country_singapore) to "SG",
        stringResource(R.string.country_slovakia) to "SK",
        stringResource(R.string.country_slovenia) to "SI",
        stringResource(R.string.country_solomon_islands) to "SB",
        stringResource(R.string.country_somalia) to "SO",
        stringResource(R.string.country_south_africa) to "ZA",
        stringResource(R.string.country_south_korea) to "KR",
        stringResource(R.string.country_south_sudan) to "SS",
        stringResource(R.string.country_spain) to "ES",
        stringResource(R.string.country_sri_lanka) to "LK",
        stringResource(R.string.country_sudan) to "SD",
        stringResource(R.string.country_suriname) to "SR",
        stringResource(R.string.country_sweden) to "SE",
        stringResource(R.string.country_switzerland) to "CH",
        stringResource(R.string.country_syria) to "SY",
        stringResource(R.string.country_taiwan) to "TW",
        stringResource(R.string.country_tajikistan) to "TJ",
        stringResource(R.string.country_tanzania) to "TZ",
        stringResource(R.string.country_thailand) to "TH",
        stringResource(R.string.country_timor_leste) to "TL",
        stringResource(R.string.country_togo) to "TG",
        stringResource(R.string.country_tonga) to "TO",
        stringResource(R.string.country_trinidad_and_tobago) to "TT",
        stringResource(R.string.country_tunisia) to "TN",
        stringResource(R.string.country_turkey) to "TR",
        stringResource(R.string.country_turkmenistan) to "TM",
        stringResource(R.string.country_tuvalu) to "TV",
        stringResource(R.string.country_uganda) to "UG",
        stringResource(R.string.country_ukraine) to "UA",
        stringResource(R.string.country_united_arab_emirates) to "AE",
        stringResource(R.string.country_united_kingdom) to "GB",
        stringResource(R.string.country_united_states) to "US",
        stringResource(R.string.country_uruguay) to "UY",
        stringResource(R.string.country_uzbekistan) to "UZ",
        stringResource(R.string.country_vanuatu) to "VU",
        stringResource(R.string.country_vatican_city) to "VA",
        stringResource(R.string.country_venezuela) to "VE",
        stringResource(R.string.country_vietnam) to "VN",
        stringResource(R.string.country_yemen) to "YE",
        stringResource(R.string.country_zambia) to "ZM",
        stringResource(R.string.country_zimbabwe) to "ZW"
    )

    UpdateCreateAccoutObserver(
        viewModel = viewModel,
        context = context,
        lifecycleOwner = lifecycleOwner,
        navController = navController as NavHostController)

    BackHandler {
        viewModel.profileImage.value = null
        viewModel.dob.value = ""
        navController.popBackStack()

    }

    DisposableEffect(navController) {
        onDispose {
            // Reset ONLY when leaving screen
            if (SingletonObject.isGoogleLogin) {
                SingletonObject.googleLoginData = GoogleLoginData()
                SingletonObject.isGoogleLogin = false
                Log.d("CreateAccount", "Google data reset on dispose")
            }
        }
    }

    LaunchedEffect(SingletonObject.isGoogleLogin) {
        if (SingletonObject.isGoogleLogin) {
            email = SingletonObject.googleLoginData.email
            firstName = SingletonObject.googleLoginData.firstName
            lastName = SingletonObject.googleLoginData.lastName
        }
    }

    LaunchedEffect(Unit) {

        generateToken(context) { token = it }
    }
    LaunchedEffect(Unit) {
        if (!SingletonObject.isFromEditProfile && !SingletonObject.isCreateFlowInitialized) {
            viewModel.profileImage.value = null
            SingletonObject.isCreateFlowInitialized = true
        }
    }
    LaunchedEffect(SingletonObject.isFromEditProfile) {

        if (SingletonObject.isFromEditProfile) {
            // ✅ EDIT MODE ONLY RESET
            viewModel.cityList1.clear()
            viewModel.dob.value = ""

            viewModel.hitPreviewProfile(
                access_token = SharedPreference.get(context).accessToken
            )
        }
    }

    LaunchedEffect(Unit) {

        getCityObserver(
            context = context as MainActivity,
            viewModel = viewModel,
            lifecycleOwner = lifecycleOwner,
            navController = navController as NavHostController
        )

        if (!SingletonObject.isFromEditProfile) {
            createAccountObserver(
                context = context as MainActivity,
                viewModel = viewModel,
                lifecycleOwner = lifecycleOwner,
                navController = navController as NavHostController
            )
        }
    }


    if (SingletonObject.isFromEditProfile) {
        PreviewProfileObserverOnce(viewModel) { profile ->

            firstName = profile.firstName.orEmpty()
            lastName = profile.lastName.orEmpty()
            email = profile.email.orEmpty()
            mobileNo = profile.phoneNumber.orEmpty()

            viewModel.dob.value = profile.dob.orEmpty()
            gender = profile.gender ?: "male"

            selectedCountry = countryList.find {
                it.name.equals(profile.countryName, ignoreCase = true)
            }

            countryNameEn = selectedCountry?.name ?: profile.countryName.orEmpty()
            countryNameAr = selectedCountry?.nameAr ?: ""
            countryShort = selectedCountry?.iso ?: profile.countryIso.orEmpty()
            selectedCityName = profile.city
            selectedCity = null

            //  selectedCityAr = profile.cityAr

            countryCode = profile.countryCode.orEmpty()
            Log.e("countryCode", "CreateAccountScreen: ${countryCode}", )
            viewModel.profileImage.value = profile.profileImages?.firstOrNull()

            if (!countryShort.isNullOrBlank()) {
                hitCityApi(
                    viewModel = viewModel,
                    context = context,
                    country = countryShort,
                    page = 1,
                    lang =  languageManager.currentLanguage,
                    limit = 50
                )
            }
        }
    }

    DisposableEffect(Unit) {

        onDispose {
            viewModel.clearSocialUniqueId()
        }
    }

    PreviewProfileObserverOnce(viewModel, onData ={viewModel.getPreviewProfileData.value=it})

    UploadObserverOnce(viewModel = viewModel, context = context as MainActivity)

    val imagePicker = SingleImagePicker(
        context = context, viewModel = viewModel,
        navHostController = navController as NavHostController,
        onClick = { isPdf -> })

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        val maxHeight = this.maxHeight
        var showSourceDialog by remember { mutableStateOf(false) }
        val location = latLng


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 20.dp)
                .padding(horizontal = 16.dp)

        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = if (isEditMode) stringResource(R.string.edit_profile) else stringResource(
                        R.string.create_account
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                    modifier=Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(R.drawable.info_icon),
                    contentDescription = "null",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { showBottomSheet = true }
                )
            }

            verticalSpace(15)

            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = viewModel.profileImage.value,
                            placeholder = painterResource(R.drawable.no_dp_icon),
                            error = painterResource(R.drawable.no_dp_icon),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )

                        if (isImageUploading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 3.dp
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier.clickable {
                            viewModel.uploadSource = UploadSource.PROFILE_PIC
                            imagePicker { uri ->
                            }
                        },
                        text = if (isEditMode) stringResource(R.string.change_profile_photo) else stringResource(R.string.upload_profile)+ " "+ stringResource(R.string.required_symbol),
                        color = Color(0xFF9B5DE5),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_semi_bold))
                    )
                }

                verticalSpace(30)

                CustomInputField(
                    heading = stringResource(R.string.first_name)+ " "+ stringResource(R.string.required_symbol),
                    value = firstName,
                    onValueChange = {
                        if (!isEditMode) {
                            firstName = onlyAlphabetsNoInitial(it)
                        }
                    },
                    placeholder = stringResource(R.string.enter_first_name),
                    keyboardType = KeyboardType.Text,
                    enabled = !isEditMode    // 🔥 IMPORTANT
                )


                verticalSpace(20)
                CustomInputField(
                    heading = stringResource(R.string.last_name)+ " "+ stringResource(R.string.required_symbol),
                    value = lastName,
                    onValueChange = {
                        if (!isEditMode) {
                            lastName = onlyAlphabetsNoInitial(it)
                        }
                    },
                    placeholder = stringResource(R.string.enter_last_name),
                    keyboardType = KeyboardType.Text,
                    enabled = !isEditMode
                )

                verticalSpace(20)

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.email_id)+ " "+ stringResource(R.string.required_symbol),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                1.dp,
                                if (email.isNotEmpty() && !isValidEmail) Color.Red
                                else MaterialTheme.colorScheme.outline,  // 🔥 SAME border logic
                                shape = RoundedCornerShape(12.dp)
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = email,
                            onValueChange = { newEmail ->
                                // 🔥 DISABLE EDITING for Google login
                                if (!SingletonObject.isGoogleLogin && !isEditMode) {
                                    email = noInitialSpace(newEmail)
                                }
                            },
                            enabled = !SingletonObject.isGoogleLogin && !isEditMode,
                            placeholder = {
                                Text(
                                    stringResource(R.string.enter_email_id),
                                    style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                                        fontSize = 14.sp
                                    ),
                                    color = Color(0xFF6D6D6D)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,  // 🔥 Gray for disabled
                                errorContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.onBackground
                            ),
                            textStyle = TextStyle(
                                fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // 🔥 SAME error logic (skips for Google)
                    if (email.isNotEmpty() && !isValidEmail && !SingletonObject.isGoogleLogin) {
                        Text(
                            text = stringResource(R.string.please_enter_valid_email),
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular)),
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }
                }
                verticalSpace(20)


                Text(
                    text = stringResource(R.string.mobile_number)+ " "+ stringResource(R.string.required_symbol),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(12.dp))
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {


                        Box {
                            DialogCountryPicker(
                                pickedCountry = {
                                    countryName = it.countryName.toString()
                                    countryCode = it.countryCode
                                },
                                defaultCountryIdentifier = countryCode,
                                countryCodeTextColorAndIconColor = MaterialTheme.colorScheme.onBackground,
                                trailingIconComposable = {
                                    Image(
                                        painter = painterResource(R.drawable.arrow_top_ic),
                                        modifier = Modifier.rotate(180f),
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                                        contentDescription = "Open country picker"
                                    )
                                },
                                isCircleShapeFlag = false,
                                isCountryFlagVisible = false,
                            )

                            if (isEditMode) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }) {
                                            // DO NOTHING – just block clicks
                                        })
                            }
                        }

                    }

                    horizontalSpace(10)
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                1.dp,
                                if (showError) Color.Red else MaterialTheme.colorScheme.outline,   // 🔥 dynamic border color
                                RoundedCornerShape(12.dp)
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        TextField(
                            value = mobileNo,
                            onValueChange = {
                                if (!isEditMode && it.length <= 15 && it.all { c -> c.isDigit() }) {
                                    mobileNo = it
                                }
                            },
                            enabled = !isEditMode,
                            placeholder = {
                                Text(
                                    stringResource(R.string.enter_mobile_number), style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                        fontSize = 14.sp,
                                    ), color = Color(0xFF6D6D6D), textAlign = TextAlign.Center
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                            ),
                            singleLine = true,
                            isError = showError,         // 🔥 tell TextField it's invalid
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.onBackground
                            ),
                            textStyle = TextStyle(
                                fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }


                }
                verticalSpace(3)

                // 🔥 Show error text below
                if (showError) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.please_enter_a_valid_mobile_number_7_to_15_digits),
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.axiforma_regular))
                    )
                }
                verticalSpace(20)


                Text(
                    text = stringResource(R.string.select_country)+ " "+ stringResource(R.string.required_symbol),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                )
                verticalSpace(10)

                val asianEuropeanCountries = countryIsoMap    // from CountryList.kt

                CountryBottomSheetSelector(
                    countryList = countryList,
                    selectedCountry = selectedCountry,
                    onCountrySelected = { country ->
                        selectedCountry = country
                        countryNameEn = country.name
                        countryNameAr = country.nameAr
                        countryShort = country.iso
                        selectedCity = null
                        selectedCityName = null
                        selectedLatitude = null   // ✅ reset
                        selectedLongitude = null  // ✅ reset

                        hitCityApi(
                            viewModel = viewModel,
                            context = context,
                            country = country.iso,
                            page = 1,
                            lang = languageManager.currentLanguage,
                            limit = 50
                        )
                    }                )

                verticalSpace(20)

                Text(
                    text = stringResource(R.string.select_city)+ " "+ stringResource(R.string.required_symbol),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                )
                verticalSpace(10)



                // Replace your existing CityBottomSheetSelector's onCitySelected:
                val coroutineScope = rememberCoroutineScope()

                CityBottomSheetSelector(
                    viewModel = viewModel,
                    navController = navController,
                    country = countryShort,
                    selectedCityName = selectedCityName,
                    searchQuery = searchQuery,
                    selectedCity = selectedCity,
                    onCitySelected = { city ->
                        selectedCity = city
                        selectedCityName = if (languageManager.currentLanguage == "ar") {
                            city?.nameAr?.takeIf { it.isNotBlank() } ?: city?.nameEn ?: ""
                        } else {
                            city?.nameEn ?: ""
                        }


                        coroutineScope.launch(Dispatchers.IO) {
                            val latLngResult = getLatLngFromCityCountry(
                                context = context,
                                city = city?.nameEn ?: "",
                                country = countryNameEn
                            )
                            withContext(Dispatchers.Main) {
                                selectedLatitude = latLngResult?.first
                                selectedLongitude = latLngResult?.second
                                Log.d("Geocoder", "City lat/lng: $selectedLatitude, $selectedLongitude")
                            }
                        }
                    }
                )

                verticalSpace(20)


                Text(
                    text = stringResource(R.string.gender)+ " "+ stringResource(R.string.required_symbol),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                )
                verticalSpace(8)

                //var gender by remember { mutableStateOf("male") }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {

                    CustomRadioButton(
                        selected = gender == "male",
                        label = stringResource(R.string.male),
                        enabled = !isEditMode,
                        onClick = { if (!isEditMode) gender = "male" })

                    CustomRadioButton(
                        selected = gender == "female",
                        label = stringResource(R.string.female),
                        enabled = !isEditMode,
                        onClick = { if (!isEditMode) gender = "female" })
                }

                verticalSpace(20)

                Text(
                    text = stringResource(R.string.date_of_birth)+ " "+ stringResource(R.string.required_symbol),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.axiforma_regular))
                )
                verticalSpace(10)


                CustomDatePicker(
                    selectedDate = viewModel.dob.value, enabled = !isEditMode, onDateSelected = {
                        if (!isEditMode) {
                            viewModel.dob.value = it
                        }
                    }
                )


                verticalSpace(30)
                fun validateInputs(
                    profile: String?,
                    firstName: String?,
                    lastName: String?,
                    email: String?,
                    countryCode: String?,
                    mobile: String?,
                    gender: String?,
                    selectCity: String?,
                    dob: String?,

                    ): Boolean {

                    return when {
                        profile.isNullOrBlank() -> {
                            context.showToast(context.getString(R.string.please_upload_profile_image))
                            false
                        }
                        firstName.isNullOrBlank() -> {
                            context.showToast(context.getString(R.string.please_enter_first_name)); false
                        }

                        lastName.isNullOrBlank() -> {
                            context.showToast(context.getString(R.string.please_enter_last_name)); false
                        }

                        email.isNullOrBlank() -> {
                            context.showToast(context.getString(R.string.please_enter_email)); false
                        }

                        email.isValidEmail().not() -> {
                            context.showToast(context.getString(R.string.please_enter_valid_email)); false
                        }

                        mobile.isNullOrBlank() -> {
                            context.showToast(context.getString(R.string.please_enter_mobile_number)); false
                        }

                        !isValidPhone(mobile) -> {
                            context.showToast(context.getString(R.string.please_enter_valid_phone))
                            false
                        }

                        countryCode.isNullOrBlank() -> {
                            context.showToast(context.getString(R.string.please_enter_country_code))
                            false
                        }

                        dob.isNullOrBlank() -> {
                            context.showToast(context.getString(R.string.please_select_dob))
                            false
                        }


                        calculateAge(dob) < 18 -> {
                            context.showToast(context.getString(R.string.minimum_age_required))
                            false
                        }

                        selectCity.isNullOrBlank() -> {
                            context.showToast(context.getString(R.string.please_select_city))
                            false
                        }

                        gender.isNullOrBlank() -> {
                            context.showToast(context.getString(R.string.please_select_gender))
                            false
                        }




                        else -> true
                    }
                }

                getCountryNameFromCode(countryShort.toString()).subscribe({ name ->
                    countryName = name
                    println("Country: $name")
                }, { error -> println("Error: ${error.message}") })



                AppButton(
                    modifier = Modifier, text = stringResource(R.string.submit), onClick = {
                        viewModel.loginEmail = ""
                        viewModel.loginMobile = ""
                        viewModel.countryCode = ""
                        viewModel.loginEmail = email
                        viewModel.loginMobile = mobileNo
                        viewModel.countryCode = countryCode
                        SingletonObject.loginFromMobile = false
                        SingletonObject.loginFromEmail = false
                        SingletonObject.isComeFromRegister = true

                        val isValid = validateInputs(
                            profile = viewModel.profileImage.value,
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            countryCode = countryCode,
                            mobile = mobileNo,
                            gender = gender,
                            selectCity = selectedCity?.nameEn,
                            dob = viewModel.dob.value,
                        )

                        if (isValid) {
                            if (SingletonObject.isFromEditProfile) {
                                viewModel.hitUpdateProfile(
                                    access_token = SharedPreference.get(context).accessToken,
                                    request = UpdateProfileRequest(
                                        countryCode = countryCode,
                                        dob = viewModel.dob.value,
                                        email = email,
                                        firstName = firstName,
                                        gender = gender,
                                        lastName = lastName,
                                        city = selectedCity?.nameEn ?: "",
                                        cityAr = selectedCity?.nameAr?.takeIf { it.isNotBlank() }
                                            ?: selectedCity?.nameEn
                                            ?: "",
                                        countryIso = countryShort ?: "",
                                        countryName = countryNameEn,
                                        countryNameAr = countryNameAr,
                                        phoneNumber = mobileNo,
                                        profileImages = viewModel.profileImage.value
                                    )
                                )

                            }

                            else {

                                val finalLat = selectedLatitude
                                val finalLng = selectedLongitude

                                if (finalLat == null || finalLng == null) {
                                    Toast.makeText(
                                        context,
                                        "Unable to get location for selected city. Please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@AppButton
                                }

                                viewModel.hitCreateAccount(
                                    token = SharedPreference.get(context).accessToken,
                                    model = CreateAccountRequest(
                                        countryCode = countryCode,
                                        deviceToken = token,
                                        deviceType = "1",
                                        dob = viewModel.dob.value,
                                        email = email,
                                        firstName = firstName,
                                        gender = gender,
                                        lastName = lastName,
                                        city = selectedCity?.nameEn ?: "",
                                        cityAr = selectedCity?.nameAr?.takeIf { it.isNotBlank() }
                                            ?: selectedCity?.nameEn ?: "",
                                        countryIso = countryShort ?: "",
                                        countryName = countryNameEn,
                                        countryNameAr = countryNameAr,
                                        phoneNumber = mobileNo,
                                        profileImages = viewModel.profileImage.value,
                                        uniqueId =uniqueId,
                                        latitude = finalLat.toString(),
                                        longitude = finalLng.toString()
                                    )
                                )
                                if (SingletonObject.isGoogleLogin) {
                                    SingletonObject.googleLoginData = GoogleLoginData()
                                    SingletonObject.isGoogleLogin = false
                                }
                            }
                        }
                    }
                )


                verticalSpace(10, true)

                if (!SingletonObject.isFromEditProfile) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                navController.navigate(Screen.LoginScreen.route) {
                                    popUpTo(0)
                                }
                            }
                            .padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Text(
                            text = stringResource(R.string.already_have_an_account),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = FontFamily(Font(R.font.axiforma_regular))
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = stringResource(R.string.login),
                            fontSize = 12.sp,
                            color = Color(0xFF9B5DE5),
                            fontFamily = FontFamily(Font(R.font.axiforma_medium)),
                            modifier = Modifier
                        )

                    }
                }


                verticalSpace(20)
            }

        }
    }
    if (showBottomSheet) {

        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            sheetGesturesEnabled = false,
            dragHandle = null,
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF14590988))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = stringResource(R.string.photo_guidline),
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                        modifier = Modifier.weight(1f)
                    )

                    Image(
                        painter = painterResource(R.drawable.cross_pruple_ic),
                        contentDescription = "close",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                showBottomSheet = false
                            })
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Row() {
                        Image(
                            painter = painterResource(R.drawable.green_tick_verificatin_ic),
                            contentDescription = "",
                            modifier = Modifier.size(25.dp)
                        )
                        horizontalSpace(10)
                        Text(
                            text = stringResource(R.string.pass),
                            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    verticalSpace(10)


                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(passItemList.size) { index ->
                            val item = passItemList[index]
                            ImageBottomText(
                                img = item.img,
                                text = item.text,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    verticalSpace(10)

                    Row() {
                        Image(
                            painter = painterResource(R.drawable.block_ic_red),
                            contentDescription = "",
                            modifier = Modifier.size(25.dp)
                        )
                        horizontalSpace(10)
                        Text(
                            text = stringResource(R.string.blocked),
                            fontFamily = FontFamily(Font(R.font.axiforma_semi_bold)),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    verticalSpace(10)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(blockedItemList.size) { index ->
                            val item = blockedItemList[index]

                            ImageTextRedBorder(
                                img = item.img,
                                text = item.text,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }

}

fun hitCityApi(viewModel: AuthViewModel, context: Context, country: String, page: Int, limit: Int,lang: String, search: String? = null) {
    viewModel.hitGetCityByCountryCode(
        access = SharedPreference.get(context).accessToken,
        country = country,
        page = page,
        limit = limit,
        lang = lang,
        search = search
    )
}

fun getCityObserver(context: MainActivity, viewModel: AuthViewModel, lifecycleOwner: LifecycleOwner, navController: NavHostController) {
    viewModel.getCityByCountryCode.observe(lifecycleOwner) { state ->
        when (state) {
            is EmpResource.Failure -> {
                state.throwable?.let { err ->
                    ErrorUtil.handlerGeneralError(context, err)
                }
                CustomLoader.hideLoader()
            }

            EmpResource.Loading -> {
                //  CustomLoader.showLoader(context)
            }

            is EmpResource.Success -> {
                CustomLoader.hideLoader()

                if (state.value.success == true) {
                    // 👇 IMPORTANT: update paging + list here
                    viewModel.updateCityPaging(state.value.data)
                    state.value.success = false
                }
            }

            else -> {
                // no-op
            }
        }
    }
}

fun calculateAge(dobString: String): Int {
    try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH) // ✅ was Locale.getDefault()
        val dob = sdf.parse(dobString) ?: return -1

        val dobCal = Calendar.getInstance().apply { time = dob }
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dobCal.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    } catch (e: Exception) {
        return -1
    }
}



data class Country(
    val name: String,
    val nameAr: String,
    val iso: String
)


suspend fun getLatLngFromCityCountry(
    context: Context,
    city: String,
    country: String
): Pair<Double, Double>? {
    return try {
        val geocoder = android.location.Geocoder(context, java.util.Locale.ENGLISH)
        val query = if (city.isNotBlank()) "$city, $country" else country

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ async API
            var result: Pair<Double, Double>? = null
            val latch = java.util.concurrent.CountDownLatch(1)
            geocoder.getFromLocationName(query, 1) { addresses ->
                result = addresses.firstOrNull()?.let {
                    Pair(it.latitude, it.longitude)
                }
                latch.countDown()
            }
            latch.await(3, java.util.concurrent.TimeUnit.SECONDS)
            result
        } else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(query, 1)
            addresses?.firstOrNull()?.let {
                Pair(it.latitude, it.longitude)
            }
        }
    } catch (e: Exception) {
        Log.e("Geocoder", "Failed to get lat/lng: ${e.message}")
        null
    }
}
