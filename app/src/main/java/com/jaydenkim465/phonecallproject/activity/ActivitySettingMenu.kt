package com.jaydenkim465.phonecallproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaydenkim465.phonecallproject.adapter.AdapterSettingMenuList
import com.jaydenkim465.phonecallproject.data.KeyValuePair
import com.jaydenkim465.phonecallproject.data.SettingListItem
import com.jaydenkim465.phonecallproject.MainApplication
import com.jaydenkim465.phonecallproject.R
import kotlinx.android.synthetic.main.activity_setting_menu_list.*

/**
 * 설정 목록 Activity
 * @property itemList ArrayList<SettingListItem>
 * @property settingAdapter AdapterSettingMenuList
 */
class ActivitySettingMenu : AppCompatActivity(), AdapterSettingMenuList.SettingAdapterDialogListener {
	// 설정 목록들
	private val itemList = ArrayList<SettingListItem>()
	// RecyclerView 에 사용될 Adapter
	private var settingAdapter = AdapterSettingMenuList(itemList, this)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_setting_menu_list)

		setInitialSetting()
	}

	// 세부 설정에서 확인 버튼을 눌렀을 때 이벤트
	// 현재는 테마 변경만 단일
	// TODO: 추후 설정이 늘어나면 수정 필요
	override fun onDialogPositiveClick(dialog: DialogFragment, selectValue: Int) {
		MainApplication.prefs.setInteger(getString(R.string.keySettingThemeMode), selectValue)
		var mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
		when(selectValue) {
			1 -> mode = AppCompatDelegate.MODE_NIGHT_NO
			2 -> mode = AppCompatDelegate.MODE_NIGHT_YES
			else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
		}
		setThemeValue(selectValue)
		// notifyDataSetChanged 를 사용하였으나, Warning 으로 인하여
		// 아래와 같이 ItemChanged 로 변경하였음
		settingAdapter.notifyItemChanged(0)

		AppCompatDelegate.setDefaultNightMode(mode)
		dialog.dismiss()
	}

	/**
	 * 초기 설정 함수
	 */
	private fun setInitialSetting() {
		// SharedPreference 에서 저장된 값을 불러옴
		val themeMode = MainApplication.prefs.getInteger(getString(R.string.keySettingThemeMode), 0)
		setThemeValue(themeMode)

		settingAdapter = AdapterSettingMenuList(itemList, this)
		settingAdapter.listener = this

		setUI()
		setCustomListener()
	}

	/**
	 * UI 관련 설정 함수
	 */
	private fun setUI() {
		RecyclerViewSettingList.layoutManager = LinearLayoutManager(this)
		RecyclerViewSettingList.adapter = settingAdapter
	}

	/**
	 * UI Event Listener 설정 함수
	 */
	private fun setCustomListener() {
		TextViewClose.setOnClickListener {
			finish()
		}
	}

	/**
	 * 테마 설정값 저장
	 * @param input Int
	 */
	private fun setThemeValue(input:Int) {
		var themeMode = input
		val themeList = ArrayList<KeyValuePair>()
		themeList.add(KeyValuePair(getString(R.string.textThemeSettingDefault)))
		themeList.add(KeyValuePair(getString(R.string.textThemeSettingDay)))
		themeList.add(KeyValuePair(getString(R.string.textThemeSettingNight)))

		if(themeMode < themeList.size) {
			themeList[themeMode].mValue = true
		} else {
			themeList[0].mValue = true
			themeMode = 0
		}

		itemList.clear()
		itemList.add(SettingListItem(getString(R.string.textThemeSettingTitle), themeList[themeMode].mKey ,themeList))
	}
}