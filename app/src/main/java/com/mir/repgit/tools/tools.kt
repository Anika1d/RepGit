package com.mir.repgit.tools

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


fun String.formatDate(): String {
    if (this.isNotEmpty()) {
        val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH)
        val r=ZonedDateTime.parse(this)
        return r.format(outputFormatter)
    }
    return ""
}
