package com.jaydenkim465.phonecallproject.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.jaydenkim465.phonecallproject.data.HistoryData
import com.jaydenkim465.phonecallproject.databinding.ItemHistoryListBinding

class AdapterHistoryList(
	private val values: List<HistoryData>
) : RecyclerView.Adapter<AdapterHistoryList.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			ItemHistoryListBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = values[position]
		holder.dateView.text = item.sDate
		holder.timeView.text = item.sTime
		holder.numberView.text = item.number
		holder.typeView.text = item.sType
		holder.durationView.text = item.sDuration
	}

	override fun getItemCount(): Int = values.size

	inner class ViewHolder(binding: ItemHistoryListBinding) : RecyclerView.ViewHolder(binding.root) {
		val mainLayer: ConstraintLayout = binding.listItemMainLayer
		val dateView: TextView = binding.date
		val numberView: TextView = binding.number
		val typeView: TextView = binding.type
		val durationView: TextView = binding.duration
		val timeView: TextView = binding.time
	}

}