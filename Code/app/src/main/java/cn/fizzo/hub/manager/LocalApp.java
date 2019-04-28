package cn.fizzo.hub.manager;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import cn.fizzo.hub.manager.service.KeepAliveService;
import cn.fizzo.hub.manager.utils.SerialU;

public class LocalApp extends MultiDexApplication{

    private static final String TAG = "LocalApp";

    public static Context applicationContext;//整个APP的上下文
    private static LocalApp instance;//Application 对象

    /* Device info */
    private String cpuSerial;
    private EventBus eventBus;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        //初始化xUtils
        x.Ext.init(this);
        startLocalService();
    }

    /**
     * 获取 LocalApplication
     *
     * @return
     */
    public static LocalApp getInstance() {
        if (instance == null) {
            instance = new LocalApp();
        }
        return instance;
    }

    /**
     * 获取设备CPU信息
     *
     * @return
     */
    public String getCpuSerial() {
        if (cpuSerial == null) {
            cpuSerial = SerialU.getCpuSerial();
        }
        return cpuSerial;
    }


    /**
     * 获取EventBus
     *
     * @return
     */
    public EventBus getEventBus() {
        if (eventBus == null) {
            eventBus = EventBus.builder()
                    .sendNoSubscriberEvent(false)
                    .logNoSubscriberMessages(false)
                    .build();
        }
        return eventBus;
    }


    /**
     *
     */
    private void startLocalService(){
        Intent KeepAliveServiceI = new Intent(this, KeepAliveService.class);
        startService(KeepAliveServiceI);
    }
}
