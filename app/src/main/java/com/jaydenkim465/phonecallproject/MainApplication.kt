package com.jaydenkim465.phonecallproject

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.jaydenkim465.phonecallproject.util.PreferenceUtil

class MainApplication : Application() {
	companion object {
		lateinit var prefs:PreferenceUtil
	}

	override fun onCreate() {
		super.onCreate()
		prefs = PreferenceUtil(applicationContext)

		// 아래와 같이 구현하지 않으면 재시작 되어도 테마가 따라가지 않음
		val themeMode = prefs.getInteger(getString(R.string.keySettingThemeMode), 0)
		var mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
		when(themeMode) {
			1 -> mode = AppCompatDelegate.MODE_NIGHT_NO
			2 -> mode = AppCompatDelegate.MODE_NIGHT_YES
			else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
		}
		AppCompatDelegate.setDefaultNightMode(mode)
	}
}