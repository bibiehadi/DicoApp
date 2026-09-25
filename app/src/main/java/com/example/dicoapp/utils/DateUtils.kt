package com.example.dicoapp.utils

import java.text.SimpleDateFormat
import java.util.Locale


fun formatEventDateRange(beginTime: String?, endTime: String?): String {
    if (beginTime.isNullOrEmpty()) return "-"

    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dateOnlyFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val timeOnlyFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))

        val startDate = inputFormat.parse(beginTime) ?: return beginTime
        val formattedStartDate = dateOnlyFormat.format(startDate)
        val startTime = timeOnlyFormat.format(startDate)

        if (endTime.isNullOrEmpty()) {
            return "$formattedStartDate, $startTime WIB"
        }

        val endDate = inputFormat.parse(endTime) ?: return "$formattedStartDate, $startTime WIB"
        val formattedEndDate = dateOnlyFormat.format(endDate)
        val endTimeStr = timeOnlyFormat.format(endDate)

        if (formattedStartDate == formattedEndDate) {
            "$formattedStartDate, $startTime - $endTimeStr WIB"
        } else {
            "$formattedStartDate, $startTime - $formattedEndDate, $endTimeStr WIB"
        }
    } catch (e: Exception) {
        beginTime
    }
}
