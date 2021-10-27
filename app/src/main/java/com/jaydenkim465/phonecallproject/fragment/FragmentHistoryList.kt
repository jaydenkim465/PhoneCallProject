package com.jaydenkim465.phonecallproject.fragment

import android.os.Bundle
import android.provider.CallLog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaydenkim465.phonecallproject.R
import com.jaydenkim465.phonecallproject.adapter.AdapterHistoryList
import com.jaydenkim465.phonecallproject.data.HistoryData
import java.util.*
import kotlin.collections.ArrayList

/**
 * A fragment representing a list of Items.
 */
class FragmentHistoryList(fragment: FragmentActivity) : Fragment() {
	private val mFragment = fragment
	private var columnCount = 1
	private var historyList = ArrayList<HistoryData>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_history_list, container, false)

		val cursor = mFragment.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
		val numberIndex = cursor?.getColumnIndex(CallLog.Calls.NUMBER)
		val typeIndex = cursor?.getColumnIndex(CallLog.Calls.TYPE)
		val dateIndex = cursor?.getColumnIndex(CallLog.Calls.DATE)
		val durationIndex = cursor?.getColumnIndex(CallLog.Calls.DURATION)

		while(cursor?.moveToNext() == true) {
			val date = Date(cursor.getString(dateIndex!!).toLong()).toString()
			val number = cursor.getString(numberIndex!!)
			val type = cursor.getString(typeIndex!!).toInt()
			val duration = cursor.getString(durationIndex!!)
			var typeString = ""
			when(type) {
				CallLog.Calls.OUTGOING_TYPE -> typeString = "OUT"
				CallLog.Calls.INCOMING_TYPE -> typeString = "IN"
				CallLog.Calls.MISSED_TYPE -> typeString = "MISS"
				CallLog.Calls.BLOCKED_TYPE -> typeString = "BLOCK"
				CallLog.Calls.REJECTED_TYPE -> typeString = "REJECT"
			}

			historyList.add(
				HistoryData(
					date,
					number,
					typeString,
					duration
				)
			)
		}
		cursor?.close()

		// Set the adapter
		if (view is RecyclerView) {
			with(view) {
				layoutManager = when {
					columnCount <= 1 -> LinearLayoutManager(context)
					else -> GridLayoutManager(context, columnCount)
				}
				adapter = AdapterHistoryList(historyList)
			}
		}
		return view
	}
}