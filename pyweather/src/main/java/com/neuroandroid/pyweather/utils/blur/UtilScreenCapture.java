package com.neuroandroid.pyweather.utils.blur;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * 截屏工具类
 * <p>
 * Created by Bamboy on 2016/12/30.
 */
public class UtilScreenCapture {

    /**
     * 获取系统状态栏和软件标题栏，部分软件没有标题栏，看自己软件的配置；
     *
     * @return
     */
    private static int getOtherHeight(Window window) {
        try {
            Rect frame = new Rect();
            window.getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            int contentTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
            int titleBarHeight = contentTop - statusBarHeight;
            return statusBarHeight + titleBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取屏幕截屏 【不包含状态栏】
     *
     * @param activity
     * @return
     */
    public static Bitmap getScreenshot(Activity activity) {
        try {
            Window window = activity.getWindow();
            View view = window.getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            Bitmap bmp1 = view.getDrawingCache();
            /**
             * 除去状态栏和标题栏
             **/
            int height = getOtherHeight(window);
            return Bitmap.createBitmap(bmp1, 0, height, bmp1.getWidth(), bmp1.getHeight() - height);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Activity截图
     *
     * @param activity
     * @return bitmap
     */
    public static Bitmap getDrawing(Activity activity) {
        try {
            View view = ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
            view.setDrawingCacheEnabled(true);
            Bitmap tBitmap = view.getDrawingCache();
            // 拷贝图片，否则在setDrawingCacheEnabled(false)以后该图片会被释放掉
            tBitmap = tBitmap.createBitmap(tBitmap);
            view.setDrawingCacheEnabled(false);
            return tBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
