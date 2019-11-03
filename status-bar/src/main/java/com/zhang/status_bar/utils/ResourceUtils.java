package com.zhang.status_bar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

/**
 * resource工具
 */

public class ResourceUtils {

    private ResourceUtils() {
    }

    public static int getColor(@ColorRes int id) {
        return getColor(id);
    }

    public static int getColor(@NonNull Context context, @ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
        return ContextCompat.getDrawable(context, id);
    }

    public static String getString(@NonNull Context context, @StringRes int id) {
        return context.getString(id);
    }

    public static String getString(@StringRes int id) {
        return getString(id);
    }

    public static String getString(@NonNull Context context, @StringRes int id, Object... formatArgs) {
        return context.getString(id, formatArgs);
    }

    public static String[] getStringArray(@NonNull Context context, @ArrayRes int id) {
        return context.getResources().getStringArray(id);
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

}
