package com.example.cameraxexample.view_model

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel : ViewModel() {

    //  使用するカメラ
    private val _cameraLensFacing = MutableStateFlow(CameraSelector.LENS_FACING_BACK)
    val cameraLensFacing = _cameraLensFacing.asStateFlow()  //  読み取り専用StateFlowに変換


    //  写真を撮影するためのUseCase
    lateinit var imageCapture: ImageCapture

    //  撮影した画像のURL
    private val _takenImageUrlList = MutableStateFlow<List<String>>(emptyList())
    val takenImageUrlList = _takenImageUrlList.asStateFlow()

    fun toggleCamera() {
        _cameraLensFacing.value =
            if (_cameraLensFacing.value == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
    }

    fun takePhoto(context: Context) {
        val name = "CameraxImage.jpeg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    println("Success")
                    outputFileResults.savedUri?.let {
                        //  まだ３枚写真がない場合
                        if (_takenImageUrlList.value.count() < 3) {
                            _takenImageUrlList.value += listOf(it.toString())
                        } else {
                            //  3枚写真がある場合は、FIFOで
                            _takenImageUrlList.value = _takenImageUrlList.value.drop(1)
                            _takenImageUrlList.value += listOf(it.toString())
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    println("Failed $exception")
                }

            })
    }

}