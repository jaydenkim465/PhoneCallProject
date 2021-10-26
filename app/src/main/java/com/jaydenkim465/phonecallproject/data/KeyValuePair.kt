package com.jaydenkim465.phonecallproject.data

/**
 * Key Value 쌍 목록 관리 목적 Class
 * @property mKey String
 * @property mValue String
 * @constructor
 */
class KeyValuePair(key: String = "", value: Boolean = false) {
	var mKey = key
	var mValue = value
}