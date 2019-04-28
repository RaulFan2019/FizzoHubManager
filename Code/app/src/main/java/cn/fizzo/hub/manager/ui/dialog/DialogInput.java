package cn.fizzo.hub.manager.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.fizzo.hub.manager.R;

/**
 * Created by Raul.fan on 2017/9/13 0013.
 */

public class DialogInput {

    public Dialog mDialog;


    TextView tvTitle;
    EditText et;
    LinearLayout llEt;
    Button btnOk;
    Button btnCancel;

    onBtnClickListener mListener;


    public interface onBtnClickListener {
        void onOkClick(String etStr);
    }


    /**
     * 初始化
     *
     * @param context
     */
    public DialogInput(Context context) {
        mDialog = new Dialog(context, R.style.DialogTheme);
        mDialog.setContentView(R.layout.dialog_input);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        tvTitle = (TextView) mDialog.findViewById(R.id.tv_title);
        btnOk = (Button) mDialog.findViewById(R.id.btn_ok);
        btnCancel = (Button) mDialog.findViewById(R.id.btn_cancel);
        et = (EditText) mDialog.findViewById(R.id.et);
        llEt = (LinearLayout) mDialog.findViewById(R.id.ll_et);


        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    llEt.setBackgroundResource(R.drawable.bg_dialog_et_focus);
                }else {
                    llEt.setBackgroundResource(R.drawable.bg_dialog_et_normal);
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    String etStr = et.getText().toString();
                    mListener.onOkClick(etStr);
                    mDialog.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
    }

    /**
     * 显示
     *
     * @param title
     */
    public void show(final String title) {
        tvTitle.setText(title);
        mDialog.show();
    }

    /**
     * 显示
     *
     * @param title
     */
    public void show(final String title, final String content) {
        tvTitle.setText(title);
        et.setText(content);
        mDialog.show();
    }


    /**
     * 设置监听器
     *
     * @param listener
     */
    public void setListener(onBtnClickListener listener) {
        mListener = listener;
    }

}
