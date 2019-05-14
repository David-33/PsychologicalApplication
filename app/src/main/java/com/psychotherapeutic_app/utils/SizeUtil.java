package com.psychotherapeutic_app.utils;

import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by David on 05 Oct 2017
 */

public final class SizeUtil {

    private static final double WIDE_SCREEN_ASPECT_RATIO = 1.6f;

    private static int sScreenWidth = -1;
    private static int sScreenHeight = -1;
    private static float sDiagonalInInches = -1.0f;

    private SizeUtil() {
    }

    public static int screenW() {
        if (sScreenHeight < 0 || sScreenWidth < 0) {
            throw new NotInitializedException();
        }
        return sScreenWidth;
    }

    public static int screenH() {
        if (sScreenHeight < 0 || sScreenWidth < 0) {
            throw new NotInitializedException();
        }
        return sScreenHeight;
    }

    public static float diagonalInInches() {
        if (sDiagonalInInches < 0.0f) {
            throw new NotInitializedException();
        }
        return sDiagonalInInches;
    }

    public static boolean isWideScreenDevice() {
        return sScreenHeight / sScreenWidth <= WIDE_SCREEN_ASPECT_RATIO;
    }

    /**
     * Initialize variables of scree size
     */
    public static void initScreenSize(WindowManager windowManager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(displayMetrics);

        // Since SDK_INT == 1
        sScreenWidth = displayMetrics.widthPixels;
        sScreenHeight = displayMetrics.heightPixels;

        // Includes window decorations (status bar / menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                sScreenWidth = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                sScreenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception ignored) {
                sScreenWidth = displayMetrics.widthPixels;
                sScreenHeight = displayMetrics.heightPixels;
            }
        } else if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                sScreenWidth = realSize.x;
                sScreenHeight = realSize.y;
            } catch (Exception ignored) {
                sScreenWidth = displayMetrics.widthPixels;
                sScreenHeight = displayMetrics.heightPixels;
            }
        }

        sDiagonalInInches = getDiagonalInInches(displayMetrics, sScreenWidth, sScreenHeight);
    }

    /**
     * @return calculates the given value depending on the screen diagonal of the current device
     */
    public static int relativeValue(float value) {
        if (sScreenHeight < 0 || sScreenWidth < 0) {
            throw new NotInitializedException();
        }

        // Diagonal of the current screen
        final float diagonal = (float) Math.sqrt(sScreenWidth * sScreenWidth + sScreenHeight * sScreenHeight);
        final float ratio = diagonal / 100;

        return (int) (value * ratio);
    }

    /**
     * @return calculates the given value depending on the screen width
     */
    public static int relativeW(double value) {
        if (sScreenWidth < 0) {
            throw new NotInitializedException();
        }

        return (int) Math.round((value / 100.0) * sScreenWidth);
    }

    /**
     * @return calculates the given value depending on the screen height
     */
    public static int relativeH(double value) {
        if (sScreenHeight < 0) {
            throw new NotInitializedException();
        }

        return (int) Math.round((value / 100.0) * sScreenHeight);
    }

    /**
     * Calculates and return text size relatively on screen diagonal in inches
     */
    public static float getTextSize(WindowManager windowManager, int value) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        float diagonalInInches = getDiagonalInInches(displayMetrics, sScreenWidth, sScreenHeight);
        final float DEFAULT_SCREEN_INCHES = 5.5f; // Default screen diagonal in inches

        final float scaleRatio = diagonalInInches / DEFAULT_SCREEN_INCHES;
        return value * scaleRatio;
    }


    private static float getDiagonalInInches(DisplayMetrics displayMetrics, int screenWidth, int screenHeight) {
        double x = screenWidth / displayMetrics.xdpi;
        double y = screenHeight / displayMetrics.ydpi;
        double screenInches = Math.sqrt(x * x + y * y);

        screenInches *= 10;
        screenInches = Math.round(screenInches);
        screenInches /= 10;

        return (float) screenInches;
    }


    private static class NotInitializedException extends RuntimeException {
        private final static String MESSAGE = "Screen size was not initialized. Call SizeUtil.initScreenSize() to initialize";

        private NotInitializedException() {
            super(MESSAGE);
        }
    }

}
