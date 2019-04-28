package cn.fizzo.hub.manager.ui;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.fizzo.hub.manager.LocalApp;
import cn.fizzo.hub.manager.R;
import cn.fizzo.hub.manager.config.UrlConfig;
import cn.fizzo.hub.manager.data.SPData;
import cn.fizzo.hub.manager.entity.net.BaseRE;
import cn.fizzo.hub.manager.entity.net.GetConsoleInfoRE;
import cn.fizzo.hub.manager.network.BaseResponseParser;
import cn.fizzo.hub.manager.network.HttpExceptionHelper;
import cn.fizzo.hub.manager.network.RequestParamsBuilder;
import cn.fizzo.hub.manager.utils.AppU;
import cn.fizzo.hub.manager.utils.LogU;
import cn.fizzo.hub.manager.utils.ShellU;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private static final int MSG_GET_CONSOLE_INFO_OK = 0x01;
    private static final int MSG_GET_CONSOLE_INFO_ERROR = 0x02;
    private static final int MSG_NOT_FIND_PACKAGE = 0x03;
    private static final int MSG_UPDATE_APP = 0x04;
    private static final int MSG_LAUNCH_APP = 0x05;
    private static final int MSG_DOWN_APP_PERCENT = 0x06;
    private static final int MSG_DOWNLOAD_APK_OK = 0x07;
    private static final int MSG_DOWNLOAD_APK_ERROR = 0x08;
    private static final int MSG_REBOOT = 0x09;
    private static final int MSG_POST_GET_CONSOLE = 0x10;


    private static final int MAX_ERROR_TIMER = 10;

    @BindView(R.id.tv_percent)
    TextView tvPercent;
    @BindView(R.id.ll_update)
    LinearLayout llUpdate;
    @BindView(R.id.tv_tip_more)
    TextView tvTipMore;

    @BindView(R.id.ll_tip)
    LinearLayout llTip;
    @BindView(R.id.ll_btn)
    LinearLayout llBtn;
    @BindView(R.id.btn_set_net)
    Button btnSetNet;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    /* data */
    private GetConsoleInfoRE mConsoleInfoRE;

    private int errorTimer = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @OnClick({R.id.btn_set_net, R.id.btn_device_debug, R.id.btn_set_app})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //配置网络
            case R.id.btn_set_net:
                startActivity(SetNetworkActivity.class);
                break;
            //设备调试
            case R.id.btn_device_debug:
                startActivity(DeviceDebugActivity.class);
                break;
            //设置场景
            case R.id.btn_set_app:
                startActivity(SetAppActivity.class);
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            case MSG_GET_CONSOLE_INFO_ERROR:
                String launchApp = SPData.getLaunchApp(MainActivity.this);
