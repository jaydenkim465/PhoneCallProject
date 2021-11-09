package com.jaydenkim465.phonecallproject.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.jaydenkim465.phonecallproject.data.ContactData.PlaceholderItem
import com.jaydenkim465.phonecallproject.databinding.ItemContactListBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class AdapterContactList(
	private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<AdapterContactList.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

		return ViewHolder(
			ItemContactListBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = values[position]
		holder.idView.text = item.id
		holder.contentView.text = item.content
	}

	override fun getItemCount(): Int = values.size

	inner class ViewHolder(binding: ItemContactListBinding) :
		RecyclerView.ViewHolder(binding.root) {
		val idView: TextView = binding.itemNumber
		val contentView: TextView = binding.content

		override fun toString(): String {
			return super.toString() + " '" + contentView.text + "'"
		}
	}

}