package com.example.cameraxexample

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.cameraxexample.ui.theme.CameraXExampleTheme
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CameraXExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    CameraScreen(cameraPreviewView = { CameraPreviewView() })
                }
            }
        }
    }
}

@Composable
fun CameraScreen(
    cameraPreviewView: @Composable () -> Unit,
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(7f)
        ) {
            cameraPreviewView()
        }
        Box(
            modifier = Modifier
                .weight(1.4f)
                .align(Alignment.CenterHorizontally)
                .fillMaxSize()
        ) {
            // 撮影ボタン（中央配置）
            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .size(72.dp) // ボタンのサイズ
                    .background(
                        androidx.compose.ui.graphics.Color.Gray,
                        CircleShape
                    )
                    .align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = "撮影",
                    tint = Color.White // アイコンの色
                )
            }

            Box(
                modifier = Modifier
                    .padding(end = 20.dp)
                    .align(Alignment.CenterEnd)
            ) {
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .size(42.dp) // ボタンのサイズ
                        .background(Color.Gray, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "カメラ切り替え",
                        tint = Color.White // アイコンの色
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.6f)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                modifier = Modifier.padding(4.dp, 6.dp, 4.dp, 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.girl),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                Image(
                    painter = painterResource(id = R.drawable.girl),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                Image(
                    painter = painterResource(id = R.drawable.girl),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
fun CameraPreviewView(modifier: Modifier = Modifier) {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val context: Context = LocalContext.current
    val preview = Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    AndroidView(factory = { previewView }, modifier.fillMaxSize())
}

//  Contextの拡張メソッド
//  CameraProviderを取得する
private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CameraXExampleTheme {
        CameraScreen {
            Box(modifier = Modifier
                .background(Color.Red)
                .fillMaxSize())
        }
    }
}