package com.kbline.kotlin_module.GalleryUtil.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;

import androidx.annotation.NonNull;

public class CameraSettingPreferences {
    private static final String FLASH_MODE = "squarecamera__flash_mode";

    private static SharedPreferences getCameraSettingPreferences(@NonNull Context context) {
        return context.getSharedPreferences("style_ranker", 0);
    }

    public static void saveCameraFlashMode(@NonNull Context context, @NonNull String cameraFlashMode) {
        SharedPreferences preferences = getCameraSettingPreferences(context);
        if (preferences != null) {
            Editor editor = preferences.edit();
            editor.putString(FLASH_MODE, cameraFlashMode);
            editor.apply();
        }
    }

    public static String getCameraFlashMode(@NonNull final Context context) {
        final SharedPreferences preferences = getCameraSettingPreferences(context);

        if (preferences != null) {
            return preferences.getString(FLASH_MODE, Camera.Parameters.FLASH_MODE_AUTO);
        }

        return Camera.Parameters.FLASH_MODE_AUTO;
    }
}
