package com.jaydenkim465.phonecallproject.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.jaydenkim465.phonecallproject.R
import com.jaydenkim465.phonecallproject.databinding.FragmentNumberPadBinding
import com.jaydenkim465.phonecallproject.viewmodel.NumberPadViewModel
import kotlinx.android.synthetic.main.fragment_number_pad.*
import java.util.regex.Pattern

/**
 * TODO: 추후 아래와 같이 직접 전화를 걸고 받을 수 있는 기능 구현 필요
 * https://developer.android.com/guide/topics/connectivity/telecom/selfManaged?hl=ko
 */
class NumberPad : Fragment() {
	private lateinit var mBinding: FragmentNumberPadBinding
	private val model: NumberPadViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_number_pad, container, false)
		mBinding.lifecycleOwner = this
		mBinding.viewModel = model

		return mBinding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUI()
		setCustomListener()
	}

	private fun setUI() {
		// 전화번호 입력란 부분에 focus 가 가더라도 키보드 표시 되지 않도록 하는 부분
		NumberInputEditText.showSoftInputOnFocus = false
	}

	private fun setCustomListener() {
		//TODO : 커서위치에 따른 삭제 구현 필요(TextWatcher)
		VoiceCallButton.setOnClickListener {
			var originNumber = NumberInputEditText.text.toString()
			if(originNumber.isNotEmpty()) {
				// 전화번호 입력시에 검증하는 부분이 부족하여 우선 전화걸 때 확인하는 부분 구현
				// TODO: 추후 TextWatcher 통해서 전화번호 검증 로직 강화 필요
				val pattern = Pattern.compile("""^([0-9]|[*#])*$""")
				originNumber = originNumber.replace("-","")
				if(pattern.matcher(originNumber).find()) {
					val voiceCall = Intent(Intent.ACTION_CALL, Uri.parse(String.format("tel:%s",originNumber)))
					startActivity(voiceCall)
					return@setOnClickListener
				}
			}
			Toast.makeText(this.context, R.string.textErrorIncorrectPhoneNumber, Toast.LENGTH_SHORT).show()
		}
	}
}