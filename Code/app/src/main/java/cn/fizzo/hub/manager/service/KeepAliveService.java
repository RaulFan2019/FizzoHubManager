package cn.fizzo.hub.manager.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cn.fizzo.hub.manager.config.UrlConfig;
import cn.fizzo.hub.manager.data.SPData;
import cn.fizzo.hub.manager.entity.net.BaseRE;
import cn.fizzo.hub.manager.entity.net.KeepAliveRE;
import cn.fizzo.hub.manager.network.BaseResponseParser;
import cn.fizzo.hub.manager.network.RequestParamsBuilder;
import cn.fizzo.hub.manager.utils.ShellU;

public class KeepAliveService extends Service {


    /* contains */
    private static final String TAG = "ConsoleInfoMonitorService";
    private static final boolean DEBUG = false;

    private int INTERVAL_DEFAULT_MONITOR = 10 * 1000;//呼吸间隔
    private int OFFLINE_TIMER_MAX = 6 * 30 ;

    private int offlineTimer = 0;

    private static final int MSG_POST_KEEP_ALIVE = 0x01;//更新设备信息
    private static final int MSG_REBOOT = 0x02;//APP重启
    private static final int MSG_POST_COMMAND = 0x03;//发送执行结果


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //获取设备信息
                case MSG_POST_KEEP_ALIVE:
                    postKeepAlive();
                    mHandler.sendEmptyMessageDelayed(MSG_POST_KEEP_ALIVE, INTERVAL_DEFAULT_MONITOR);
                    break;
                case MSG_REBOOT:
                    ShellU.CommandResult result = ShellU.execCmd("reboot",true);
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler.sendEmptyMessage(MSG_POST_KEEP_ALIVE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 发送保活
     */
    private void postKeepAlive() {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestParamsBuilder.buildSetConsoleAliveRP(KeepAliveService.this,
                        SPData.getServiceIp(KeepAliveService.this) + UrlConfig.URL_KEEP_ALIVE);
                x.http().post(params, new Callback.CommonCallback<BaseRE>() {
                    @Override
                    public void onSuccess(BaseRE result) {
                        offlineTimer = 0;
                        if (result.errorcode == BaseResponseParser.ERROR_CODE_NONE){
                            KeepAliveRE keepAliveRE = JSON.parseObject(result.result,KeepAliveRE.class);
                            if (keepAliveRE.keeper.linuxcommand.equals("")){
                                //DO NOTHING
                            }else if (keepAliveRE.keeper.linuxcommand.equals("reboot")){
                                postSetCommandResult(keepAliveRE.keeper.commandid,"reboot");
                            }else {
                                doLinuxCommand(keepAliveRE.keeper.commandid,keepAliveRE.keeper.linuxcommand);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        offlineTimer ++;
                        if (offlineTimer > OFFLINE_TIMER_MAX){
                            mHandler.sendEmptyMessage(MSG_REBOOT);
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }

    /**
     * 发送结果
     * @param commandId
     * @param commandResult
     */
    private void postSetCommandResult(final int commandId, final String commandResult){
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestParamsBuilder.buildSetCommandExecutedRP(KeepAliveService.this,
                        SPData.getServiceIp(KeepAliveService.this) + UrlConfig.URL_SET_COMMAND_EXECUTED,
                        commandId,commandResult);
                x.http().post(params, new Callback.CommonCallback<BaseRE>() {
                    @Override
                    public void onSuccess(BaseRE result) {
                        if (result.errorcode == BaseResponseParser.ERROR_CODE_NONE){
                            if (commandResult.equals("reboot")){
                                mHandler.sendEmptyMessage(MSG_REBOOT);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }

    /**
     * 执行命令
     * @param commandId
     * @param command
     */
    private void doLinuxCommand(final int commandId , final String command){
        ShellU.CommandResult result = ShellU.execCmd(command,true);
        if (result.result == 0){
            postSetCommandResult(commandId,result.successMsg);
        }else {
           postSetCommandResult(commandId,result.errorMsg);
        }
    }

}
