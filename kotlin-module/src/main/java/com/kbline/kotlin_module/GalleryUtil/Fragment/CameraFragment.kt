package com.kbline.kotlin_module.GalleryUtil.Fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.Camera
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment

import bsc2086.kotlin_module.R
import com.kbline.kotlin_module.GalleryUtil.Util.*
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CameraFragment : Fragment(), SurfaceHolder.Callback, Camera.PictureCallback {
    
    private var mCamera: Camera? = null
    private var mCameraID: Int = 0
    private var mFlashMode: String? = null
    private var mImageParameters: ImageParameters? = null
    private var mIsSafeToTakePhoto = false
    protected var mIsVisibleToUser: Boolean = false
    private var mOrientationListener: CameraOrientationListener? = null
    lateinit var mPreviewView: SquareCameraPreview
    private var mSurfaceHolder: SurfaceHolder? = null

    private val frontCameraID: Int
        get() = if (activity!!.packageManager.hasSystemFeature("android.hardware.camera.front")) {
            1
        } else backCameraID

    private val backCameraID: Int
        get() = 0

    private val photoRotation: Int
        get() {
            val orientation = mOrientationListener!!.rememberedNormalOrientation
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(mCameraID, info)
            return if (info.facing == 1) {
                (info.orientation - orientation + 360) % 360
            } else (info.orientation + orientation) % 360
        }



    private class CameraOrientationListener(context: Context) : OrientationEventListener(context, 3) {
        private var mCurrentNormalizedOrientation: Int = 0
        private var mRememberedNormalOrientation: Int = 0

        val rememberedNormalOrientation: Int
            get() {
                rememberOrientation()
                return mRememberedNormalOrientation
            }

        override fun onOrientationChanged(orientation: Int) {
            if (orientation != -1) {
                mCurrentNormalizedOrientation = normalize(orientation)
            }
        }

        private fun normalize(degrees: Int): Int {
            if (degrees <= 315) {
                if (degrees > 45) {
                    if (degrees > 45 && degrees <= 135) {
                        return 90
                    }
                    if (degrees > 135 && degrees <= 225) {
                        return 180
                    }
                    if (degrees > 225 && degrees <= 315) {
                        return 270
                    }
                    throw RuntimeException("The physics as we know them are no more. Watch out for anomalies.")
                }
            }
            return 0
        }

        fun rememberOrientation() {
            mRememberedNormalOrientation = mCurrentNormalizedOrientation
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOrientationListener = CameraOrientationListener(context!!)
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mIsVisibleToUser = isVisibleToUser
        if (!isResumed) {
            return
        }
        if (mIsVisibleToUser) {
            onVisible()
        } else {
            onInVisible()
        }
    }

    fun onVisible() {
        if (mCamera != null) {
            stopCameraPreview()
            mCamera!!.release()
            mCamera = null
            restartPreview()
            return
        }
        startCameraPreview()
    }

    fun onInVisible() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       
        if (savedInstanceState == null) {
            mCameraID = backCameraID
            mFlashMode = CameraSettingPreferences.getCameraFlashMode(activity!!)
            mImageParameters = ImageParameters()
            return
        }
        mCameraID = savedInstanceState.getInt("camera_id")
        mFlashMode = savedInstanceState.getString("flash_mode")

        mImageParameters = savedInstanceState.getParcelable<Parcelable>("image_info") as ImageParameters?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mOrientationListener!!.enable()
        mPreviewView = camera_preview_view as SquareCameraPreview
        mPreviewView!!.holder.addCallback(this)
        val imageParameters = mImageParameters
        var z = true
        if (resources.configuration.orientation != 1) {
            z = false
        }
        imageParameters!!.mIsPortrait = z
        if (savedInstanceState == null) {
            mPreviewView!!.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mImageParameters!!.mPreviewWidth = mPreviewView!!.width
                    mImageParameters!!.mPreviewHeight = mPreviewView!!.height
                    val `access$000` = mImageParameters
                    val `access$0002` = mImageParameters
                    val calculateCoverWidthHeight = mImageParameters!!.calculateCoverWidthHeight()
                    `access$0002`!!.mCoverHeight = calculateCoverWidthHeight
                    `access$000`!!.mCoverWidth = calculateCoverWidthHeight
                    if (Build.VERSION.SDK_INT >= 16) {
                        mPreviewView!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    } else {
                        mPreviewView!!.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    }
                }
            })
        } else {
            mImageParameters!!.isPortrait
        }

        rotationIcon.setOnClickListener {
            if (mCameraID == 1) {
                mCameraID = backCameraID
            } else {
                mCameraID = frontCameraID
            }
            restartPreview()
        }


        captureIcon.setOnClickListener {
            takePicture()
        }

        setupFlashMode()
    }

    private fun setupFlashMode() {
        if (view != null) {
            if (!Camera.Parameters.FLASH_MODE_AUTO.equals(mFlashMode!!, ignoreCase = true)) {
                if (!Camera.Parameters.FLASH_MODE_ON.equals(mFlashMode!!, ignoreCase = true)) {
                    Camera.Parameters.FLASH_MODE_OFF.equals(mFlashMode!!, ignoreCase = true)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("camera_id", mCameraID)
        outState.putString("flash_mode", mFlashMode)
        outState.putParcelable("image_info", mImageParameters)
        super.onSaveInstanceState(outState)
    }

    private fun getCamera(cameraID: Int) {
        try {
            mCamera = Camera.open(cameraID)
            mPreviewView!!.setCamera(mCamera)
        } catch (e: Exception) {
            val str = TAG
            val stringBuilder = StringBuilder()
            stringBuilder.append("Can't open camera with id ")
            stringBuilder.append(cameraID)
            Log.d(str, stringBuilder.toString())
            e.printStackTrace()
        }

    }

    private fun restartPreview() {
        if (mCamera != null) {
            stopCameraPreview()
            mCamera!!.release()
            mCamera = null
        }
        getCamera(mCameraID)
        startCameraPreview()
    }

    private fun startCameraPreview() {
        determineDisplayOrientation()
        setupCamera()
        try {
            mCamera!!.setPreviewDisplay(mSurfaceHolder)
            mCamera!!.startPreview()
            setSafeToTakePhoto(true)
            setCameraFocusReady(true)
        } catch (e: IOException) {
            val str = TAG
            val stringBuilder = StringBuilder()
            stringBuilder.append("Can't start camera preview due to IOException ")
            stringBuilder.append(e)
            Log.d(str, stringBuilder.toString())
            e.printStackTrace()
        }

    }

    private fun stopCameraPreview() {
        setSafeToTakePhoto(false)
        setCameraFocusReady(false)
        mCamera!!.stopPreview()
        mPreviewView!!.setCamera(null)
    }

    private fun setSafeToTakePhoto(isSafeToTakePhoto: Boolean) {
        mIsSafeToTakePhoto = isSafeToTakePhoto
    }

    private fun setCameraFocusReady(isFocusReady: Boolean) {
        if (mPreviewView != null) {
            mPreviewView!!.setIsFocusReady(isFocusReady)
        }
    }

    private fun determineDisplayOrientation() {
        val displayOrientation: Int
        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(mCameraID, cameraInfo)
        var degrees = 0
        when (activity!!.windowManager.defaultDisplay.rotation) {
            0 -> degrees = 0
            1 -> degrees = 90
            2 -> degrees = 180
            3 -> degrees = 270
            else -> {
            }
        }
        if (cameraInfo.facing == 1) {
            displayOrientation = (360 - (cameraInfo.orientation + degrees) % 360) % 360
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360
        }
        mImageParameters!!.mDisplayOrientation = displayOrientation
        mImageParameters!!.mLayoutOrientation = degrees
        mCamera!!.setDisplayOrientation(mImageParameters!!.mDisplayOrientation)
    }

    private fun setupCamera() {
        val parameters = mCamera!!.parameters
        val bestPreviewSize = determineBestPreviewSize(parameters)
        val bestPictureSize = determineBestPictureSize(parameters)
        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height)
        parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height)
        if (parameters.supportedFocusModes.contains("continuous-picture")) {
            parameters.focusMode = "continuous-picture"
        }
        val flashModes = parameters.supportedFlashModes
        if (flashModes != null && flashModes.contains(mFlashMode)) {
            parameters.flashMode = mFlashMode
        }
        mCamera!!.parameters = parameters
    }

    private fun determineBestPreviewSize(parameters: Camera.Parameters): Camera.Size {
        return determineBestSize(parameters.supportedPreviewSizes, PREVIEW_SIZE_MAX_WIDTH)
    }

    private fun determineBestPictureSize(parameters: Camera.Parameters): Camera.Size {
        return determineBestSize(parameters.supportedPictureSizes, PICTURE_SIZE_MAX_WIDTH)
    }

    private fun determineBestSize(sizes: List<Camera.Size>, widthThreshold: Int): Camera.Size {
        val numOfSizes = sizes.size
        var bestSize: Camera.Size? = null
        var i = 0
        while (true) {
            var isBetterSize = true
            if (i >= numOfSizes) {
                break
            }
            val size = sizes[i]
            val isDesireRatio = size.width / 1 == size.height / 1
            if (bestSize != null) {
                if (size.width <= bestSize.width) {
                    isBetterSize = false
                }
            }
            if (isDesireRatio && isBetterSize) {
                bestSize = size
            }
            i++
        }
        if (bestSize != null) {
            return bestSize
        }
        Log.d(TAG, "cannot find the best camera size")
        return sizes[sizes.size - 1]
    }

    private fun takePicture() {
        if (mIsSafeToTakePhoto) {
            setSafeToTakePhoto(false)
            mOrientationListener!!.rememberOrientation()
            mCamera!!.takePicture(null, null, null, this)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        mOrientationListener!!.disable()
        if (mCamera != null) {
            stopCameraPreview()
            mCamera!!.release()
            mCamera = null
        }
        CameraSettingPreferences.saveCameraFlashMode(activity!!, mFlashMode!!)
        super.onStop()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mSurfaceHolder = holder
        getCamera(mCameraID)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == -1) {
            if (requestCode != 1) {
                super.onActivityResult(requestCode, resultCode, data)
            } else {
                val imageUri = data!!.data
            }
        }
    }

    override fun onPictureTaken(data: ByteArray, camera: Camera) {
        rotatePicture(photoRotation, data)
        setSafeToTakePhoto(true)
    }

    private fun rotatePicture(rotation: Int, data: ByteArray) {
        var bitmap = ImageUtility.decodeSampledBitmapFromByte(activity!!, data)
        if (rotation != 0) {
            val oldBitmap = bitmap
            val matrix = Matrix()
            matrix.postRotate(rotation.toFloat())
            bitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.width, oldBitmap.height, matrix, false)
            oldBitmap.recycle()
        }
        bitmapConvertToFile(bitmap)
    }

    fun bitmapConvertToFile(bitmap: Bitmap): File? {
        var fileOutputStream: FileOutputStream? = null
        var bitmapFile: File? = null
        try {
            val file = File(Environment.getExternalStoragePublicDirectory("kb_crop_camera"), "")
            if (!file.exists()) {
                file.mkdir()
            }
            val stringBuilder = StringBuilder()
            stringBuilder.append("IMG_")
            stringBuilder.append(SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().time))
            stringBuilder.append(".jpg")
            bitmapFile = File(file, stringBuilder.toString())
            fileOutputStream = FileOutputStream(bitmapFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)

            MediaScannerConnection.scanFile(context, arrayOf(bitmapFile.absolutePath), null, object : MediaScannerConnection.OnScanCompletedListener {

                override fun onScanCompleted(path: String, uri: Uri) {
                    activity!!.runOnUiThread {
                        val tempData = ArrayList<String>()
                        tempData.add(path)
                        var intent = Intent()
                        intent.putExtra(CameraValue.IMAGES,tempData)
                        activity!!.setResult(Activity.RESULT_OK,intent)
                        activity!!.finish()

                    }
                }
            })
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush()
                    fileOutputStream.close()
                } catch (e: Exception) {
                }

            }
        } catch (e2: Exception) {
            e2.printStackTrace()
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush()
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        } catch (th: Throwable) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush()
                    fileOutputStream.close()
                } catch (e3: Exception) {
                }

            }
        }

        return bitmapFile
    }

    companion object {
        val CAMERA_FLASH_KEY = "flash_mode"
        val CAMERA_ID_KEY = "camera_id"
        val IMAGE_INFO = "image_info"
        private val PICTURE_SIZE_MAX_WIDTH = 1280
        private val PREVIEW_SIZE_MAX_WIDTH = 640
        val TAG = CameraFragment::class.java.simpleName

        fun newInstance(): Fragment {
            return CameraFragment()
        }
    }
}
