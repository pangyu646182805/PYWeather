package com.neuroandroid.pyweather.ui.fragment;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.adapter.CityListAdapter;
import com.neuroandroid.pyweather.base.BaseFragment;
import com.neuroandroid.pyweather.bean.CityBean;
import com.neuroandroid.pyweather.event.BaseEvent;
import com.neuroandroid.pyweather.event.CityManageEvent;
import com.neuroandroid.pyweather.provider.PYCityStore;
import com.neuroandroid.pyweather.ui.activity.MainActivity;
import com.neuroandroid.pyweather.ui.activity.SelectedCityActivity;
import com.neuroandroid.pyweather.utils.ShowUtils;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.TitleBar;
import com.neuroandroid.pyweather.widget.dialog.TitleDialog;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by NeuroAndroid on 2017/6/1.
 */

public class CityManageFragment extends BaseFragment implements MainActivity.MainActivityFragmentCallbacks {
    @BindView(R.id.rv_city_list)
    RecyclerView mRvCityList;
    @BindView(R.id.ll_empty)
    LinearLayout mLlEmpty;
    private ArrayList<CityBean.CityListBean> mAllCities;
    private CityListAdapter mCityListAdapter;

    public static CityManageFragment newInstance() {
        return new CityManageFragment();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_city_manage;
    }

    @Override
    protected void initView() {
        initTitleBar(UIUtils.getString(R.string.city_manager));
        initLeftAction(new TitleBar.ImageAction(R.drawable.ic_arrow_back) {
            @Override
            public void performAction(View view) {
                mActivity.onBackPressed();
            }
        });
        initRightAction(new TitleBar.TextAction(UIUtils.getString(R.string.add_city)) {
            @Override
            public void performAction(View view) {
                mIntent.setClass(mContext, SelectedCityActivity.class);
                UIUtils.toLayout(mIntent);
            }
        });
    }

    @Override
    protected void initData() {
        mAllCities = PYCityStore.getInstance(mContext).getAllCities();
        if (mCityListAdapter == null) {
            mCityListAdapter = new CityListAdapter(mContext, mAllCities);
            // 布局管理器
            mRvCityList.setLayoutManager(new LinearLayoutManager(mContext));
            // RecyclerView分割线
            mRvCityList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                    .sizeResId(R.dimen.y2).colorResId(R.color.split).build());
            mRvCityList.setAdapter(mCityListAdapter);
            // 长按删除
            mCityListAdapter.setItemLongClickListener((view, position, cityListBean) ->
                    new TitleDialog(mContext).setCustomTitle("是否删除该城市")
                            .setConfirmClickListener((dialog, v) -> {
                                new DeleteAsyncTask().execute(cityListBean);
                                dialog.dismiss();
                            }).show());
        } else {
            mCityListAdapter.replaceAll(mAllCities);
        }
        checkIsEmpty();
    }

    /**
     * 检查适配器的数量是否为空
     * 如为空则显示 empty view
     */
    private void checkIsEmpty() {
        if (mCityListAdapter == null || mCityListAdapter.getItemCount() == 0) {
            mLlEmpty.setVisibility(View.VISIBLE);
        } else {
            mLlEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent != null) {
            switch (baseEvent.getEventFlag()) {
                case BaseEvent.EVENT_CITY_MANAGE:
                    CityManageEvent cityManageEvent = (CityManageEvent) baseEvent;
                    new SaveAsyncTask().execute(cityManageEvent);
                    break;
            }
        }
    }

    /**
     * @return true : 表示已经消费返回事件
     */
    @Override
    public boolean handleBackPress() {
        return false;
    }

    /**
     * 异步任务
     * 保存或者更新城市列表信息
     */
    class SaveAsyncTask extends AsyncTask<CityManageEvent, Void, Boolean> {
        @Override
        protected Boolean doInBackground(CityManageEvent... cityManageEvents) {
            CityBean.CityListBean selectCity = cityManageEvents[0].getSelectCity();
            PYCityStore pyCityStore = PYCityStore.getInstance(mContext);
            int count = pyCityStore.find(selectCity.getId());
            if (count == 0) {
                // 没有记录才去添加记录
                pyCityStore.addItem(selectCity.getId(), selectCity.getCityZh(), -1, 100, 100, "");
            } else {
                pyCityStore.update(selectCity.getId(), selectCity.getCityZh(), -1, 100, 100, "");
            }
            return count != 0;
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            if (flag) ShowUtils.showToast("该城市已经添加过");
            // 后台任务执行完成时候重新刷新一下界面
            initData();
        }
    }

    /**
     * 异步任务
     * 删除数据库中的某一条记录
     */
    class DeleteAsyncTask extends AsyncTask<CityBean.CityListBean, Void, Boolean> {
        @Override
        protected Boolean doInBackground(CityBean.CityListBean... cityListBeen) {
            CityBean.CityListBean cityListBean = cityListBeen[0];
            PYCityStore pyCityStore = PYCityStore.getInstance(mContext);
            int delete = pyCityStore.deleteByCityId(cityListBean.getId());
            return delete == 1;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) ShowUtils.showToast("该城市已经删除");
            // 后台任务执行完成时候重新刷新一下界面
            initData();
        }
    }
}
