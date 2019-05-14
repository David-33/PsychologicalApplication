package com.psychotherapeutic_app.utils;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * Created by David on 1/31/18
 */

public final class BitmapLoader {

    private static LruCache<String, Bitmap> sMemoryCache;
    private static boolean sIsLoadingAllowed;

//    static {
//        final int maxMemoryInKb = (int) (Runtime.getRuntime().maxMemory() / 1024L);
//        final int cacheSize = (int) (0.7 * maxMemoryInKb);
//
//        System.out.println("BitmapLoader__ maxMemory = " + maxMemoryInKb + " KB");
//        System.out.println("BitmapLoader__ cacheSize = " + cacheSize + " KB");
//
//        sMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(String key, Bitmap bitmap) {
//                return Math.round((key.getBytes().length + bitmap.getByteCount()) / 1024f);
//            }
//        };
//    }

    private BitmapLoader() {
        throw new AssertionError();
    }


    public static void setLoadingPermission(boolean isAllowed) {
        sIsLoadingAllowed = isAllowed;
    }

    public static boolean getLoadingPermission() {
        return sIsLoadingAllowed;
    }

    @Nullable
    public static Bitmap getBitmap(@NonNull final Resources res,
                                   @DrawableRes final int resId,
                                   final int reqWidth,
                                   final int reqHeight) {
        if (sIsLoadingAllowed) {
            String key = String.format(Locale.US, "%d[%dx%d]", resId, reqWidth, reqHeight);
            Bitmap bitmap = sMemoryCache.get(key);
            if (bitmap == null) {
                bitmap = loadBitmap(res, resId, reqWidth, reqHeight, key);
            }
            return bitmap;
        }
        return null;
    }

    @Nullable
    public static Bitmap[] getBitmaps(@NonNull final Resources res,
                                      final int reqWidth,
                                      final int reqHeight,
                                      @DrawableRes final int... resIds) {
        if (sIsLoadingAllowed) {
            Bitmap[] bitmaps = new Bitmap[resIds.length];
            for (int i = 0; i < resIds.length; ++i) {
                bitmaps[i] = getBitmap(res, resIds[i], reqWidth, reqHeight);
            }
            return bitmaps;
        }
        return null;
    }

    @Nullable
    public static Bitmap getBitmapFromAssets(@NonNull final AssetManager assetManager,
                                             @NonNull final String fileName,
                                             final int reqWidth,
                                             final int reqHeight) {
        if (sIsLoadingAllowed) {
            String key = String.format(Locale.US, "%s[%dx%d]", fileName, reqWidth, reqWidth);
            Bitmap bitmap = sMemoryCache.get(key);
            if (bitmap == null) {
                bitmap = loadBitmap(assetManager, fileName, reqWidth, reqHeight, key);
            }
            return bitmap;
        }
        return null;
    }

    @Nullable
    public static Bitmap[] getBitmapsFromAssets(@NonNull final AssetManager assetManager,
                                                @NonNull final String directory,
                                                final int reqWidth,
                                                final int reqHeight) {
        if (sIsLoadingAllowed) {
            String[] files = null;
            try {
                files = assetManager.list(directory);
            } catch (IOException e) {
                System.err.println("BitmapLoader__ IOException = " + e.getMessage());
            }

            if (files == null) {
                return new Bitmap[0];
            }

            Bitmap[] bitmaps = new Bitmap[files.length];
            for (int i = 0; i < files.length; ++i) {
                bitmaps[i] = getBitmapFromAssets(
                        assetManager,
                        directory + File.separator + files[i],
                        reqWidth, reqHeight
                );
            }

            return bitmaps;
        }
        return null;
    }


    private static Bitmap loadBitmap(@NonNull final Resources res,
                                     @DrawableRes final int resId,
                                     final int reqWidth,
                                     final int reqHeight,
                                     final String key) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
        bitmap = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, false);
        sMemoryCache.put(key, bitmap);
        System.out.println("BitmapLoader__ load bitmap from Resources = " + key);
        return bitmap;
    }

    @Nullable
    private static Bitmap loadBitmap(@NonNull final AssetManager assetManager,
                                     @NonNull final String fileName,
                                     final int reqWidth,
                                     final int reqHeight,
                                     @NonNull final String key) {
        Bitmap bitmap;
        try {
            InputStream is = assetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            System.err.println("BitmapLoader__ IOException = " + e.getMessage());
            return null;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true);
        sMemoryCache.put(key, bitmap);
        System.out.println("BitmapLoader__ load bitmap = " + key);
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
