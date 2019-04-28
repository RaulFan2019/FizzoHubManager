package cn.fizzo.hub.manager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.fizzo.hub.manager.LocalApp;
import cn.fizzo.hub.manager.R;
import cn.fizzo.hub.manager.entity.event.NewAntInfo;
import cn.fizzo.hub.manager.entity.model.AntPlusInfo;
import cn.fizzo.hub.manager.service.SerialPortService;

public class DeviceDebugActivity extends BaseActivity {


    private static final int MSG_SHOW_NO_SIGN = 0x01;//显示没有信号
    private static final int INTERVAL_NO_SIGN = 5 * 1000;//信号保留时间


    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_device_serial)
    TextView tvDeviceSerial;
    @BindView(R.id.tv_ant)
    TextView tvAnt;


    /* data */
    private List<ShowHr> listShow = new ArrayList<>();//显示列表

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_debug;
    }

    @Override
    protected void myHandleMsg(Message msg) {
        switch (msg.what) {
            case MSG_SHOW_NO_SIGN:
                tvAnt.setText("NO SIGN");
                listShow.clear();
                mHandler.sendEmptyMessageDelayed(MSG_SHOW_NO_SIGN, INTERVAL_NO_SIGN);
                break;
        }
    }

    /**
     * 有新的心率信息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAntPlusHrEventBus(NewAntInfo event) {
        for (AntPlusInfo antPlusInfo : event.ants) {
            boolean found = false;
            //检查列表中是否存在
            for (ShowHr showHr : listShow) {
                if (showHr.ant.equals(antPlusInfo.serialNo)) {
                    showHr.hr = antPlusInfo.hr;
                    showHr.rssi = antPlusInfo.rssi;
                    showHr.step = antPlusInfo.step;
                    found = true;
                    break;
                }
            }
            //不存在就新增
            if (!found) {
                ShowHr show = new ShowHr(antPlusInfo.serialNo, antPlusInfo.hr, antPlusInfo.rssi, antPlusInfo.step);
                listShow.add(show);
            }
        }
        updateAntView();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void doMyCreate() {
        LocalApp.getInstance().getEventBus().register(this);
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_NO_SIGN, INTERVAL_NO_SIGN);

        Intent AntServiceI = new Intent(DeviceDebugActivity.this, SerialPortService.class);
        startService(AntServiceI);
    }

    @Override
    protected void causeGC() {
        Intent AntServiceI = new Intent(DeviceDebugActivity.this, SerialPortService.class);
        stopService(AntServiceI);

        LocalApp.getInstance().getEventBus().unregister(this);
    }


    /**
     * 更新ant 信息页面
     */
    private void updateAntView(){
        String showString = "";
        for (ShowHr showHr : listShow) {
            showString += showHr.ant + "[hr:" + showHr.hr + ",step:" + showHr.step + ",rssi:" + showHr.rssi + "]\n\n";
        }
        tvAnt.setText(showString);
        mHandler.removeMessages(MSG_SHOW_NO_SIGN);
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_NO_SIGN, INTERVAL_NO_SIGN);
    }

    /**
     * 用于显示的心率对象
     */
    class ShowHr {
        public String ant;
        public int hr;
        public int rssi;
        public int step;

        public ShowHr(String ant, int hr, int rssi, int step) {
            this.ant = ant;
            this.hr = hr;
            this.rssi = rssi;
            this.step = step;
        }
    }

}
