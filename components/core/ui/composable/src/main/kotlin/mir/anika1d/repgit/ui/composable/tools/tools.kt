package mir.anika1d.repgit.ui.composable.tools

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@SuppressLint("SimpleDateFormat")
fun String.formatDate(): String {
    if (this.isNotEmpty()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH)
            val r = ZonedDateTime.parse(this)
            return r.format(outputFormatter)
        } else {
            val formatter = SimpleDateFormat("dd-MM-yyy", Locale.ENGLISH)
            return formatter.format(this)
        }
    }
    return ""
}

