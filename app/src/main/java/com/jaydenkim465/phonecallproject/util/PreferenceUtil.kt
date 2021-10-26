package com.jaydenkim465.phonecallproject.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context:Context) {
	private val prefs:SharedPreferences =
			context.getSharedPreferences("phone_call_settings", Context.MODE_PRIVATE)

//	fun getString(key:String, value:String):String {
//		return prefs.getString(key, value).toString()
//	}
//
//	fun setString(key:String, value:String) {
//		prefs.edit().putString(key, value).apply()
//	}
//
//	fun getBoolean(key:String, value:Boolean):Boolean {
//		return prefs.getBoolean(key, value)
//	}
//
//	fun setBoolean(key:String, value:Boolean) {
//		prefs.edit().putBoolean(key, value).apply()
//	}

	fun getInteger(key:String, value:Int):Int {
		return prefs.getInt(key, value)
	}

	fun setInteger(key:String, value:Int) {
		prefs.edit().putInt(key, value).apply()
	}
}