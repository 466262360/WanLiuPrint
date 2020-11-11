package ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.mashangyou.wanliuprint.R;
import com.mashangyou.wanliuprint.api.Contant;
import com.mashangyou.wanliuprint.api.HttpCallbackListener;
import com.mashangyou.wanliuprint.api.HttpManager;
import com.mashangyou.wanliuprint.api.UrlApi;
import com.mashangyou.wanliuprint.bean.event.EventFragment;
import com.mashangyou.wanliuprint.bean.res.PassWordRes;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ui.activity.MainActivity;

/**
 * Created by Administrator on 2020/10/29.
 * Des:
 */
public class PassWordFragment extends BaseFragment{
    @BindView(R.id.et_old)
    EditText etOld;
    @BindView(R.id.et_new)
    EditText etNew;
    @BindView(R.id.et_again)
    EditText etAgain;
    private String oldPass;
    private String newPass;
    private String againPass;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pass_word;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        if (getActivity()!=null){
            ((MainActivity)getActivity()).setTopTitle(getString(R.string.title_7));
        }
    }

    @OnClick({R.id.btn_cancel,R.id.btn_commit})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_cancel:
                EventBus.getDefault().post(new EventFragment(Contant.F_SETTING));
                break;
            case R.id.btn_commit:
                check();
                break;
        }
    }

    private void check() {
        oldPass = etOld.getText().toString().trim();
        newPass = etNew.getText().toString().trim();
        againPass = etAgain.getText().toString().trim();
        if (TextUtils.isEmpty(oldPass)){
            ToastUtils.showShort(getString(R.string.pass_word_4));
            return;
        }
        if (TextUtils.isEmpty(newPass)){
            ToastUtils.showShort(getString(R.string.pass_word_5));
            return;
        }
        if (TextUtils.isEmpty(againPass)){
            ToastUtils.showShort(getString(R.string.pass_word_6));
            return;
        }
        if (!newPass.equals(againPass)){
            ToastUtils.showShort(getString(R.string.pass_word_7));
            return;
        }
        commit();
    }



    private void commit() {
        showLoading();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("npwd",newPass);
        hashMap.put("opwd",oldPass);
        hashMap.put("token",SPUtils.getInstance().getString(Contant.ACCESS_TOKEN));
        HttpManager.httpPost(UrlApi.RESETPWD, hashMap, new HttpCallbackListener() {
            @Override
            public void onSuccess(String res) {
                hideLoading();
                SPUtils.getInstance().put(Contant.PASS_WORD, newPass);
                ToastUtils.showShort(getString(R.string.pass_word_8));
                EventBus.getDefault().post(new EventFragment(Contant.F_SETTING));
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
