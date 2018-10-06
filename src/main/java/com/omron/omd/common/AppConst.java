package com.omron.omd.common;

/**
 * AppConst
 *
 * @author zxy
 */
public class AppConst {

    private static final String APP_LOGO = "OMD";
    private static final String APP_NAME = "设备管理系统";
    private static final String APP_VERSION = "v0.1.1";

    public static final String BASE_VIEW_PATH = "/views";
    public static final String INIT_PASSWORD = "123456";

    public static final String SESSION = "currUser";
    public static final String ALL_AUTHC = "allAuthc";
    public static final String HOME_AUTHC = "homeAuthc";
    public static final String ID_AUTHC = "authcIdSet";
    public static final String KEY_AUTHC = "authcKeySet";

    /**
     * 应用 LOGO
     *
     * @return String
     */
    public static String getAppLogo() {
        return APP_LOGO;
    }

    /**
     * 应用名称
     *
     * @return String
     */
    public static String getAppName() {
        return APP_NAME;
    }

    /**
     * 版本号
     *
     * @return String
     */
    public static String getAppVersion() {
        return APP_VERSION;
    }

    /**
     * 初始密码
     *
     * @return String
     */
    public static String getInitPass() {
        return INIT_PASSWORD;
    }
}
