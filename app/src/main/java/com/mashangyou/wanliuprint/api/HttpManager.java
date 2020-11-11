package com.mashangyou.wanliuprint.api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.mashangyou.wanliuprint.bean.res.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import ui.activity.LoginActivity;

/**
 * Created by Administrator on 2020/10/12.
 * Des:
 */
public class HttpManager {

    Handler handler;

    public static void httpPost(final String url, final Map<String, String> params, final HttpCallbackListener listener) {
        final HttpManager http = new HttpManager();
        final Handler handler = http.getHandler(listener);
        new Thread(new Runnable() {
            @Override
            public void run() {
                http.sendPost(url, params, listener, handler);
            }
        }).start();
    }

    public static void httpGet(final String url, final Map<String, String> params, final HttpCallbackListener listener) {
        final HttpManager http = new HttpManager();
        final Handler handler = http.getHandler(listener);
        new Thread(new Runnable() {
            @Override
            public void run() {
                http.sendGet(url, params, listener, handler);
            }
        }).start();
    }

    public static void sendPost(final String address, final Map<String, String> params, final HttpCallbackListener listener, Handler handler) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(address);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            // conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            OutputStream out = conn.getOutputStream();
            out.write(getRequestData(params).toString().getBytes());
            out.flush();
            out.close();
            // 从连接中读取响应信息
            String msg = "";
            int code = conn.getResponseCode();
            if (code == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    msg += line + "\n";
                }
                reader.close();
                // LogUtils.d(msg);
                if (listener != null) {
                    ResponseBody responseBody = new Gson().fromJson(msg, ResponseBody.class);
                    if (0 == responseBody.getCode()) {
                        Message m = Message.obtain();
                        m.what = 0;
                        m.obj = msg;
                        handler.sendMessage(m);
                    } else if (401 == responseBody.getCode()) {
                        ActivityUtils.finishAllActivities();
                        ActivityUtils.startActivity(LoginActivity.class);
                    } else {
                        Message m = Message.obtain();
                        m.what = 1;
                        m.obj = responseBody.getErrMsg();
                        m.arg1 = responseBody.getCode();
                        handler.sendMessage(m);
                    }
                }
            } else {
                Message m = Message.obtain();
                m.what = 2;
                m.obj = "网络错误";
                handler.sendMessage(m);
            }
        } catch (Exception e) {
            if (listener != null) {
                // 回调onError()方法
                Message m = Message.obtain();
                m.what = 2;
                m.obj = e.getMessage();
                handler.sendMessage(m);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static void sendGet(final String address, final Map<String, String> params, final HttpCallbackListener listener, Handler handler) {
        HttpURLConnection conn = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;
        LogUtils.d(address);
        try {
            URL url = new URL(address);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            // conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            int code = conn.getResponseCode();
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sbf.append(line);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
                if (listener != null) {
                    ResponseBody responseBody = new Gson().fromJson(result, ResponseBody.class);
                    if (0 == responseBody.getCode()) {
                        Message m = Message.obtain();
                        m.what = 0;
                        m.obj = result;
                        handler.sendMessage(m);
                    } else if (401 == responseBody.getCode()) {
                        ActivityUtils.finishAllActivities();
                        ActivityUtils.startActivity(LoginActivity.class);
                    } else {
                        Message m = Message.obtain();
                        m.what = 1;
                        m.obj = responseBody.getErrMsg();
                        m.arg1 = responseBody.getCode();
                        handler.sendMessage(m);
                    }
                }
            } else {
                Message m = Message.obtain();
                m.what = 2;
                m.obj = "网络错误";
                handler.sendMessage(m);
            }
        } catch (Exception e) {
            if (listener != null) {
                Message m = Message.obtain();
                m.what = 2;
                m.obj = e.getMessage();
                handler.sendMessage(m);
            }
        } finally {
            try {

                if (null != br) {
                    br.close();
                }

                if (null != is) {
                    is.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static StringBuffer getRequestData(Map<String, String> params) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    private Handler getHandler(final HttpCallbackListener call) {
        if (null == handler) {
            handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    String s = msg.obj + "";
                    LogUtils.d(s);
                    if (msg.what == 0) {
                        call.onSuccess(s);
                    } else if (msg.what == 1) {
                        call.onFail(msg.arg1, s);
                    } else if (msg.what == 2) {
                        call.onError(s);
                    }
                }
            };
        }
        return handler;
    }
}
