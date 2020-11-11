package com.mashangyou.wanliuprint;

import android.app.Application;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;
import hdx.HdxUtil;

/**
 * Created by Administrator on 2020/10/9.
 * Des:
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private SerialPort mSerialPort = null;

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
            //SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
            String path = HdxUtil.GetPrinterPort();//dev/ttyS3
            //String path = "/dev/ttyS3";
            int baudrate = 115200;//Integer.decode(sp.getString("BAUDRATE", "-1"));
            /* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }
}
