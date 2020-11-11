package com.mashangyou.wanliuprint.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


import com.mashangyou.wanliuprint.R;
import com.mashangyou.wanliuprint.bean.res.PublishInfoRes;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2020/11/2.
 * Des:
 */
public class PublishDialog extends Dialog {

    private final PublishInfoRes infos;
    private final SimpleDateFormat format;

    public PublishDialog(@NonNull Context context, PublishInfoRes infos) {
        super(context, R.style.CustomDialog);
        this.infos =infos;
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_publish);
        TextView tvDate = findViewById(R.id.tv_date);
        TextView tvStates = findViewById(R.id.tv_states);
        TextView tvPeople = findViewById(R.id.tv_people);
        Button button = findViewById(R.id.btn_dissmiss);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tvDate.setText(format.format(new Date(infos.getOpentime())));
        int i = infos.getAllcanuserperson() - infos.getUserdPersoncont();
        tvPeople.setText(i+"人");
        switch (infos.getSalestatus()) {
            case "0":
                tvStates.setText(getContext().getString(R.string.publish_17));
                tvStates.setTextColor(ContextCompat.getColor(getContext(),R.color.color_text_6));
                break;
            case "1":
                if (i==0){
                    tvStates.setText(getContext().getString(R.string.publish_18));
                    tvStates.setTextColor(ContextCompat.getColor(getContext(),R.color.color_text_6));
                }else{
                    tvStates.setText(getContext().getString(R.string.publish_15));
                    tvStates.setTextColor(ContextCompat.getColor(getContext(),R.color.color_stroke_1));
                }
                break;
            case "2":
                tvStates.setText(getContext().getString(R.string.publish_16));
                tvStates.setTextColor(ContextCompat.getColor(getContext(),R.color.color_btn));
                break;
            case "3":
                tvStates.setText(getContext().getString(R.string.publish_18));
                tvStates.setTextColor(ContextCompat.getColor(getContext(),R.color.color_text_6));
                break;
        }
    }
}
