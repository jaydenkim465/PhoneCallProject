package com.jaydenkim465.phonecallproject.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.jaydenkim465.phonecallproject.R
import com.jaydenkim465.phonecallproject.adapter.AdapterViewPager
import kotlinx.android.synthetic.main.activity_main.*

class ActivityMain : AppCompatActivity() {
	private lateinit var adapterViewPager: AdapterViewPager

	private val permissionRequestCode = 1001
	// Permission 요청할 목록
	private val permissionList = arrayOf(
		Manifest.permission.CALL_PHONE,
		Manifest.permission.READ_CALL_LOG
	)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setInitialSetting()
	}

	/**
	 * 초기 설정 함수
	 */
	private fun setInitialSetting() {
		var errorIndex = -1

		// Permission 허용 목록들 확인
		for(i:Int in permissionList.indices) {
			if(checkSelfPermission(permissionList[i]) == PackageManager.PERMISSION_DENIED) {
				errorIndex = i
				break
			}
		}

		// 발생한 오류가 없다면
		if(errorIndex < 0) {
			setUI()
			setCustomListener()
		}
		// 하나의 Denied 가 있다면 전부다 허용 요청
		else {
			requestPermissions(permissionList, permissionRequestCode)
		}
	}

	/**
	 * UI 관련 설정 함수
	 */
	private fun setUI() {
		adapterViewPager = AdapterViewPager(this)
		MainViewPager2.adapter = adapterViewPager
		val tabTitles = listOf(
			R.string.textNumberPad,
			R.string.textHistory,
			R.string.textContracts,
			R.string.textRank
		)
		TabLayoutMediator(MainTabLayout, MainViewPager2) {tab, position ->
			tab.text = getString(tabTitles[position])
		}.attach()
	}

	/**
	 * UI Event Listener 설정 함수
	 */
	private fun setCustomListener() {
		TextViewSetting.setOnClickListener {
			startActivity(Intent(this, ActivitySettingMenu::class.java))
		}
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		when(requestCode) {
			permissionRequestCode -> {
				if(grantResults.isEmpty()) {
					finish()
				}
				if(permissionList.size == grantResults.size) {
					for (i: Int in grantResults.indices) {
						// 허용이 하나라도 되어 있지 않다면
						if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
							// 단순거부 일경우에는 다시 요청
							if (shouldShowRequestPermissionRationale(permissionList[i])) {
								requestPermissions(permissionList, permissionRequestCode)
								return
							}
							// 다시보지 않기를 한 경우에는 팝업 표시하여 설정 화면 이동
							else {
								showDialogToGetPermission()
								return
							}
						}
					}
				} else {
					finish()
				}
			}
		}

		setUI()
		setCustomListener()
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}

	/**
	 * 권한 허용 안내를 위한 팝업 함수
	 */
	private fun showDialogToGetPermission() {
		val builder = AlertDialog.Builder(this)
		builder.setTitle(getString(R.string.textErrorPermissionDeniedTitle))
			.setMessage(getString(R.string.textErrorPermissionDeniedMessage))

		builder.setPositiveButton(getString(R.string.textPopupYes)) { _, _ ->
			val intent = Intent(
				Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
				Uri.fromParts("package", packageName, null))
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			startActivity(intent)
			finish()
		}
		builder.setNegativeButton(getString(R.string.textPopupNo)) { _, _ ->
			finish()
		}
		builder.create().show()
	}
}