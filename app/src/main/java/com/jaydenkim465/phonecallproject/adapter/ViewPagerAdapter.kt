package com.jaydenkim465.phonecallproject.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jaydenkim465.phonecallproject.fragment.NumberPad


/**
 * ViewPager2를 위한 Adapter Class
 * @constructor
 */
class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
	override fun getItemCount(): Int = 4

	//TODO: 추후 position 에 따른 화면 분기 처리 필요
	override fun createFragment(position: Int): Fragment {
		return NumberPad()
	}
}