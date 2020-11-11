package ui.fragment;

import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashangyou.wanliuprint.R;
import com.mashangyou.wanliuprint.api.Contant;
import com.mashangyou.wanliuprint.api.HttpCallbackListener;
import com.mashangyou.wanliuprint.api.HttpManager;
import com.mashangyou.wanliuprint.api.UrlApi;
import com.mashangyou.wanliuprint.bean.event.EventFragment;
import com.mashangyou.wanliuprint.bean.res.CountWriteRes;
import com.mashangyou.wanliuprint.bean.res.ResponseBody;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ui.activity.LoginActivity;
import ui.activity.MainActivity;

/**
 * Created by Administrator on 2020/10/26.
 * Des:
 */
public class SettingFragment extends BaseFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        if (getActivity()!=null){
            ((MainActivity)getActivity()).setTopTitle(getString(R.string.title_5));
        }
    }

    @OnClick({R.id.cl_1,R.id.btn_exit})
    void onClick(View view){
        switch (view.getId()){
            case R.id.cl_1:
                EventBus.getDefault().post(new EventFragment(Contant.F_PASSWORD));
                break;
            case R.id.btn_exit:
                quit();
                break;
        }
    }

    private void quit() {
        showLoading();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPUtils.getInstance().getString(Contant.ACCESS_TOKEN));
        HttpManager.httpPost(UrlApi.EXIT, hashMap, new HttpCallbackListener() {
            @Override
            public void onSuccess(String res) {
                hideLoading();
                SPUtils.getInstance().put(Contant.ACCESS_TOKEN, "");
                SPUtils.getInstance().put(Contant.USER_NAME, "");
                SPUtils.getInstance().put(Contant.PASS_WORD, "");
                ActivityUtils.startActivity(LoginActivity.class);
                ActivityUtils.finishAllActivities();
            }

            @Override
            public void onFail(int code,String errorMsg) {
                hideLoading();
                ToastUtils.showShort(errorMsg);
            }

            @Override
            public void onError(String e) {
                hideLoading();
            }
        });
    }
}
