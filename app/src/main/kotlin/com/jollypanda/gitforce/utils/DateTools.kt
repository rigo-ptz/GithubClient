package com.jollypanda.gitforce.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Yamushev Igor
 * @since  22.06.17
 */
private fun Date.format(format: String): String =
        SimpleDateFormat(format, Locale.getDefault()).format(this)

fun formatToList(date: Date?) = date?.format("d MMMM, HH:mm")


