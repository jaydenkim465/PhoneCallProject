package com.jaydenkim465.phonecallproject.presenter

import com.jaydenkim465.phonecallproject.constants.ConstantsNumberPad
import com.jaydenkim465.phonecallproject.viewmodel.ViewModelNumberPad

class PresenterNumberPad(viewModel:ViewModelNumberPad) : ConstantsNumberPad.Presenter{
	val viewModel = viewModel
	override fun insertText(inputString: String) {
	}

	override fun backSpace(inputPosition: Int) {
		TODO("Not yet implemented")
	}
}