package com.kbline.kotlin_module.GalleryUtil.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bsc2086.kotlin_module.R;

public class ImageUtility {
    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 80, out);
        return Base64.encodeToString(out.toByteArray(), 0);
    }

    public static byte[] convertBitmapStringToByteArray(String bitmapByteString) {
        return Base64.decode(bitmapByteString, 0);
    }

    public static Bitmap rotatePicture(Context context, int rotation, byte[] data) {
        Bitmap bitmap = decodeSampledBitmapFromByte(context, data);
        if (rotation == 0) {
            return bitmap;
        }
        Bitmap oldBitmap = bitmap;
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotation);
        bitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false);
        oldBitmap.recycle();
        return bitmap;
    }

    public static Uri savePicture(Context context, Bitmap bitmap) {
        int cropHeight;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            cropHeight = bitmap.getWidth();
        } else {
            cropHeight = bitmap.getHeight();
        }
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, cropHeight, cropHeight, 2);
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.getString(R.string.app_name));
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mediaStorageDir.getPath());
        stringBuilder.append(File.separator);
        stringBuilder.append("IMG_");
        stringBuilder.append(timeStamp);
        stringBuilder.append(".jpg");
        File mediaFile = new File(stringBuilder.toString());
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 100, out);
            FileOutputStream stream = new FileOutputStream(mediaFile);
            stream.write(out.toByteArray());
            stream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Intent mediaScannerIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        Uri fileContentUri = Uri.fromFile(mediaFile);
        mediaScannerIntent.setData(fileContentUri);
        context.sendBroadcast(mediaScannerIntent);
        return fileContentUri;
    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        options.inMutable = true;
        options.inBitmap = BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inScaled = true;
        options.inDensity = options.outWidth;
        options.inTargetDensity = options.inSampleSize * reqWidth;
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap decodeSampledBitmapFromByte(Context context, byte[] bitmapBytes) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int reqWidth, reqHeight;
        Point point = new Point();

        if (VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(point);
            reqWidth = point.x;
            reqHeight = point.y;
        } else {
            reqWidth = display.getWidth();
            reqHeight = display.getHeight();
        }


        final Options options = new Options();
        options.inJustDecodeBounds = true;
        options.inMutable = true;
        options.inBitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Load & resize the image to be 1/inSampleSize dimensions
        // Use when you do not want to scale the image with a inSampleSize that is a power of 2
        options.inScaled = true;
        options.inDensity = options.outWidth;
        options.inTargetDensity = reqWidth * options.inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false; // If set to true, the decoder will return null (no bitmap), but the out... fields will still be set, allowing the caller to query the bitmap without having to allocate the memory for its pixels.
        options.inPurgeable = true;         // Tell to gc that whether it needs free memory, the Bitmap can be cleared
        options.inInputShareable = true;    // Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future

        return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int initialInSampleSize = computeInitialSampleSize(options, reqWidth, reqHeight);
        if (initialInSampleSize > 8) {
            return ((initialInSampleSize + 7) / 8) * 8;
        }
        int roundedInSampleSize = 1;
        while (roundedInSampleSize < initialInSampleSize) {
            roundedInSampleSize <<= 1;
        }
        return roundedInSampleSize;
    }

    private static int computeInitialSampleSize(Options options, int reqWidth, int reqHeight) {
        int upperBound;
        Options options2 = options;
        int i = reqWidth;
        int i2 = reqHeight;
        double height = (double) options2.outHeight;
        double width = (double) options2.outWidth;
        long maxNumOfPixels = (long) (i * i2);
        int minSideLength = Math.min(i2, i);
        int lowerBound = maxNumOfPixels < 0 ? 1 : (int) Math.ceil(Math.sqrt((width * height) / ((double) maxNumOfPixels)));
        if (minSideLength < 0) {
            upperBound = 128;
        } else {
            upperBound = (int) Math.min(Math.floor(width / ((double) minSideLength)), Math.floor(height / ((double) minSideLength)));
        }
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if (maxNumOfPixels < 0 && minSideLength < 0) {
            return 1;
        }
        if (minSideLength < 0) {
            return lowerBound;
        }
        return upperBound;
    }
}
