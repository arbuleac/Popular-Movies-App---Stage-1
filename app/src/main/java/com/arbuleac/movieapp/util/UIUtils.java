package com.arbuleac.movieapp.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.arbuleac.movieapp.MovieApp;

public abstract class UIUtils {
    public static int dpToPx(int dps) {
        return (int) getAppContext().getResources().getDisplayMetrics().density * dps;
    }

    public static int spToPx(int sps) {
        return (int) getAppContext().getResources().getDisplayMetrics().scaledDensity * sps;
    }

    public static int getDimension(int resId) {
        return (int) getAppContext().getResources().getDimension(resId);
    }

    public static int getColor(int colorId) {
        return getAppContext().getResources().getColor(colorId);
    }

    public static Resources getResources() {
        return getAppContext().getResources();
    }

    public static String getString(int stringId) {
        return getAppContext().getString(stringId);
    }

    public static Context getAppContext() {
        return MovieApp.getInstance().getApplicationContext();
    }

    public static Point getScreenSize() {
        WindowManager wm = (WindowManager) UIUtils.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static void hideKeyboard(TextView textView) {
        Context context = textView.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }
}
