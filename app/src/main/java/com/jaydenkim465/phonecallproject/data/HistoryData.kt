package com.jaydenkim465.phonecallproject.data

import android.provider.CallLog
import java.text.SimpleDateFormat
import java.util.*

class HistoryData (date: Date, val number: String, val type: Int, val duration:Long) {
	var sType = ""
	var sDate = ""
	var sTime = ""
	var sDuration = ""

	init {
		val dateDF = SimpleDateFormat("yyyy-MM-dd")
		val timeDF = SimpleDateFormat("hh:mm")
		sDate = dateDF.format(date)
		sTime = timeDF.format(date)

		val lHours = duration / 3600
		val lMinutes = (duration % 3600) / 60
		val lSeconds = duration % 60

		sDuration = String.format("%d:%d:%d", lHours, lMinutes, lSeconds)

		when(type) {
			CallLog.Calls.OUTGOING_TYPE -> sType = "OUT"
			CallLog.Calls.INCOMING_TYPE -> sType = "IN"
			CallLog.Calls.MISSED_TYPE -> sType = "MISS"
			CallLog.Calls.BLOCKED_TYPE -> sType = "BLOCK"
			CallLog.Calls.REJECTED_TYPE -> sType = "REJECT"
			else -> sType = String.format("TYPE : %d", type)
		}
	}
}