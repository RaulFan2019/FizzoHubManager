package cn.fizzo.hub.manager.network;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.xutils.http.RequestParams;

import cn.fizzo.hub.manager.LocalApp;
import cn.fizzo.hub.manager.utils.TimeU;

/**
 * Created by Raul.fan on 2018/1/1 0001.
 */

public class RequestParamsBuilder {

    /**
     * 检查硬件版本信息
     * @return
     */
    public static RequestParams buildGetConsoleInfoRP(final Context context , final String url){
        MyRequestParams requestParams = new MyRequestParams(context, url);
        requestParams.addBodyParameter("serialno", LocalApp.getInstance().getCpuSerial());
        return requestParams;
    }


    /**
     * 检查应用场景列表
     * @return
     */
    public static RequestParams buildGetProviderListRP(final Context context , final String url){
        MyRequestParams requestParams = new MyRequestParams(context, url);
        requestParams.addBodyParameter("serialno", LocalApp.getInstance().getCpuSerial());
        return requestParams;
    }


    /**
     * 设置应用场景
     * @return
     */
    public static RequestParams buildSetConsoleProviderRP(final Context context , final String url ,final int provider){
        MyRequestParams requestParams = new MyRequestParams(context, url);
        requestParams.addBodyParameter("serialno", LocalApp.getInstance().getCpuSerial());
        requestParams.addBodyParameter("provider",provider+ "");
        return requestParams;
    }


    /**
     * 告诉服务器命令执行结果
     * @param context
     * @param url
     * @param commandid
     * @param commandidResult
     * @return
     */
    public static RequestParams buildSetCommandExecutedRP(final Context context,final String url,
                                                          final int commandid ,final String commandidResult){
        MyRequestParams requestParams = new MyRequestParams(context, url);
        requestParams.addBodyParameter("serialno", LocalApp.getInstance().getCpuSerial());
        requestParams.addBodyParameter("commandid",commandid+ "");
        requestParams.addBodyParameter("commandresult",commandidResult+ "");
        return requestParams;
    }


    /**
     * 检查硬件版本信息
     * @return
     */
    public static RequestParams buildSetConsoleAliveRP(final Context context , final String url){
        MyRequestParams requestParams = new MyRequestParams(context, url);
        requestParams.addBodyParameter("serialno", LocalApp.getInstance().getCpuSerial());
        requestParams.addBodyParameter("timestamp", TimeU.NowTime(TimeU.FORMAT_TYPE_1));
        return requestParams;
    }

}
