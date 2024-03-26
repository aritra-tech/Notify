package com.aritra.notify.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aritra.notify.R
import com.aritra.notify.utils.ReminderDateTimeModel
import com.aritra.notify.utils.formatToLocalDateTime
import com.aritra.notify.utils.Const
import com.aritra.notify.utils.checkDateIsNotOld
import com.aritra.notify.utils.checkTimeIsNotOld
import com.aritra.notify.utils.formatReminderDateTime
import com.aritra.notify.utils.toast
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@Composable
fun TextDialog(
    modifier: Modifier = Modifier,
    isOpened: Boolean,
    onDismissCallback: () -> Unit,
    onConfirmCallback: () -> Unit,
    title: String = stringResource(R.string.warning),
    description: String = stringResource(R.string.are_you_sure_want_to_delete_these_items_it_cannot_be_recovered),
) {
    if (isOpened) {
        AlertDialog(
            onDismissRequest = { onDismissCallback() },
            title = {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = description,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { onConfirmCallback() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.semantics { testTag = Const.CONFIRM_BUTTON }
                ) {
                    Text(
                        text = stringResource(R.string.confirm),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismissCallback() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.semantics { testTag = Const.DISMISS_BUTTON }
                ) {
                    Text(
                        text = stringResource(R.string.dismiss),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DateTimeDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean = false,
    isEdit: Boolean = false,
    onDateTimeUpdated: (LocalDateTime) -> Unit,
    onDismissCallback: () -> Unit,
) {
    var shouldShowDatePicker by remember {
        mutableStateOf(false)
    }
    var shouldShowTimePicker by remember {
        mutableStateOf(false)
    }

    var isEditDateTime by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = isEdit) {
        if (isEdit) {
            isEditDateTime = true
        }
    }
    val context = LocalContext.current
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    if (shouldShowDatePicker || isEditDateTime) {
        DatePickerDialog(onDismissRequest = {
            shouldShowDatePicker = false
            onDismissCallback()
        }, confirmButton = {
            TextButton(onClick = {
                if (datePickerState.selectedDateMillis == null) {
                    context.toast("Please Select Date")
                } else {
                    if (LocalDateTime.of(
                            LocalDate.ofInstant(
                                Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
                                ZoneId.systemDefault()
                            ),
                            LocalTime.now()
                        ).checkDateIsNotOld()
                    ) {
                        shouldShowDatePicker = false
                        shouldShowTimePicker = true
                        if (isEdit) {
                            isEditDateTime = false
                        }
                    } else {
                        context.toast("Can not choose old Date")
                    }
                }
            }) {
                Text(text = "Confirm")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }

    if (shouldShowTimePicker) {
        DatePickerDialog(onDismissRequest = {
            shouldShowTimePicker = false
            shouldShowDatePicker = true
        }, confirmButton = {
            TextButton(onClick = {
                val dateTime = LocalDateTime.of(
                    LocalDate.ofInstant(
                        Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
                        ZoneId.systemDefault()
                    ),
                    LocalTime.of(timePickerState.hour, timePickerState.minute)
                )
                if (dateTime.checkTimeIsNotOld()) {
                    onDateTimeUpdated(
                        dateTime
                    )
                    shouldShowTimePicker = false
                } else {
                    context.toast("Can not choose old Time")
                }
            }) {
                Text(text = "Confirm")
            }
        }) {
            TimePicker(
                state = timePickerState,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 16.dp)
            )
        }
    }
    if (isOpen && !isEdit) {
        BasicAlertDialog(onDismissRequest = onDismissCallback) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp),
                modifier = modifier,
                tonalElevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 24.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.set_reminder),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    FlowRow(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(bottom = 24.dp),
                        maxItemsInEachRow = 1
                    ) {
                        ReminderDateTimeModel.entries.forEach {
                            AssistChip(leadingIcon = {
                                Icon(imageVector = Icons.Default.AccessTime, contentDescription = "")
                            }, onClick = {
                                if (it == ReminderDateTimeModel.CUSTOM) {
                                    shouldShowDatePicker = true
                                } else {
                                    onDateTimeUpdated(it.formatToLocalDateTime())
                                }
                            }, label = {
                                Text(
                                    text = if (it != ReminderDateTimeModel.CUSTOM) {
                                        it.formatToLocalDateTime()
                                            .formatReminderDateTime()
                                    } else {
                                        "Custom"
                                    },
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            })
                        }
                    }
                }
            }
        }
    }
}
