package com.mashangyou.wanliuprint.api;

import com.mashangyou.wanliuprint.bean.res.ResponseBody;

/**
 * Created by Administrator on 2020/10/12.
 * Des:
 */
public interface HttpCallbackListener {
    /**
     * 服务器成功响应我们请求的时候调用
     * @param res
     */
    void onSuccess(String res);

    void onFail(int code,String errorMsg);
    /**
     * 进行网络操作出现错误的时候调用
     * @param e
     */
    void onError(String e);

}
