package cn.fizzo.hub.manager.ui;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.fizzo.hub.manager.LocalApp;
import cn.fizzo.hub.manager.R;
import cn.fizzo.hub.manager.data.SPData;
import cn.fizzo.hub.manager.ui.dialog.DialogBuilder;
import cn.fizzo.hub.manager.ui.dialog.DialogInput;
import cn.fizzo.hub.manager.utils.NetworkU;

public class SetNetworkActivity extends BaseActivity {


    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_device_serial)
    TextView tvDeviceSerial;


    /* data */
    DialogBuilder mDialogBuilder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_network;
    }

    @OnClick({R.id.btn_set_service_ip, R.id.btn_set_wifi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_set_service_ip:
                showSetServiceIpDialog();
                break;
            case R.id.btn_set_wifi:
                NetworkU.openSetting(SetNetworkActivity.this);
                break;
        }
    }

    @Override
    protected void myHandleMsg(Message msg) {

    }

    @Override
    protected void initData() {
        mDialogBuilder = new DialogBuilder();
    }

    @Override
    protected void initViews() {
        tvDeviceSerial.setText(LocalApp.getInstance().getCpuSerial());
        tvDeviceName.setText(LocalApp.getInstance().getCpuSerial().substring(LocalApp.getInstance().getCpuSerial().length() -8));
    }

    @Override
    protected void doMyCreate() {

    }

    @Override
    protected void causeGC() {

    }

    /**
     * 显示服务器IP
     */
    private void showSetServiceIpDialog(){
        mDialogBuilder.showInputDialog(SetNetworkActivity.this,"修改服务器IP", SPData.getServiceIp(SetNetworkActivity.this));
        mDialogBuilder.setInputDialogListener(new DialogInput.onBtnClickListener() {
            @Override
            public void onOkClick(String etStr) {
                SPData.setServiceIp(SetNetworkActivity.this,etStr);
            }
        });
    }

}
