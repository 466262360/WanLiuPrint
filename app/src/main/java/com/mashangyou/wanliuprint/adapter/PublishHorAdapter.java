package com.mashangyou.wanliuprint.adapter;

import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mashangyou.wanliuprint.R;
import com.mashangyou.wanliuprint.bean.res.PublishInfoRes;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2020/9/10.
 * Des:
 */
public class PublishHorAdapter extends BaseQuickAdapter<PublishInfoRes, BaseViewHolder> {

    private final SimpleDateFormat format;
    private  int mWidth;

    public PublishHorAdapter(int layoutResId, @Nullable List<PublishInfoRes> data, int width) {
        super(layoutResId, data);
        format = new SimpleDateFormat("HH:mm");
        mWidth =width;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PublishInfoRes data) {
        baseViewHolder.setText(R.id.tv_time, format.format(new Date(data.getOpentime())));
        ConstraintLayout view = baseViewHolder.getView(R.id.cl_bg);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (mWidth- ConvertUtils.dp2px(12)*8-ConvertUtils.dp2px(25)) / 8;
        view.setLayoutParams(params);
        switch (data.getSalestatus()) {
            case "0":
                baseViewHolder
                        .setVisible(R.id.tv_bg,false)
                        .setTextColorRes(R.id.tv_time, R.color.color_text_6)
                        .setBackgroundResource(R.id.cl_bg, R.drawable.shape_publish_2);
                break;
            case "1":
                if (data.getAllcanuserperson()-data.getUserdPersoncont()==0){
                    baseViewHolder
                            .setVisible(R.id.tv_bg,true)
                            .setText(R.id.tv_bg, getContext().getString(R.string.publish_9))
                            .setTextColorRes(R.id.tv_time, R.color.color_stroke_4)
                            .setBackgroundResource(R.id.cl_bg, R.drawable.shape_publish_4);
                }else{
                    baseViewHolder
                            .setVisible(R.id.tv_bg,true)
                            .setText(R.id.tv_bg, getContext().getString(R.string.publish_6))
                            .setTextColorRes(R.id.tv_time, R.color.color_white)
                            .setBackgroundResource(R.id.cl_bg, R.drawable.shape_publish_1);
                }
                break;
            case "2":
                baseViewHolder
                        .setVisible(R.id.tv_bg,true)
                        .setText(R.id.tv_bg, getContext().getString(R.string.publish_7))
                        .setTextColorRes(R.id.tv_time, R.color.color_white)
                        .setBackgroundResource(R.id.cl_bg, R.drawable.shape_publish_3);
                break;
            case "3":
                baseViewHolder
                        .setVisible(R.id.tv_bg,true)
                        .setText(R.id.tv_bg, getContext().getString(R.string.publish_9))
                        .setTextColorRes(R.id.tv_time, R.color.color_stroke_4)
                        .setBackgroundResource(R.id.cl_bg, R.drawable.shape_publish_4);
                break;
        }


    }
}
