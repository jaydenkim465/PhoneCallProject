package com.jaydenkim465.phonecallproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_number_pad.*
import java.util.regex.Pattern

class NumberPad : Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_number_pad, container, false)
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
		Number1Button.setOnClickListener {
			autoHyphenCalculate("1", false)
		}
		Number2Button.setOnClickListener {
			autoHyphenCalculate("2", false)
		}
		Number3Button.setOnClickListener {
			autoHyphenCalculate("3", false)
		}
		Number4Button.setOnClickListener {
			autoHyphenCalculate("4", false)
		}
		Number5Button.setOnClickListener {
			autoHyphenCalculate("5", false)
		}
		Number6Button.setOnClickListener {
			autoHyphenCalculate("6", false)
		}
		Number7Button.setOnClickListener {
			autoHyphenCalculate("7", false)
		}
		Number8Button.setOnClickListener {
			autoHyphenCalculate("8", false)
		}
		Number9Button.setOnClickListener {
			autoHyphenCalculate("9", false)
		}
		StarButton.setOnClickListener {
			autoHyphenCalculate("*", false)
		}
		Number0Button.setOnClickListener {
			autoHyphenCalculate("0", false)
		}
		SharpButton.setOnClickListener {
			autoHyphenCalculate("#", false)
		}
		//TODO : 커서위치에 따른 삭제 구현 필요(TextWatcher)
		DeleteButton.setOnClickListener {
			var originText = NumberInputEditText.text.toString()
			if (originText.length > 1) {
				originText = originText.substring(0, originText.length - 1)
				autoHyphenCalculate(originText, true)
			} else if(originText.length == 1) {
				NumberInputEditText.setText("")
			}
		}
		// 지우기 버튼 오래 누르면 전체삭제
		DeleteButton.setOnLongClickListener {
			NumberInputEditText.setText("")
			true
		}

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

	/**
	 * 전화번호 자동 Hyphen 입력 함수
	 * 대한민국 국내 유선전화 및 무선전화만 가능
	 * TODO: 국제전화 및 미국 전화 체계 도입 필요
	 * TODO: 추후 TextWatcher 로 변경 필요
	 * @param input String
	 * @param isDelete Boolean
	 */
	private fun autoHyphenCalculate(input:String, isDelete:Boolean) {
		var finalString: String

		if(isDelete) {
			finalString = input
		} else {
			finalString = NumberInputEditText.text.toString()
			finalString += input
		}

		finalString = finalString.replace("-","")

		// 특수기호(#, *) 입력을 위한 예외처리
		// toLong 으로 예외처리 하려했으나 시작이 *, #이면 가능하나 마지막이나 중간에 있으면 작동않음
		if(finalString.contains("*") || finalString.contains("#")) {
			NumberInputEditText.setText(finalString)
			return
		}

		// 숫자입력이 3자리 이상일 경우에만 Hyphen 입력 로직 작동
		// 정규식으로 하려하였으나, 함수 설명란에 기입한 내용처럼 경우의 수가 너무 많아 아래와 같이 진행
		if(finalString.length > 3) {
			// 첫글자가 0인 경우
			if(finalString[0] == '0') {
				val remainString: String
				val firstString: String
				val secondString: String
				var thirdString = ""
				
				// 서울지역번호 첫번째 자리 자르기
				if(finalString[1] == '2') {
					firstString = finalString.substring(0, 2)
					remainString = finalString.substring(2, finalString.length)
				}
				// 그외 지역번호 및 휴대전화 첫번째 자리 자르기
				else {
					firstString = finalString.substring(0, 3)
					remainString = finalString.substring(3, finalString.length)
				}

				// 지역번호이면서 7자리인 경우 두번째, 세번째 자리 자르기
				if(remainString.length == 7 && finalString[1] != '1') {
					secondString = remainString.substring(0, 3)
					thirdString = remainString.substring(3, 7)
				}
				// 휴대전화이면 4자리씩 자르기
				else {
					if(remainString.length > 4) {
						secondString = remainString.substring(0, 4)
						thirdString = remainString.substring(4, remainString.length)
					} else {
						secondString = remainString
					}
				}

				finalString = if(thirdString.isEmpty()) {
					String.format("%s-%s", firstString, secondString)
				} else {
					String.format("%s-%s-%s", firstString, secondString, thirdString)
				}

			} else if(finalString[0] == '1'){
				if(finalString.length in 5..8) {
					val firstString = finalString.substring(0, 4)
					val secondString = finalString.substring(4, finalString.length)
					finalString = String.format("%s-%s", firstString, secondString)
				}
			}
		}

		NumberInputEditText.setText(finalString)
	}
}