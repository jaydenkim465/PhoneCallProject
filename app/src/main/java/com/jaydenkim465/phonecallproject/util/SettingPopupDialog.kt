package com.jaydenkim465.phonecallproject.util

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.jaydenkim465.phonecallproject.R
import com.jaydenkim465.phonecallproject.data.SettingListItem

class SettingPopupDialog(private val listItems:SettingListItem) : DialogFragment(){
	internal  lateinit var listener: SettingPopupDialogListener

	interface SettingPopupDialogListener {
		fun onDialogPositiveClick(dialog:DialogFragment, selectValue:Int)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			var selectedValue = 0
			val builder = AlertDialog.Builder(it)
			var defaultValue = 0

			for(i in listItems.mSubList.indices) {
				if(listItems.mSubList[i].mValue) {
					defaultValue = i
					selectedValue = i
				}
			}

			builder.setTitle(listItems.mTitle)
					// 단일선택 기본 팝업창 생성
					.setSingleChoiceItems(
							R.array.arrayThemeSettingList, defaultValue
					) { _, i ->
						selectedValue = i
					}
					// 확인버튼 이벤트
					.setPositiveButton(R.string.textPopupOk
					) { _, _ ->
						listener.onDialogPositiveClick(this, selectedValue)
						/**
						 * 기존에 아래 주석과 같이 구현하였으나, 시스템 기본값과 변경하려는 테마가 동일하면 setDefaultNightMode 가 화면을 갱신하지 않음
						 * 그렇기에 RecyclerView 의 Adapter 갱신을 하기 위해서 listener 를 interface 를 통하여 최상단으로 빼버림
						 */
//						MainApplication.prefs.setInteger(getString(R.string.keySettingThemeMode), selectedValue)
//						var mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
//						when(selectedValue) {
//							1 -> mode = AppCompatDelegate.MODE_NIGHT_NO
//							2 -> mode = AppCompatDelegate.MODE_NIGHT_YES
//							else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
//						}
//						AppCompatDelegate.setDefaultNightMode(mode)
//						dismiss()
					}
					// 취소버튼 이벤트
					.setNegativeButton(R.string.textPopupCancel
					) { _, _ ->
						dismiss()
					}

			builder.create()
		} ?: throw IllegalStateException("Activity cannot be null")
	}
}