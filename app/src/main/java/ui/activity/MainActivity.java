package ui.activity;

import android.app.Presentation;
import android.content.Context;
import android.media.MediaRouter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashangyou.wanliuprint.MyApplication;
import com.mashangyou.wanliuprint.R;
import com.mashangyou.wanliuprint.api.Contant;
import com.mashangyou.wanliuprint.api.HttpCallbackListener;
import com.mashangyou.wanliuprint.api.HttpManager;
import com.mashangyou.wanliuprint.api.UrlApi;
import com.mashangyou.wanliuprint.bean.event.EventCode;
import com.mashangyou.wanliuprint.bean.event.EventErrorCode;
import com.mashangyou.wanliuprint.bean.event.EventFragment;
import com.mashangyou.wanliuprint.bean.event.EventPrint;
import com.mashangyou.wanliuprint.bean.res.ResponseBody;
import com.mashangyou.wanliuprint.bean.res.VerifyRes;
import com.mashangyou.wanliuprint.scan.ScannerFragment;
import com.mashangyou.wanliuprint.util.PrintContract;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import android_serialport_api.SerialPort;
import butterknife.BindView;
import butterknife.OnClick;
import hdx.HdxUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ui.fragment.CodeResultFragment;
import ui.fragment.DataFragment;
import ui.fragment.OrderFragment;
import ui.fragment.PassWordFragment;
import ui.fragment.PublishFragment;
import ui.fragment.ScanErrorFragment;
import ui.fragment.SellFragment;
import ui.fragment.SettingFragment;
import ui.fragment.VerifyResultFragment;


public class MainActivity extends BaseActivity {
    @BindView(R.id.cl_scan)
    ConstraintLayout clScan;
    @BindView(R.id.cl_publish)
    ConstraintLayout clPublish;
    @BindView(R.id.cl_sell)
    ConstraintLayout clSell;
    @BindView(R.id.cl_data)
    ConstraintLayout clData;
    @BindView(R.id.cl_setting)
    ConstraintLayout clSetting;
    @BindView(R.id.tv_1)
    TextView tvScan;
    @BindView(R.id.tv_2)
    TextView tvPublish;
    @BindView(R.id.tv_3)
    TextView tvSell;
    @BindView(R.id.tv_4)
    TextView tvData;
    @BindView(R.id.tv_5)
    TextView tvSetting;
    @BindView(R.id.iv_bg_2)
    ImageView ivPublishBg;
    @BindView(R.id.iv_bg_3)
    ImageView ivSellBg;
    @BindView(R.id.iv_bg_4)
    ImageView ivDataBg;
    @BindView(R.id.iv_bg_5)
    ImageView ivSettingBg;
    @BindView(R.id.iv_icon_2)
    ImageView ivPublishIcon;
    @BindView(R.id.iv_icon_3)
    ImageView ivSellIcon;
    @BindView(R.id.iv_icon_4)
    ImageView ivDataIcon;
    @BindView(R.id.iv_icon_5)
    ImageView ivSettingIcon;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private FragmentManager fragmentManager;
    private Fragment cFragment;
    private OrderFragment orderFragment;
    private ScannerFragment scannerFragment;
    private PublishFragment publishFragment;
    private SellFragment sellFragment;
    private DataFragment dataFragment;
    private SettingFragment settingFragment;
    private CodeResultFragment codeResultFragment;
    private ScanErrorFragment scanErrorFragment;
    private boolean isExit;
    private boolean verify;
    private VerifyResultFragment verifyResultFragment;
    private PassWordFragment passWordFragment;
  byte [] Status_Buffer=new byte[300];
    boolean Status_Start_Falg = false;
    int Status_Buffer_Index = 0;
    Time time = new Time();
    int TimeSecond;
    private static   String Error_State = "";
    private final byte  HDX_ST_NO_PAPER1 = (byte)(1<<0);     // 1 缺纸
    //private final byte  HDX_ST_BUF_FULL  = (byte)(1<<1);     // 1 缓冲满
    //private final byte  HDX_ST_CUT_ERR   = (byte)(1<<2);     // 1 打印机切刀错误
    private final byte  HDX_ST_HOT       = (byte)(1<<4);     // 1 打印机太热
    private final byte  HDX_ST_WORK      = (byte)(1<<5);     // 1 打印机在工作状态
    MyHandler handler;
    private boolean stop = false;
    private static final String TAG = "MainActivity";
    private final int SHOW_FONT_UPTAE_INFO=1;
    private boolean isIn=true;
    protected MyApplication mApplication;
    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    protected ReadThread mReadThread;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initToobar() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mApplication = (MyApplication) getApplication();
        try {
            mSerialPort = mApplication.getSerialPort();
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

            /* Create a receiving thread */
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (SecurityException e) {
            //DisplayError(R.string.error_security);
        } catch (IOException e) {
            //DisplayError(R.string.error_unknown);
        } catch (InvalidParameterException e) {
            //DisplayError(R.string.error_configuration);
        }
        HdxUtil.SwitchSerialFunction(HdxUtil.SERIAL_FUNCTION_PRINTER);
        handler = new MyHandler();
        fragmentManager = getSupportFragmentManager();
        initOrderFragment();
        verify=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    private void initOrderFragment() {
        orderFragment = new OrderFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, orderFragment).commitAllowingStateLoss();
        reSetButton(orderFragment);
    }

