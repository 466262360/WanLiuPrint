package ui.fragment;

import android.widget.RadioGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashangyou.wanliuprint.R;
import com.mashangyou.wanliuprint.adapter.ConsumeAdapter;
import com.mashangyou.wanliuprint.adapter.TotalRecordAdapter;
import com.mashangyou.wanliuprint.api.Contant;
import com.mashangyou.wanliuprint.api.HttpCallbackListener;
import com.mashangyou.wanliuprint.api.HttpManager;
import com.mashangyou.wanliuprint.api.UrlApi;
import com.mashangyou.wanliuprint.bean.res.CountWriteRes;
import com.mashangyou.wanliuprint.bean.res.LoginRes;
import com.mashangyou.wanliuprint.bean.res.ResponseBody;
import com.mashangyou.wanliuprint.bean.res.SelectWriteRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ui.activity.MainActivity;

/**
 * Created by Administrator on 2020/10/26.
 * Des:
 */
public class DataFragment extends BaseFragment{
    @BindView(R.id.rv_consume)
    RecyclerView rvConsume;
    @BindView(R.id.rv_record)
    RecyclerView rvRecord;
    @BindView(R.id.rg)
    RadioGroup rg;
    private List<CountWriteRes.Content> currentMonthList=new ArrayList<>();
    private List<CountWriteRes.Content> sevendaysList=new ArrayList<>();
    private List<CountWriteRes.Content> yearsList=new ArrayList<>();
    private List<CountWriteRes.Content> countWriteList;
    private ConsumeAdapter consumeAdapter;
    private List<SelectWriteRes.Record> recordList;
    private TotalRecordAdapter totalRecordAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_data;
    }

    @Override
    protected void initView() {
        rg.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rb_week:
                    countWriteList.clear();
                    countWriteList.addAll(sevendaysList);
                    consumeAdapter.setList(countWriteList);
                    break;
                case R.id.rb_month:
                    countWriteList.clear();
                    countWriteList.addAll(currentMonthList);
                    consumeAdapter.setList(countWriteList);
                    break;
                case R.id.rb_year:
                    countWriteList.clear();
                    countWriteList.addAll(yearsList);
                    consumeAdapter.setList(countWriteList);
                    break;
            }
        });
        countWriteList = new ArrayList<>();
        consumeAdapter = new ConsumeAdapter(R.layout.item_consume, countWriteList);
        rvConsume.setLayoutManager(new LinearLayoutManager(mContext));
        rvConsume.setAdapter(consumeAdapter);

        recordList = new ArrayList<>();
        totalRecordAdapter = new TotalRecordAdapter(R.layout.item_total_record, recordList);
        rvRecord.setLayoutManager(new LinearLayoutManager(mContext));
        rvRecord.setAdapter(totalRecordAdapter);
    }

    @Override
    protected void initData() {
        if (getActivity()!=null){
            ((MainActivity)getActivity()).setTopTitle(getString(R.string.title_4));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        countWrite();
        selectWrite();
    }

    private void countWrite() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPUtils.getInstance().getString(Contant.ACCESS_TOKEN));
        HttpManager.httpPost(UrlApi.COUNT_WRITE, hashMap, new HttpCallbackListener() {
            @Override
            public void onSuccess(String res) {
                ResponseBody<CountWriteRes> response = new Gson().fromJson(res, new TypeToken<ResponseBody<CountWriteRes>>() {}.getType());
                sevendaysList = response.getData().getSevendays();
                currentMonthList = response.getData().getCurrentMonth();
                yearsList = response.getData().getThisYear();
                countWriteList.clear();
                countWriteList.addAll(sevendaysList);
                consumeAdapter.setList(countWriteList);
            }

            @Override
            public void onFail(int code,String errorMsg) {
                ToastUtils.showShort(errorMsg);
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    private void selectWrite() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",SPUtils.getInstance().getString(Contant.ACCESS_TOKEN));
        HttpManager.httpPost(UrlApi.COUNT_SELECTWRITE, hashMap, new HttpCallbackListener() {
            @Override
            public void onSuccess(String res) {
                ResponseBody<SelectWriteRes> response = new Gson().fromJson(res, new TypeToken<ResponseBody<SelectWriteRes>>() {}.getType());
                recordList = response.getData().getRecord();
                totalRecordAdapter.setList(recordList);
            }

            @Override
            public void onFail(int code,String errorMsg) {
                ToastUtils.showShort(errorMsg);
            }

            @Override
            public void onError(String e) {
            }
        });
    }

}
