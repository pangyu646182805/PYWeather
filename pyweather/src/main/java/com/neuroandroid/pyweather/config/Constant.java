package com.neuroandroid.pyweather.config;

/**
 * Created by NeuroAndroid on 2017/3/15.
 */

public class Constant {
    /**
     * URL前缀
     */
    // public static final String BASE_URL = "http://192.168.97.159:80/clinicCloudDemo/v1/";
    public static final String BASE_URL = "https://free-api.heweather.com/v5/";

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
}
