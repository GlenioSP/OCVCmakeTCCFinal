package com.example.gleniosp.ocvcmaketccfinal;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;

import java.util.List;

public class CameraView extends JavaCameraView {

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFlashTorchOn() {
        // As of Android 5.0 use Camera2 API (android.hardware.Camera2)
        Camera.Parameters camParams = mCamera.getParameters();
        camParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(camParams);
    }

    public void setFlashTorchOff() {
        // As of Android 5.0 use Camera2 API (android.hardware.Camera2)
        Camera.Parameters camParams = mCamera.getParameters();
        camParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(camParams);
    }

    public List<Size> getResolutionList() {
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    public void setResolution(Size resolution) {
        disconnectCamera();
        mMaxHeight = resolution.height;
        mMaxWidth = resolution.width;
        connectCamera(getWidth(), getHeight());
    }

    public Size getResolution() {
        return mCamera.getParameters().getPreviewSize();
    }
}
