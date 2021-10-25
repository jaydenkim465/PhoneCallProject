package com.jaydenkim465.phonecallproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	private lateinit var viewPagerAdapter: ViewPagerAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setUI()
		setCustomListener()
	}

	private fun setUI() {
		viewPagerAdapter = ViewPagerAdapter(this)
		MainViewPager2.adapter = viewPagerAdapter
		val tabTitles = listOf(R.string.textNumberPad, R.string.textHistory, R.string.textContracts, R.string.textRank)
		TabLayoutMediator(MainTabLayout, MainViewPager2) {tab, position ->
			tab.text = getString(tabTitles[position])
		}.attach()
	}

	private fun setCustomListener() {
	}
}

class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
	override fun getItemCount(): Int = 4

	override fun createFragment(position: Int): Fragment {
		return NumberPad()
	}
}