package com.jaydenkim465.phonecallproject.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jaydenkim465.phonecallproject.fragment.FragmentContactList
import com.jaydenkim465.phonecallproject.fragment.FragmentHistoryList
import com.jaydenkim465.phonecallproject.fragment.FragmentNumberPad
import com.jaydenkim465.phonecallproject.fragment.FragmentRankList


/**
 * ViewPager2를 위한 Adapter Class
 * @constructor
 */
class AdapterViewPager(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

	override fun getItemCount(): Int = 4

	//TODO: 추후 position 에 따른 화면 분기 처리 필요
	override fun createFragment(position: Int): Fragment {
		return when(position) {
			0 -> FragmentNumberPad()
			1 -> FragmentHistoryList()
			2 -> FragmentContactList()
			3 -> FragmentRankList()
			else -> FragmentNumberPad()
		}
	}
}