//                Toast.makeText(MainActivity.this, (String) msg.obj + errorTimer, Toast.LENGTH_LONG).show();
                //若没有设置过启动app
                if (launchApp.equals(SPData.SP_PACKAGE_DEFAULT)) {
                    llTip.setVisibility(View.GONE);
                    llBtn.setVisibility(View.VISIBLE);
                    btnSetNet.requestFocus();
                } else {
                    if (errorTimer < MAX_ERROR_TIMER) {
                        mHandler.sendEmptyMessageDelayed(MSG_POST_GET_CONSOLE,5 * 1000);
                    } else {
                        Toast.makeText(MainActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                        startApp(SPData.getLaunchApp(MainActivity.this), SPData.getLaunchClazz(MainActivity.this));
                    }
                }
                break;
            //获取成功
            case MSG_GET_CONSOLE_INFO_OK:
                //若没有设置启动APP
                if (mConsoleInfoRE.console.packagename.equals("")) {
                    llTip.setVisibility(View.GONE);
                    llBtn.setVisibility(View.VISIBLE);
                    btnSetNet.requestFocus();
                } else {
                    checkLaunchApp();
                }
                break;
            //没有找到app
            case MSG_NOT_FIND_PACKAGE:
                startActivity(SetAppActivity.class);
                break;
            //更新应用
            case MSG_UPDATE_APP:
                llUpdate.setVisibility(View.VISIBLE);
                tvPercent.setText("0");
                tvTipMore.setText("正在更新应用...");
                DownLoadApp();
                break;
            //下载APP进度
            case MSG_DOWN_APP_PERCENT:
                tvPercent.setText(msg.arg1 + "");
                break;
            //启动应用
            case MSG_LAUNCH_APP:
                launchApp();
                break;
            //下载APP 成功
            case MSG_DOWNLOAD_APK_OK:
                installApp((File) msg.obj);
                break;
            //下载APP 失败
            case MSG_DOWNLOAD_APK_ERROR:
                break;
            case MSG_REBOOT:
                ShellU.CommandResult result = ShellU.execCmd("reboot", true);
                break;
            case MSG_POST_GET_CONSOLE:
                postGetConsoleInfo();
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        try {
            tvVersion.setText("版本号:" + AppU.getVersionName(MainActivity.this));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        llBtn.setVisibility(View.GONE);
        llTip.setVisibility(View.VISIBLE);

        tvTipMore.setText("正在检查系统...");
        errorTimer = 0;
        postGetConsoleInfo();
    }

    @Override
    protected void causeGC() {

    }


    /**
     * 启动APP
     *
     * @param appPackage
     */
    private void startApp(String appPackage, String calzz) {
        ComponentName componetName = new ComponentName(
                appPackage,  //这个是另外一个应用程序的包名
                calzz);   //这个参数是要启动的Activity的全路径名
        try {
            Intent intent = new Intent();
            intent.setComponent(componetName);
            startActivity(intent);
        } catch (Exception e) {
            llTip.setVisibility(View.GONE);
            llBtn.setVisibility(View.VISIBLE);
            btnSetNet.requestFocus();
            Toast.makeText(getApplicationContext(), "没有找到应用程序！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 检查启动APP
     */
    private void checkLaunchApp() {
        PackageManager pm = this.getPackageManager();
        try {
//            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            PackageInfo info = pm.getPackageInfo(mConsoleInfoRE.console.packagename, 0);
//            LogU.v(TAG, "info.versionCode:" + info.versionCode);
//            LogU.v(TAG, "mConsoleInfoRE.console.latest_versioncode:" + mConsoleInfoRE.console.latest_versioncode);
            if (info.versionCode < mConsoleInfoRE.console.latest_versioncode) {
                mHandler.sendEmptyMessage(MSG_UPDATE_APP);
            } else {
                mHandler.sendEmptyMessage(MSG_LAUNCH_APP);
            }
        } catch (PackageManager.NameNotFoundException e) {
            mHandler.sendEmptyMessage(MSG_UPDATE_APP);
        }
    }

    /**
     * 静默安装App
     */
    private void installApp(File file) {
        LogU.v(TAG, "installApp:" + file.getAbsolutePath());
        tvTipMore.setText("正在安装应用...");
        ShellU.CommandResult result = ShellU.execCmd("pm install -r " + file.getAbsolutePath(), true);
        if (result.result == 0) {
            mHandler.sendEmptyMessage(MSG_REBOOT);
        } else {
            postInstallError(result.errorMsg);
        }
    }

    /**
     * 启动应用
     */
    private void launchApp() {
        SPData.setLaunchApp(MainActivity.this, mConsoleInfoRE.console.packagename);
        SPData.setLaunchClazz(MainActivity.this, mConsoleInfoRE.console.classname);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        try {
            ComponentName cn = new ComponentName(mConsoleInfoRE.console.packagename, mConsoleInfoRE.console.classname);
            intent.setComponent(cn);
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            LogU.v(TAG, "启动失败");
        }
    }


    /**
     * 获取设备信息
     */
    private void postGetConsoleInfo() {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                String url = SPData.getServiceIp(MainActivity.this) + UrlConfig.URL_GET_CONSOLE_INFO;
//                LogU.v(TAG, "postGetConsoleInfo url:" + url);
                RequestParams params = RequestParamsBuilder.buildGetConsoleInfoRP(MainActivity.this, url);
                x.http().post(params, new Callback.CommonCallback<BaseRE>() {
                    @Override
                    public void onSuccess(BaseRE result) {
                        //没有错误
                        if (result.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                            mConsoleInfoRE = JSON.parseObject(result.result, GetConsoleInfoRE.class);
                            mHandler.sendEmptyMessage(MSG_GET_CONSOLE_INFO_OK);
                        } else {
                            Message msg = new Message();
                            msg.what = MSG_GET_CONSOLE_INFO_ERROR;
                            msg.obj = result.errormsg;
                            mHandler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        errorTimer++;
                        Message msg = new Message();
                        msg.what = MSG_GET_CONSOLE_INFO_ERROR;
                        msg.obj = HttpExceptionHelper.getErrorMsg(ex);
                        mHandler.sendMessage(msg);
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
     * 下载APP
     */
    private void DownLoadApp() {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams("http://" + mConsoleInfoRE.console.apkurl);
                params.setCancelFast(true);
                mCancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
                    @Override
                    public void onSuccess(File result) {
                        Message msg = new Message();
                        msg.what = MSG_DOWNLOAD_APK_OK;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Message msg = new Message();
                        msg.what = MSG_DOWNLOAD_APK_ERROR;
                        msg.obj = HttpExceptionHelper.getErrorMsg(ex);
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }

                    @Override
                    public void onWaiting() {
                    }

                    @Override
                    public void onStarted() {
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isDownloading) {
                        Message msg = new Message();
                        msg.what = MSG_DOWN_APP_PERCENT;
                        msg.arg1 = (int) (current * 100 / total);
                        mHandler.sendMessage(msg);
                    }
                });
            }
        });
    }


    /**
     * 发送安装失败
     *
     * @param error
     */
    private void postInstallError(final String error) {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestParamsBuilder.buildSetCommandExecutedRP(MainActivity.this,
                        SPData.getServiceIp(MainActivity.this) + UrlConfig.URL_SET_COMMAND_EXECUTED, 0, error);
                x.http().post(params, new Callback.CommonCallback<BaseRE>() {
                    @Override
                    public void onSuccess(BaseRE result) {

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


}
