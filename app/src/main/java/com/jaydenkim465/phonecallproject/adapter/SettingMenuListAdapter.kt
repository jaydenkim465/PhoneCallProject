package com.jaydenkim465.phonecallproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.jaydenkim465.phonecallproject.data.SettingListItem
import com.jaydenkim465.phonecallproject.R
import com.jaydenkim465.phonecallproject.util.SettingPopupDialog

/**
 * 설정 RecyclerView Adapter
 * @property listItems ArrayList<SettingListItem>
 * @property mListItem ArrayList<SettingListItem>
 * @constructor
 */
class SettingMenuListAdapter(private val listItems:ArrayList<SettingListItem>, private val fragmentActivity:FragmentActivity)
	: RecyclerView.Adapter<SettingMenuListAdapter.ViewHolder>(), SettingPopupDialog.SettingPopupDialogListener {
	// Dialog 에서 확인버튼을 눌렀을 때 이벤트 Listener 를 받아서 Parent 에 전달 목적으로 만든 interface 및 Listener
	internal lateinit var listener:SettingAdapterDialogListener
	interface SettingAdapterDialogListener {
		fun onDialogPositiveClick(dialog:DialogFragment, selectValue:Int)
	}
	override fun onDialogPositiveClick(dialog: DialogFragment, selectValue:Int) {
		listener.onDialogPositiveClick(dialog, selectValue)
	}

	private val mListItem = listItems

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
		val titleTextView:TextView = view.findViewById(R.id.TextViewSettingTitle)
		val valueTextView:TextView = view.findViewById(R.id.TextViewSettingValue)
		val backgroundArea:LinearLayout = view.findViewById(R.id.LinearLayoutSettingItemBackArea)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context)
				.inflate(R.layout.item_setting_menu_recyclerview, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.titleTextView.text = mListItem[position].mTitle
		holder.valueTextView.text = mListItem[position].mValue
		// TODO: 추후 설정이 늘어나면 선택시 작동될 분기 설정 필요
		holder.backgroundArea.setOnClickListener {
			val popup = SettingPopupDialog(listItems[position])
			// Google 가이드로는 DialogFragment 내부에서 override 된 attach 함수에서 listener 를 등록하게 되어있음
			// 제대로 작동을 하지 않아서 아래와 같이 구현함
			popup.listener = this
			popup.show(fragmentActivity.supportFragmentManager, "ThemeSetting")
		}
	}

	override fun getItemCount(): Int {
		return mListItem.size
	}
}
