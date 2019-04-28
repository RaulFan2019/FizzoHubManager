package cn.fizzo.hub.manager.ui.dialog;

import android.content.Context;

/**
 * Created by Raul.fan on 2017/6/18 0018.
 */

public class DialogBuilder {

    public DialogInput dialogInput;

    public void showInputDialog(final Context context, final String title) {
        if (dialogInput == null) {
            dialogInput = new DialogInput(context);
        }
        dialogInput.show(title);
    }

    public void showInputDialog(final Context context, final String title, final String content) {
        if (dialogInput == null) {
            dialogInput = new DialogInput(context);
        }
        dialogInput.show(title,content);
    }

    /**
     * 设置等待对话框的监听器
     *
     * @param listener
     */
    public void setInputDialogListener(DialogInput.onBtnClickListener listener) {
        dialogInput.setListener(listener);
    }
}
