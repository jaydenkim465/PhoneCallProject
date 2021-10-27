package com.jaydenkim465.phonecallproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NumberPadViewModel : ViewModel() {
	private var originPhoneNumber = MutableLiveData<String>()
	var displayPhoneNumber = MutableLiveData<String>()

	init {
		originPhoneNumber.value = ""
		displayPhoneNumber.value = ""
	}

	fun append(input:String) {
		val tempString = originPhoneNumber.value + input

		// 특수기호(#, *) 입력을 위한 예외처리
		// toLong 으로 예외처리 하려했으나 시작이 *, #이면 가능하나 마지막이나 중간에 있으면 작동않음
		if(tempString.contains("*") || tempString.contains("#")) {
			originPhoneNumber.value = tempString
			displayPhoneNumber.value = tempString
			return
		}

		autoHyphenCalculate(tempString)
	}

	fun clear() : Boolean {
		displayPhoneNumber.value = ""
		originPhoneNumber.value = ""
		return true
	}

	fun backSpace() {
		/**
		 * 경우의 수 (null인 경우 0으로 만들거나 글자길이와 같이 만들어줌)
		 * 글자길이 경우의 수 : 0, 양수
		 * 커서위치 경우의 수 : 0, 양수
		 * 경우의 수	1		2		3		4		5		6
		 * 글자		0		0		양수		작음		큼		같음
		 * 커서		0		양수		0		큼		작음		같음
		 */
		// String
		// Elvis 연산자
		var tempString = originPhoneNumber.value ?: ""

		// Int
		val tempStringLength = tempString.length

		// 커서가 null(-1) 이고, 글자길이가 0인 경우 커서는 0이 되버리므로, 아래와 같이 조건이 필요함
		// 1, 2번의 경우
		if(tempStringLength < 1) {
			return
		}
		// 5, 6번의 경우
		else {
			tempString = tempString.removeRange(tempStringLength - 1, tempStringLength)
			tempString = tempString.replace("-","")
			autoHyphenCalculate(tempString)
		}
	}

	/**
	 * 전화번호 자동 Hyphen 입력 함수
	 * 대한민국 국내 유선전화 및 무선전화만 가능
	 * TODO: 국제전화 및 미국 전화 체계 도입 필요
	 * TODO: 추후 TextWatcher 로 변경 필요
	 * @param inputString String
	 */
	private fun autoHyphenCalculate(inputString:String) {
		var tempString = inputString
		originPhoneNumber.value = tempString

		// 숫자입력이 3자리 이상일 경우에만 Hyphen 입력 로직 작동
		// 정규식으로 하려하였으나, 함수 설명란에 기입한 내용처럼 경우의 수가 너무 많아 아래와 같이 진행
		if(tempString.length > 3) {
			// 첫글자가 0인 경우
			if(tempString[0] == '0') {
				val remainString: String
				val firstString: String
				val secondString: String
				var thirdString = ""

				// 서울지역번호 첫번째 자리 자르기
				if(tempString[1] == '2') {
					firstString = tempString.substring(0, 2)
					remainString = tempString.substring(2, tempString.length)
				}
				// 그외 지역번호 및 휴대전화 첫번째 자리 자르기
				else {
					firstString = tempString.substring(0, 3)
					remainString = tempString.substring(3, tempString.length)
				}

				// 지역번호이면서 7자리인 경우 두번째, 세번째 자리 자르기
				if(remainString.length == 7 && tempString[1] != '1') {
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

				tempString = if(thirdString.isEmpty()) {
					String.format("%s-%s", firstString, secondString)
				} else {
					String.format("%s-%s-%s", firstString, secondString, thirdString)
				}

			} else if(tempString[0] == '1'){
				if(tempString.length in 5..8) {
					val firstString = tempString.substring(0, 4)
					val secondString = tempString.substring(4, tempString.length)
					tempString = String.format("%s-%s", firstString, secondString)
				}
			}
		}

		displayPhoneNumber.value = tempString
	}
}