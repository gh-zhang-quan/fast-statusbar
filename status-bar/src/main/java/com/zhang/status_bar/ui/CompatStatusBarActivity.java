package com.zhang.status_bar.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.zhang.status_bar.R;
import com.zhang.status_bar.utils.OsUtil;
import com.zhang.status_bar.utils.ResourceUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author: zhang_quan
 * Email:  zhang_quan_888@163.com
 * Date:   2019/9/16
 * Des:    适配andorid 手机状态栏
 */

public class CompatStatusBarActivity extends AppCompatActivity {
    private FrameLayout mFrameLayoutContent;
    private View statusBarFix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_compat_status_bar);
        getResources();
        statusBarFix = findViewById(R.id.status_bar_fix);
        mFrameLayoutContent = findViewById(R.id.frame_layout_content_place);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View contentView = LayoutInflater.from(this).inflate(layoutResID, null, false);
        mFrameLayoutContent.addView(contentView);
    }

    /**
     * @param isImmersion    是否为沉浸式状态栏
     * @param isDark         是否状态栏字体和图标颜色为深色
     * @param statusBarColor 设置状态栏颜色
     */
    protected void setStatusBarColor(boolean isImmersion, boolean isDark, @ColorRes int statusBarColor) {
        setTranslucentStatus();
        ViewGroup.LayoutParams params = statusBarFix.getLayoutParams();
        params.height = !isImmersion ? getStatusBarHeight() : 0;
        statusBarFix.setLayoutParams(params);
        statusBarFix.setBackgroundColor(!isImmersion ? ResourceUtils.getColor(getApplication(), statusBarColor) : Color.TRANSPARENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || OsUtil.isMIUI() || OsUtil.isFlyme()) {
            setStatusBarFontIconDark(isDark);
        }

    }

    public int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 设置状态栏透明
     */
    private void setTranslucentStatus() {
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     *
     * @param dark 状态栏字体是否为深色
     */
    @SuppressWarnings("unchecked")
    @SuppressLint("PrivateApi")
    private void setStatusBarFontIconDark(boolean dark) {
        // 小米MIUI
        try {
            Window window = getWindow();
            Class clazz = getWindow().getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {       //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 魅族FlymeUI
        try {
            Window window = getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // android6.0+系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && dark) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration newConfig = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (res.getConfiguration().fontScale != 1 || res.getConfiguration().densityDpi != 480) {
                newConfig.fontScale = 1;
                newConfig.densityDpi = 480;
                res.updateConfiguration(newConfig, res.getDisplayMetrics());
            }
        } else {
            if (res.getConfiguration().fontScale != 1) {
                newConfig.fontScale = 1;
                res.updateConfiguration(newConfig, res.getDisplayMetrics());
            }
        }
        return res;
    }
}
