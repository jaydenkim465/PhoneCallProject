package com.jaydenkim465.phonecallproject.data

/**
 * 설정 리스트를 위한 Class
 * 항목명, 설정값, 하위 설정 목록이 있음
 * @property mTitle String
 * @property mValue String
 * @property mSubList ArrayList<KeyValuePair>
 * @constructor
 */
class SettingListItem(
		title: String = "",
		value: String = "",
		subList:ArrayList<KeyValuePair> = ArrayList()
) {
	var mTitle = title
	var mValue = value
	var mSubList = subList
}