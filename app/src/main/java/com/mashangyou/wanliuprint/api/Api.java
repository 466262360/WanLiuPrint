package com.mashangyou.wanliuprint.api;



import com.mashangyou.wanliuprint.bean.res.CountWriteRes;
import com.mashangyou.wanliuprint.bean.res.LoginRes;
import com.mashangyou.wanliuprint.bean.res.PassWordRes;
import com.mashangyou.wanliuprint.bean.res.ResponseBody;
import com.mashangyou.wanliuprint.bean.res.SelectWriteRes;
import com.mashangyou.wanliuprint.bean.res.VerifyRes;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("mobile/login")
    Observable<ResponseBody<LoginRes>> login(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("mobile/resetPwd")
    Observable<ResponseBody<PassWordRes>> passWord(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("mobile/verification")
    Observable<ResponseBody<VerifyRes>> verify(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("mobile/use")
    Observable<ResponseBody> use(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("mobile/quit")
    Observable<ResponseBody> quit(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("mobile/countWrite")
    Observable<ResponseBody<CountWriteRes>> countWrite(@Field("token") String token);

    @FormUrlEncoded
    @POST("mobile/selectWrite")
    Observable<ResponseBody<SelectWriteRes>> selectWrite(@Field("token") String token);
}
