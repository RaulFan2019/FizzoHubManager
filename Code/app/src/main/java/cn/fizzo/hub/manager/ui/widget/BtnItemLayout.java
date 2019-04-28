package cn.fizzo.hub.manager.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.fizzo.hub.manager.R;

public class BtnItemLayout extends LinearLayout {

    Button button;

    public BtnItemLayout(Context context) {
        super(context);
    }

    public BtnItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_item, this, true);
        button = findViewById(R.id.btn);
    }

    /**
     * 设置按钮
     * @param tx
     */
    public void setText(final String tx){
        button.setText(tx);
    }

}
