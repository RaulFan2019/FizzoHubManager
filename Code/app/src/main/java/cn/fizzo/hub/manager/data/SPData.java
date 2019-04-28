package cn.fizzo.hub.manager.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SPData {


    public static final String SP_PACKAGE = "package";
    public static final String SP_CLAZZ = "clazz";
    public static final String SP_SERVICE_IP = "serviceIp";

    public static final String SP_PACKAGE_DEFAULT = "";
    public static final String SP_CLAZZ_DEFAULT = "";
    public static final String DEFAULT_SERVICE_IP = "http://www.123yd.cn";

    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 设置默认启动app
     * @param context
     * @param appPackage
     */
    public static void setLaunchApp(final Context context , final String appPackage){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SP_PACKAGE, appPackage);
        editor.commit();
    }


    /**
     * 获取默认启动app
     * @param context
     * @return
     */
    public static String getLaunchApp(final Context context){
        return getSharedPreferences(context).getString(SP_PACKAGE, SP_PACKAGE_DEFAULT);
    }

    /**
     * 设置默认启动clazz
     * @param context
     * @param appPackage
     */
    public static void setLaunchClazz(final Context context , final String appPackage){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SP_CLAZZ, appPackage);
        editor.commit();
    }


    /**
     * 获取默认启动clazz
     * @param context
     * @return
     */
    public static String getLaunchClazz(final Context context){
        return getSharedPreferences(context).getString(SP_CLAZZ, SP_CLAZZ_DEFAULT);
    }


    /**
     * 设置服务器IP
     * @param context
     * @param serviceIp
     */
    public static void setServiceIp(final Context context, final String serviceIp){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SP_SERVICE_IP, serviceIp);
        editor.commit();
    }

    /**
     * 获取服务器IP
     * @param context
     * @return
     */
    public static String getServiceIp(final Context context){
        return getSharedPreferences(context).getString(SP_SERVICE_IP, DEFAULT_SERVICE_IP);
    }
}
