package com.mashangyou.wanliuprint.api;


import com.mashangyou.wanliuprint.BuildConfig;

/**
 * Created by Administrator on 2020/10/12.
 * Des:
 */
public class UrlApi {
    public static final String LOGIN = BuildConfig.BASE_URL+"mobile/login";
    public static final String VERIFY = BuildConfig.BASE_URL+"mobile/multiple/verification";
    public static final String USE_CODE = BuildConfig.BASE_URL+"mobile/use";
    public static final String COUNT_WRITE = BuildConfig.BASE_URL+"mobile/countWrite";
    public static final String COUNT_SELECTWRITE = BuildConfig.BASE_URL+"mobile/selectWrite";
    public static final String EXIT = BuildConfig.BASE_URL+"mobile/quit";
    public static final String RESETPWD = BuildConfig.BASE_URL+"mobile/resetPwd";
    public static final String QUERY = BuildConfig.BASE_URL+"mobile/querySeasion";
    public static final String ORDER = BuildConfig.BASE_URL+"mobile/orderlist";
}
