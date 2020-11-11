package com.mashangyou.wanliuprint.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.mashangyou.wanliuprint.R;


/**
 * Created by Administrator on 2020/5/25.
 * Des:
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
    }
}
