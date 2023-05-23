package com.example.spender.ui.navigation.screens.balanceScreens

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.spender.domain.remotemodel.LocalTrip
import com.example.spender.domain.remotemodel.toTrip
import com.example.spender.domain.remotemodel.user.Friend
import com.example.spender.domain.remotemodel.user.toFriend
import com.example.spender.ui.navigation.screens.firstScreens.EditTextField
import com.example.spender.ui.navigation.screens.helperfunctions.EmptyListItem
import com.example.spender.ui.theme.GreenMain
import com.example.spender.ui.viewmodel.SpendViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AddSpendingScreen(
    navigator: DestinationsNavigator, spendViewModel: SpendViewModel, trip: LocalTrip
) {
    Scaffold(
        topBar = { AddSpendingScreenTopBar(navigator, trip, spendViewModel) },
    ) {
        AddSpendingScreenContent(it, navigator, spendViewModel, trip)
    }
    LaunchedEffect(key1 = true, block = { SpendAssemble.getInstance().initializeNewAssemble(trip) })
}

@Composable
fun AddSpendingScreenTopBar(
    navigator: DestinationsNavigator,
    trip: LocalTrip,
    spendViewModel: SpendViewModel,
) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = { navigator.popBackStack() }) {
            Icon(
                modifier = Modifier.size(64.dp),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Go back",
                tint = GreenMain
            )
        }
        IconButton(
            onClick = {
                if (SpendAssemble.getInstance().checkIfFieldsAreInitialized()) {
                    spendViewModel.createSpend(
                        trip.toTrip(),
                        SpendAssemble.getInstance().toLocalSpend(trip)
                    )
                    navigator.popBackStack()
                } else {
                    Toast.makeText(
                        context,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                imageVector = Icons.Filled.Check,
                contentDescription = "Add spending",
                tint = GreenMain
            )
        }
    }
}

@Composable
fun AddSpendingScreenContentField(
    text: String,
    editField: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center) {
            AutoResizedText(
                text = text,
                color = GreenMain,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center) {
            editField()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddSpendingScreenContent(
    paddingValues: PaddingValues,
    navigator: DestinationsNavigator,
    spendViewModel: SpendViewModel,
    trip: LocalTrip,
) {
    val context = LocalContext.current
    val splitEqualChecked = remember { mutableStateOf(false) }
    val geoChecked = remember { mutableStateOf(false) }
    val geoPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    splitEqualChecked.value =
        SpendAssemble.getInstance().splitMode != SpendAssemble.Companion.SplitMode.Custom
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        AddSpendingScreenContentField(text = "Spend Name") {
            EditTextField(
                text = SpendAssemble.getInstance().name,
                onTextChanged = {
                    SpendAssemble.getInstance().name = it
                },
                label = { Text(text = "Name") },
                keyboardType = KeyboardType.Text,
                fieldNeedToBeHidden = false
            )
        }
        AddSpendingScreenContentField(text = "Total spend") {
            EditTextField(
                text = SpendAssemble.getInstance().totalSpend,
                onTextChanged = {
                    SpendAssemble.getInstance().totalSpend = if (it.isEmpty()) {
                        it
                    } else {
                        when (it.toDoubleOrNull()) {
                            null -> SpendAssemble.getInstance().totalSpend //old value
                            else -> it   //new value
                        }
                    }
                },
                label = { Text(text = "Amount") },
                keyboardType = KeyboardType.Number,
                fieldNeedToBeHidden = false
            )
        }
        AddSpendingScreenContentField(text = "Split equally") {
            Checkbox(
                checked = splitEqualChecked.value,
                onCheckedChange = {
                    if (SpendAssemble.getInstance().totalSpend.isNotEmpty()) {
                        splitEqualChecked.value = it
                        if (it) {
                            SpendAssemble.getInstance().splitMode =
                                SpendAssemble.Companion.SplitMode.Equal
                        } else {
                            SpendAssemble.getInstance().splitMode =
                                SpendAssemble.Companion.SplitMode.Custom
                        }
                    }
                }
            )
        }
        AddSpendingScreenContentField(text = "Include geo") {
            Checkbox(
                checked = geoChecked.value,
                onCheckedChange = {
                    if (it) {
                        if (!geoPermissionState.status.isGranted) {
                            geoPermissionState.launchPermissionRequest()
                            geoChecked.value = geoPermissionState.status.isGranted
                            if (!geoPermissionState.status.isGranted) {
                                Toast.makeText(
                                    context,
                                    "Please, grant location permission",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            geoChecked.value = false
                            Toast.makeText(
                                context,
                                "Please, grant location permission",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            LocationServices.getFusedLocationProviderClient(context)
                                .lastLocation.addOnSuccessListener { location ->
                                    if (location != null) {
                                        SpendAssemble.getInstance().latitude = location.latitude
                                        SpendAssemble.getInstance().longitude = location.longitude
                                    } else {
                                        geoChecked.value = false
                                        Toast.makeText(
                                            context,
                                            "Turn on location services",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }
                    geoChecked.value = it
                }
            )
        }
        FriendsListSpend(
            friendsLst = buildList {
                trip.members.forEach { friend ->
                    this@buildList.add(friend.toFriend())
                }
            },
        )
    }
}

@Composable
fun FriendsListSpend(
    friendsLst: List<Friend>,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxSize()
    ) {
        if (friendsLst.isEmpty()) {
            item { EmptyListItem(text = "No friends") }
            return@LazyColumn
        }
        items(friendsLst) { friend ->
            FriendsListItemSpend(friend = friend)
        }
    }
}

@Composable
fun FriendsListItemSpend(
    friend: Friend,
) {
    val payment = remember { mutableStateOf("0") }
    payment.value = SpendAssemble.getInstance().getEqualPayment()
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        AddSpendingScreenContentField(text = friend.nickname) {
            EditTextField(
                text = if (payment.value == "0") "" else payment.value,
                onTextChanged = {
                    SpendAssemble.getInstance().resetEqualSplitModeOnUserInput()

                    payment.value = if (it.isEmpty()) {
                        "0"
                    } else {
                        when (it.toDoubleOrNull()) {
                            null -> { //old value
                                payment.value
                            }

                            else -> { //new value
                                it
                            }
                        }
                    }
                    SpendAssemble.getInstance().members[friend.nickname] = payment.value.toDouble()
                },
                label = { Text(text = "Amount") },
                keyboardType = KeyboardType.Number,
                fieldNeedToBeHidden = false
            )
        }
    }
}

