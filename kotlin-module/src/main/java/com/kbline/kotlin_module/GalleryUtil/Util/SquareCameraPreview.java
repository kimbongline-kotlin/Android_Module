package com.kbline.kotlin_module.GalleryUtil.Util;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class SquareCameraPreview extends SurfaceView {
    private static final double ASPECT_RATIO = 1.0d;
    private static final int FOCUS_MAX_BOUND = 1000;
    private static final int FOCUS_MIN_BOUND = -1000;
    private static final int FOCUS_SQR_SIZE = 100;
    private static final int INVALID_POINTER_ID = -1;
    public static final String TAG = SquareCameraPreview.class.getSimpleName();
    private static final int ZOOM_DELTA = 1;
    private static final int ZOOM_IN = 1;
    private static final int ZOOM_OUT = 0;
    private int mActivePointerId = -1;
    private Camera mCamera;
    private Area mFocusArea;
    private ArrayList<Area> mFocusAreas;
    private boolean mIsFocus;
    private boolean mIsFocusReady;
    private boolean mIsZoomSupported;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mMaxZoom;
    private ScaleGestureDetector mScaleDetector;
    private int mScaleFactor = 1;




    private class ScaleListener extends SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public boolean onScale(ScaleGestureDetector detector) {
            SquareCameraPreview.this.mScaleFactor = (int) detector.getScaleFactor();
            SquareCameraPreview.this.handleZoom(SquareCameraPreview.this.mCamera.getParameters());
            return true;
        }
    }

    public SquareCameraPreview(Context context) {
        super(context);
        init(context);
    }

    public SquareCameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SquareCameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        this.mFocusArea = new Area(new Rect(), 1000);
        this.mFocusAreas = new ArrayList();
        this.mFocusAreas.add(this.mFocusArea);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        boolean z = true;
        if (getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        if (z) {
            if (((double) width) > ((double) height) * ASPECT_RATIO) {
                width = (int) ((((double) height) * ASPECT_RATIO) + 0.5d);
            } else {
                height = (int) ((((double) width) / ASPECT_RATIO) + 0.5d);
            }
        } else if (((double) height) > ((double) width) * ASPECT_RATIO) {
            height = (int) ((((double) width) * ASPECT_RATIO) + 0.5d);
        } else {
            width = (int) ((((double) height) / ASPECT_RATIO) + 0.5d);
        }
        setMeasuredDimension(width, height);
    }

    public int getViewWidth() {
        return getWidth();
    }

    public int getViewHeight() {
        return getHeight();
    }

    public void setCamera(Camera camera) {
        this.mCamera = camera;
        if (camera != null) {
            Parameters params = camera.getParameters();
            this.mIsZoomSupported = params.isZoomSupported();
            if (this.mIsZoomSupported) {
                this.mMaxZoom = params.getMaxZoom();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.mScaleDetector.onTouchEvent(event);
        int action = event.getAction() & 255;
        if (action == 3) {
            this.mActivePointerId = -1;
        } else if (action != 5) {
            switch (action) {
                case 0:
                    this.mIsFocus = true;
                    this.mLastTouchX = event.getX();
                    this.mLastTouchY = event.getY();
                    this.mActivePointerId = event.getPointerId(0);
                    break;
                case 1:
                    if (this.mIsFocus && this.mIsFocusReady) {
                        handleFocus(this.mCamera.getParameters());
                    }
                    this.mActivePointerId = -1;
                    break;
                default:
                    break;
            }
        } else {
            this.mCamera.cancelAutoFocus();
            this.mIsFocus = false;
        }
        return true;
    }

    private void handleZoom(Parameters params) {
        int zoom = params.getZoom();
        if (this.mScaleFactor == 1) {
            if (zoom < this.mMaxZoom) {
                zoom++;
            }
        } else if (this.mScaleFactor == 0 && zoom > 0) {
            zoom--;
        }
        params.setZoom(zoom);
        this.mCamera.setParameters(params);
    }

    private void handleFocus(Parameters params) {
        if (setFocusBound(this.mLastTouchX, this.mLastTouchY)) {
            List<String> supportedFocusModes = params.getSupportedFocusModes();
            if (supportedFocusModes != null && supportedFocusModes.contains(Parameters.FOCUS_MODE_AUTO)) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mFocusAreas.size());
                stringBuilder.append("");
                Log.d(str, stringBuilder.toString());
                params.setFocusAreas(this.mFocusAreas);
                params.setFocusMode(Parameters.FOCUS_MODE_AUTO);
                this.mCamera.setParameters(params);
                this.mCamera.autoFocus(new AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {


                    }
                });
            }
        }
    }

    public void setIsFocusReady(boolean isFocusReady) {
        this.mIsFocusReady = isFocusReady;
    }

    private boolean setFocusBound(float x, float y) {
        int left = (int) (x - 1112014848);
        int right = (int) (x + 1112014848);
        int top = (int) (y - 1112014848);
        int bottom = (int) (1112014848 + y);
        if (-1000 <= left) {
            if (left <= 1000) {
                if (-1000 <= right) {
                    if (right <= 1000) {
                        if (-1000 <= top) {
                            if (top <= 1000) {
                                if (-1000 <= bottom) {
                                    if (bottom <= 1000) {
                                        this.mFocusArea.rect.set(left, top, right, bottom);
                                        return true;
                                    }
                                }
                                return false;
                            }
                        }
                        return false;
                    }
                }
                return false;
            }
        }
        return false;
    }
}
