package com.neuroandroid.pyweather.config;

import android.graphics.Color;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.utils.UIUtils;

/**
 * Created by NeuroAndroid on 2017/3/15.
 */

public class Constant {
    /**
     * URL前缀
     */
    // public static final String BASE_URL = "http://192.168.97.159:80/clinicCloudDemo/v1/";
    public static final String BASE_URL = "https://free-api.heweather.com/v5/";
    public static final String BASE_WEATHER_IMG_URL = "https://cdn.heweather.com/cond_icon/";

    /**
     * SP配置TAG
     */
    public static final String PACKAGE_NAME_PREFERENCES = "config";

    /**
     * 是否显示过引导页
     */
    public static final String IS_USER_GUIDE_SHOWED = "is_user_guide_showed";

    /**
     * 用户信息存储在SP的TAG
     */
    public static final String USER = "USER";

    public static final int USER_TYPE = 0;

    public static final String APP_KEY = "4f7b1188c1674728ac1a5ad81def0388";

    public static final String APP_GUIDE = "app_guide";

    public static final String CITY = "city";

    public static final String YOUR_CITY = "your_city";

    /**
     * 返回的响应码
     */
    public static final int RESPONSE_CODE_OK = 200;
    public static final int RESPONSE_CODE_CREATED = 201;
    public static final int RESPONSE_CODE_NO_CONTENT = 204;
    public static final int RESPONSE_CODE_DELETE_OK = 260;
    public static final int RESPONSE_CODE_UPDATE_OK = 261;
    public static final int RESPONSE_CODE_CONTACTS_HAS_ADDED = -3001;
    public static final int RESPONSE_CODE_CONTACTS_USER_NONE = -1002;
    public static final int RESPONSE_CODE_NO_ROW_ERROR = -2003;
    public static final int RESPONSE_CODE_IMAGE_UPLOAD_FAILED = -3005;
    public static final int RESPONSE_CODE_USER_HAS_REGISTER = -1004;
    public static final int RESPONSE_CODE_PASSWORD_ERROR = -1001;

    /**
     * 接口正常
     */
    public static final String STATUS_OK = "ok";
    /**
     * 错误的用户 key
     */
    public static final String STATUS_INVALID_KEY = "invalid key";
    /**
     * 未知城市
     */
    public static final String STATUS_UNKNOWN_CITY = "unknown city";
    /**
     * 超过访问次数
     */
    public static final String STATUS_NO_MORE_REQUESTS = "no more requests";
    /**
     * 服务无响应或超时
     */
    public static final String STATUS_ANR = "anr";
    /**
     * 没有访问权限
     */
    public static final String STATUS_PERMISSION_DENIED = "permission denied";

    public static final String TEMP = "°";

    public static final String SP_CUSTOM_BACKGROUND = "sp_custom_background";

    public static final String SP_LINE_TYPE = "sp_line_type";  // 0 : 折线图  1 : 曲线图

    // 自定义背景透明度 (0-70)
    public static final String SP_CUSTOM_BACKGROUND_TRANSPARENCY = "sp_custom_background_transparency";

    // 自定义背景模糊程度 (0-100)
    public static final String SP_CUSTOM_BACKGROUND_BLUR_LEVEL = "sp_custom_background_blur_level";

    // app主题风格 (字体图标亮色主题还是暗色主题) Color.BLACK  Color.WHITE
    public static final String SP_APP_FONT_ICON_THEME_STYLE = "sp_app_font_icon_theme_style";

    public static final int LIGHT_THEME_STYLE_COLOR = Color.WHITE;

    public static final int DARK_THEME_STYLE_COLOR = UIUtils.getColor(R.color.colorGray333);

    public static final int LIGHT_THEME_STYLE_SUB_COLOR = UIUtils.getColor(R.color.white_3);

    public static final int DARK_THEME_STYLE_SUB_COLOR = UIUtils.getColor(R.color.black_3);
}
