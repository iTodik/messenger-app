package ge.itodadze.messengerapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toHoursMinutes(): String {
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(this)
}
