package com.jaydenkim465.phonecallproject.constants

interface ConstantsNumberPad {
	interface Presenter {
		fun insertText(inputString : String)
		fun backSpace(inputPosition: Int)
	}
}