package cn.fizzo.hub.manager.ui;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.fizzo.hub.manager.LocalApp;
import cn.fizzo.hub.manager.R;
import cn.fizzo.hub.manager.config.UrlConfig;
import cn.fizzo.hub.manager.data.SPData;
import cn.fizzo.hub.manager.entity.net.BaseRE;
import cn.fizzo.hub.manager.entity.net.GetProviderListRE;
import cn.fizzo.hub.manager.network.BaseResponseParser;
import cn.fizzo.hub.manager.network.HttpExceptionHelper;
import cn.fizzo.hub.manager.network.RequestParamsBuilder;

public class SetAppActivity extends BaseActivity {

    private static final int MSG_GET_LIST_OK = 0x01;
    private static final int MSG_GET_LIST_ERROR = 0x02;
    private static final int MSG_SET_OK = 0x03;
    private static final int MSG_SET_ERROR = 0x04;


    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_device_serial)
    TextView tvDeviceSerial;
    @BindView(R.id.ll_apps)
    LinearLayout llApps;

    /* data */
    private GetProviderListRE mProviderListRE;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_app;
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what){
            //获取列表成功
            case MSG_GET_LIST_OK:
                updateBtns();
                break;
                //获取列表错误
            case MSG_GET_LIST_ERROR:
                Toast.makeText(SetAppActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                break;
            case MSG_SET_OK:
                Toast.makeText(SetAppActivity.this,"设置成功",Toast.LENGTH_LONG).show();
                finish();
                break;
            case MSG_SET_ERROR:
                Toast.makeText(SetAppActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                break;
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        tvDeviceSerial.setText(LocalApp.getInstance().getCpuSerial());
        tvDeviceName.setText(LocalApp.getInstance().getCpuSerial().substring(LocalApp.getInstance().getCpuSerial().length() -8));
    }

    @Override
    protected void doMyCreate() {
        postGetApps();
    }

    @Override
    protected void causeGC() {

    }


    /**
     * 获取应用场景列表
     */
    private void postGetApps(){
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestParamsBuilder.buildGetProviderListRP(SetAppActivity.this,
                        SPData.getServiceIp(SetAppActivity.this) + UrlConfig.URL_GET_PROVIDER_LIST);
                x.http().post(params, new Callback.CommonCallback<BaseRE>() {
                    @Override
                    public void onSuccess(BaseRE result) {
                        if (result.errorcode == BaseResponseParser.ERROR_CODE_NONE){
                            mProviderListRE = JSON.parseObject(result.result,GetProviderListRE.class);
                            mHandler.sendEmptyMessage(MSG_GET_LIST_OK);
                        }else {
                            Message msg = new Message();
                            msg.what = MSG_GET_LIST_ERROR;
                            msg.obj = result.errormsg;
                            mHandler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Message msg = new Message();
                        msg.what = MSG_GET_LIST_ERROR;
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
     * 增加按钮
     */
    private void updateBtns(){
        for (final GetProviderListRE.ProvidersBean providersBean : mProviderListRE.providers){
            Button button = new Button(SetAppActivity.this);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 144);
            button.setLayoutParams(lparams);
            button.setTextAppearance(R.style.SetNetworkBtn);
            button.setBackgroundResource(R.drawable.selector_item);
            button.setTextSize(28);
            button.setText(providersBean.description);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postSetConsoleProvider(providersBean.provider);
                }
            });
            llApps.addView(button);
        }

        Button button = new Button(SetAppActivity.this);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 144);
        button.setLayoutParams(lparams);
        button.setTextAppearance(R.style.SetNetworkBtn);
        button.setBackgroundResource(R.drawable.selector_item);
        button.setTextSize(28);
        button.setText("暂不");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llApps.addView(button);
    }

    /**
     * 发送设置场景信息
     */
    private void postSetConsoleProvider(final int provider){
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestParamsBuilder.buildSetConsoleProviderRP(SetAppActivity.this,
                        SPData.getServiceIp(SetAppActivity.this) + UrlConfig.URL_SET_PROVIDER,provider);
                x.http().post(params, new Callback.CommonCallback<BaseRE>() {
                    @Override
                    public void onSuccess(BaseRE result) {
                        if (result.errorcode == BaseResponseParser.ERROR_CODE_NONE){
                            mHandler.sendEmptyMessage(MSG_SET_OK);
                        }else {
                            Message msg = new Message();
                            msg.what = MSG_SET_ERROR;
                            msg.obj = result.errormsg;
                            mHandler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Message msg = new Message();
                        msg.what = MSG_SET_ERROR;
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
}
