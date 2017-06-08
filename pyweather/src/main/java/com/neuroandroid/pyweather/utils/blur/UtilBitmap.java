package com.neuroandroid.pyweather.utils.blur;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * 图片工具类
 * <p>
 * Created by Bamboy on 2016/12/30.
 */
public class UtilBitmap {

    /**
     * 将Drawable对象转化为Bitmap对象
     *
     * @param drawable Drawable对象
     * @return 对应的Bitmap对象
     */
    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        //如果本身就是BitmapDrawable类型 直接转换即可
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        //取得Drawable固有宽高
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            //创建一个1x1像素的单位色图
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            //直接设置一下宽高和ARGB
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        //重新绘制Bitmap
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 模糊ImageView
     *
     * @param context
     * @param img     ImageView
     * @param level   模糊等级【0 ~ 25之间】
     */
    public static void blurImageView(Context context, ImageView img, float level) {
        // 将图片处理成模糊
        Bitmap bitmap = UtilBlurBitmap.blurBitmap(context, drawableToBitmap(img.getDrawable()), level);
        if (bitmap != null) {
            img.setImageBitmap(bitmap);
        }
    }

    /**
     * 模糊ImageView
     *
     * @param context
     * @param img     ImageView
     * @param level   模糊等级【0 ~ 25之间】
     * @param color   为ImageView蒙上一层颜色
     */
    public static void blurImageView(Context context, ImageView img, float level, int color) {
        // 将图片处理成模糊
        Bitmap bitmap = UtilBlurBitmap.blurBitmap(context, drawableToBitmap(img.getDrawable()), level);
        if (bitmap != null) {
            Drawable drawable = coverColor(context, bitmap, color);
            img.setImageDrawable(drawable);
        } else {
            img.setImageBitmap(null);
            img.setBackgroundColor(color);
        }
    }

    /**
     * 将bitmap转成蒙上颜色的Drawable
     *
     * @param context
     * @param bitmap
     * @param color   要蒙上的颜色
     * @return Drawable
     */
    public static Drawable coverColor(Context context, Bitmap bitmap, int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        new Canvas(bitmap).drawRoundRect(rect, 0, 0, paint);
        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
