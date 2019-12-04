package com.tencent.qbar.sample

import android.graphics.Point
import android.graphics.Rect
import android.hardware.Camera
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tencent.qbar.QbarNative
import com.tencent.qbar.WechatScanner
import kotlinx.android.synthetic.main.activity_main.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.Charset

@Suppress(names = ["DEPRECATION"])
class MainActivity : AppCompatActivity(), SurfaceHolder.Callback, Camera.PreviewCallback, Camera.AutoFocusCallback {

    private lateinit var wechatScanner: WechatScanner
    private lateinit var camera: Camera

    private var isProcessing: Boolean = false
    private var isScanFinish: Boolean = false

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickInit(view: View) {
        wechatScanner = WechatScanner()
        wechatScanner.releaseAssert(view.context)
        wechatScanner.init(view.context)
        wechatScanner.setReader()
        textView.text = wechatScanner.version()
    }

    fun onClickOpen(view: View) {
        surfaceView.holder.addCallback(this)

        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
        camera.setPreviewDisplay(surfaceView.holder)
        camera.setDisplayOrientation(90)

        val parameters: Camera.Parameters = camera.parameters
        parameters.focusMode = Camera.Parameters.FLASH_MODE_AUTO

        camera.parameters = parameters
        camera.setPreviewCallback(this)
        camera.startPreview()
    }

    fun onClickFouce(view: View) {
        camera.autoFocus(this)
    }

    fun onClickReset(view: View) {
        isScanFinish = false
        textView.text = ""
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
    }

    override fun onPreviewFrame(data: ByteArray, camera: Camera) {
        if (isScanFinish || isProcessing)
            return

        isProcessing = true

        val startTimestamp: Long = System.currentTimeMillis()
        val scanResultList: List<QbarNative.QBarResultJNI> = wechatScanner.onPreviewFrame(
            data = data,
            size = Point(camera.parameters.previewSize.width, camera.parameters.previewSize.height),
            crop = Rect(373, 36, 1163, 826),
            rotation = 90
        )
        if (scanResultList.isNotEmpty()) {
            isScanFinish = true
            scanResultList.forEach { qBarResultJNI: QbarNative.QBarResultJNI -> logger.info("LOG:MainActivity:onPreviewFrame typeName={} charset={} data={}", qBarResultJNI.typeName, qBarResultJNI.charset, String(qBarResultJNI.data, Charset.forName(qBarResultJNI.charset))) }
            textView.post { textView.text = scanResultList.first().let { String(it.data, Charset.forName(it.charset)) } }
            logger.info("LOG:MainActivity:onPreviewFrame scan cost: {}ms", System.currentTimeMillis() - startTimestamp)
        }

        isProcessing = false
    }

    override fun onAutoFocus(success: Boolean, camera: Camera?) {
        logger.info("LOG:MainActivity:onAutoFocus success={}", success)
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.release()
        wechatScanner.release()
    }

}
