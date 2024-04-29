package com.example.cameraxexample.view_model

import androidx.camera.core.CameraSelector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel : ViewModel() {

    //  内部でのみ変更できるStateFlow
    private val _cameraLensFacing = MutableStateFlow(CameraSelector.LENS_FACING_BACK)
    val cameraLensFacing = _cameraLensFacing.asStateFlow()  //  読み取り専用StateFlowに変換

    fun toggleCamera() {
        _cameraLensFacing.value =
            if (_cameraLensFacing.value == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
    }

}