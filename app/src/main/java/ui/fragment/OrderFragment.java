package ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashangyou.wanliuprint.R;
import com.mashangyou.wanliuprint.adapter.OrderAdapter;
import com.mashangyou.wanliuprint.api.Contant;
import com.mashangyou.wanliuprint.api.HttpCallbackListener;
import com.mashangyou.wanliuprint.api.HttpManager;
import com.mashangyou.wanliuprint.api.UrlApi;
import com.mashangyou.wanliuprint.bean.event.EventFragment;
import com.mashangyou.wanliuprint.bean.res.CountWriteRes;
import com.mashangyou.wanliuprint.bean.res.OrderListRes;
import com.mashangyou.wanliuprint.bean.res.ResponseBody;


import org.greenrobot.eventbus.EventBus;

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
public class OrderFragment extends BaseFragment{
    @BindView(R.id.rv_order)
    RecyclerView rvOrder;
    private OrderAdapter orderAdapter;
    private List<OrderListRes.Order> orderList=new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView() {
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(R.layout.item_order, orderList);
        rvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrder.setAdapter(orderAdapter);
    }

    @Override
    protected void initData() {
        if (getActivity()!=null){
            ((MainActivity)getActivity()).setTopTitle(getString(R.string.title_1));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getOrder();
    }

    private void getOrder() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",SPUtils.getInstance().getString(Contant.ACCESS_TOKEN));
        HttpManager.httpGet(UrlApi.ORDER+"?token="+SPUtils.getInstance().getString(Contant.ACCESS_TOKEN), hashMap, new HttpCallbackListener() {
            @Override
            public void onSuccess(String res) {
                ResponseBody<OrderListRes> response = new Gson().fromJson(res, new TypeToken<ResponseBody<OrderListRes>>() {}.getType());
                orderList = response.getData().getOrderList();
                orderAdapter.setList(orderList);
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
