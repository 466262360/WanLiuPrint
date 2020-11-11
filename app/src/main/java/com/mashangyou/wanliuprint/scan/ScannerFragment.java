package com.mashangyou.wanliuprint.scan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.mashangyou.wanliuprint.R;
import com.mashangyou.wanliuprint.bean.event.EventCode;


import org.greenrobot.eventbus.EventBus;


/**
 * Created by Administrator on 2016/11/28.
 * 扫码Activity
 */
public class ScannerFragment extends Fragment implements OnDecodeCompletionListener {

    ScannerView cScannerView;
    public static final String SCANRES = "scan_res";
    public static final String MESSAGE_ACTION = "com.mashangyou.wanliu.barCode";
    public static final String MESSAGE_BARCODE = "com.mashangyou.wanliu.barCodeMessage";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_scan, container, false);
        cScannerView = view.findViewById(R.id.scanner_view);
        cScannerView.setOnDecodeListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cScannerView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        cScannerView.onPause();

    }

    @Override
    public void onDecodeCompletion(String code) {

        if (!TextUtils.isEmpty(code)) {
            Intent intent = new Intent();
            intent.setAction(MESSAGE_ACTION);
            intent.putExtra(MESSAGE_BARCODE, code);
            EventBus.getDefault().post(new EventCode(code));
            LogUtils.d("abc","sendBroadcast="+code);
        } else {
            Toast.makeText(getContext(), "解析错误", Toast.LENGTH_SHORT).show();
        }
    }
}