    private void initScannerFragment() {
        if (scannerFragment == null) {
            scannerFragment = new ScannerFragment();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, scannerFragment).commitAllowingStateLoss();
        reSetButton(scannerFragment);
    }

    private void initPublishFragment() {
        if (publishFragment == null) {
            publishFragment = new PublishFragment();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, publishFragment).commitAllowingStateLoss();
        reSetButton(publishFragment);
    }

    private void initSellFragment() {
        if (sellFragment == null) {
            sellFragment = new SellFragment();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, sellFragment).commitAllowingStateLoss();
        reSetButton(sellFragment);
    }

    private void initDataFragment() {
            dataFragment = new DataFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, dataFragment).commitAllowingStateLoss();
        reSetButton(dataFragment);
    }

    private void initSettingFragment() {
        if (settingFragment == null) {
            settingFragment = new SettingFragment();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, settingFragment).commitAllowingStateLoss();
        reSetButton(settingFragment);
    }

    private void initCodeResultFragment() {
        codeResultFragment = new CodeResultFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, codeResultFragment).commitAllowingStateLoss();
        reSetButton(codeResultFragment);
    }

    private void initVerifyFragment() {
        if (verifyResultFragment==null){
            verifyResultFragment = new VerifyResultFragment();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, verifyResultFragment).commitAllowingStateLoss();
    }

    private void initPassWordFragment() {
        if (passWordFragment==null){
            passWordFragment = new PassWordFragment();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, passWordFragment).commitAllowingStateLoss();
    }

    private void initScanErrorFragment() {
        if (scanErrorFragment == null) {
            scanErrorFragment = new ScanErrorFragment();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, scanErrorFragment).commitAllowingStateLoss();
        reSetButton(scanErrorFragment);
    }

    private void reSetButton(Fragment fragment) {
        cFragment = fragment;
        clearBg();
        reSetScanBtn();
        if (publishFragment == fragment) {
            clPublish.setBackgroundResource(R.drawable.shape_main_button_false);
            tvPublish.setTextColor(ContextCompat.getColor(this, R.color.color_text_2));
            ivPublishBg.setBackgroundResource(R.drawable.shape_circle_a_86);
            ivPublishIcon.setImageResource(R.drawable.main_4_s);
        } else if (sellFragment == fragment) {
            clSell.setBackgroundResource(R.drawable.shape_main_button_false);
            tvSell.setTextColor(ContextCompat.getColor(this, R.color.color_text_2));
            ivSellBg.setBackgroundResource(R.drawable.shape_circle_a_86);
            ivSellIcon.setImageResource(R.drawable.main_5_s);
        } else if (dataFragment == fragment) {
            clData.setBackgroundResource(R.drawable.shape_main_button_false);
            tvData.setTextColor(ContextCompat.getColor(this, R.color.color_text_2));
            ivDataBg.setBackgroundResource(R.drawable.shape_circle_a_86);
            ivDataIcon.setImageResource(R.drawable.main_6_s);
        } else if (settingFragment == fragment) {
            clSetting.setBackgroundResource(R.drawable.shape_main_button_false);
            tvSetting.setTextColor(ContextCompat.getColor(this, R.color.color_text_2));
            ivSettingBg.setBackgroundResource(R.drawable.shape_circle_a_86);
            ivSettingIcon.setImageResource(R.drawable.main_7_s);
        }
    }

    private void reSetScanBtn() {
        if (cFragment==scannerFragment) {
            clScan.setBackgroundResource(R.drawable.shape_scan_false);
            tvScan.setText(getString(R.string.main_0));
        } else {
            clScan.setBackgroundResource(R.drawable.shape_scan);
            tvScan.setText(getString(R.string.main_1));
        }
    }

    public void setTopTitle(String string) {
        tvTitle.setText(string);
    }

    private void clearBg() {
        clScan.setBackgroundResource(R.drawable.shape_scan_true);
        clPublish.setBackgroundResource(R.drawable.shape_main_button_true);
        ivPublishBg.setBackgroundResource(R.drawable.shape_circle_a_40);
        ivPublishIcon.setImageResource(R.drawable.main_4);

        clSell.setBackgroundResource(R.drawable.shape_main_button_true);
        ivSellBg.setBackgroundResource(R.drawable.shape_circle_a_40);
        ivSellIcon.setImageResource(R.drawable.main_5);

        clData.setBackgroundResource(R.drawable.shape_main_button_true);
        ivDataBg.setBackgroundResource(R.drawable.shape_circle_a_40);
        ivDataIcon.setImageResource(R.drawable.main_6);

        clSetting.setBackgroundResource(R.drawable.shape_main_button_true);
        ivSettingBg.setBackgroundResource(R.drawable.shape_circle_a_40);
        ivSettingIcon.setImageResource(R.drawable.main_7);

        tvPublish.setTextColor(ContextCompat.getColor(this, R.color.color_white));
        tvSell.setTextColor(ContextCompat.getColor(this, R.color.color_white));
        tvData.setTextColor(ContextCompat.getColor(this, R.color.color_white));
        tvSetting.setTextColor(ContextCompat.getColor(this, R.color.color_white));
    }

    @OnClick({R.id.cl_scan, R.id.cl_publish, R.id.cl_sell, R.id.cl_data, R.id.cl_setting})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.cl_scan:
                if (cFragment==scannerFragment){
                    initOrderFragment();
                }else{
                    initScannerFragment();
                }
                break;
            case R.id.cl_publish:
                initPublishFragment();
                break;
            case R.id.cl_sell:
                initSellFragment();
                break;
            case R.id.cl_data:
                initDataFragment();
                break;
            case R.id.cl_setting:
                initSettingFragment();
                break;
        }
    }


    private void verify(String code) {
        verify =false;
        String[] split = code.split(",");
        showLoading();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("barcode", split[0]);
        hashMap.put("brcodeo", split[1]);
        hashMap.put("brcodet", split[2]);
        hashMap.put("token", SPUtils.getInstance().getString(Contant.ACCESS_TOKEN));
        HttpManager.httpPost(UrlApi.VERIFY, hashMap, new HttpCallbackListener() {
            @Override
            public void onSuccess(String res) {
                hideLoading();
                verify=true;
                ResponseBody<VerifyRes> responseBody = new Gson().fromJson(res, new TypeToken<ResponseBody<VerifyRes>>() { }.getType());
                VerifyRes data = responseBody.getData();
                if (data != null) {
                    EventBus.getDefault().postSticky(data);
                    initCodeResultFragment();
                }
            }

            @Override
            public void onFail(int code, String errorMsg) {
                hideLoading();
                verify=true;
                if (code == 1) {
                    ToastUtils.showShort("查询失败");
                } else {
                    initScanErrorFragment();
                    EventBus.getDefault().postSticky(new EventErrorCode(code));
                }
            }

            @Override
            public void onError(String e) {
                hideLoading();
                        verify=true;
            }
        });
    }

    private void sendCommand2(String s) {
        try {
            byte[] bs = s.getBytes("cp936");
            mOutputStream.write(bs);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class PrintThread extends Thread {
        Map<String, String> printMap;

        public PrintThread(Map<String, String> map) {
            printMap = map;
        }

        public void run() {
            PrinterPowerOnAndWaitReady();
//            if (!Warning_When_Not_Normal()) {
//                return;
//            }
            try {
                sendCommand2(PrintContract.createXxTxt(printMap));
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }finally {
                try {
                    sleep(4* 1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                PrinterPowerOff();
            }
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveCode(EventCode data) {
        verify(data.getCode());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receivePrint(EventPrint data) {
        new PrintThread(data.getPrintMap()).start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveFragment(EventFragment data) {
        switch (data.getType()){
            case Contant.F_ORDER:
                initOrderFragment();
                break;
            case Contant.F_RESULT:
                initCodeResultFragment();
                break;
            case Contant.F_VERIFY:
                initVerifyFragment();
                break;
            case Contant.F_DATA:
                initDataFragment();
                break;
            case Contant.F_SELL:
                initSellFragment();
                break;
            case Contant.F_SETTING:
                initSettingFragment();
                break;
            case Contant.F_PUBLISH:
                initPublishFragment();
                break;
            case Contant.F_PASSWORD:
                initPassWordFragment();
                break;
            case Contant.F_SCAN:
                initScannerFragment();
                break;
        }
    }

    private class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            if (stop == true)
                return;
            switch (msg.what) {
                case SHOW_FONT_UPTAE_INFO:
                    ToastUtils.showLong((String)msg.obj);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mReadThread != null)
            mReadThread.interrupt();
        mApplication.closeSerialPort();
        mSerialPort = null;
        try {
            mOutputStream.close();
            mInputStream.close();
        } catch (IOException e) {
        }
        stop = true;
        PrinterPowerOff();
        super.onDestroy();

    }

    void setStatus_Buffer_Index(int v)
    {
        Status_Buffer_Index=v;
    }

    void PrinterPowerOnAndWaitReady() {

        //Status_Buffer_Index=0;
        //Status_Start_Falg = true;
        HdxUtil.SetPrinterPower(1);
        sleep(500);
    }

    private void sleep(int ms) {
        // Log.d(TAG,"start sleep "+ms);
        try {
            java.lang.Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Log.d(TAG,"end sleep "+ms);
    }

    void PrinterPowerOff() {
        HdxUtil.SetPrinterPower(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                finish();
            } else {
                ToastUtils.showShort("再按一次退出");
                isExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];

                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        // onDataReceived(buffer, size,n);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
