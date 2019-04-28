package cn.fizzo.hub.manager.config;

/**
 * Created by Raul.fan on 2018/1/23 0023.
 * Mail:raul.fan@139.com
 * QQ: 35686324
 */

public class AppConfig {

    /**
     * LOG 开关
     */
    public static final boolean LOG_V = true;
    public static final boolean LOG_D = true;
    public static final boolean LOG_I = false;
    public static final boolean LOG_W = false;
    public static final boolean LOG_E = false;

    /**
     * 捕捉异常开关
     */
    public static final boolean CATCH_EX = true;

    /**
     * 数据库
     */
    public static final String DB_NAME = "FizzoHubManager.db";
    public static final int DB_VERSION = 1;

}
