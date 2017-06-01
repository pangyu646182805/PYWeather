package com.neuroandroid.pyweather.widget.menu;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.utils.SystemUtils;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeuroAndroid on 2017/2/9.
 */

public class TopRightMenu {
    private Activity mContext;
    private View mContentView;
    private RecyclerView mRecyclerView;
    private List<MenuItem> mMenuItemList;
    private PopupWindow mPopupWindow;
    private boolean showIcon = true;
    private boolean needAnimationStyle = true;
    private int mAanimationStyle;
    private static final int DEFAULT_ANIM_STYLE = R.style.TRM_ANIM_STYLE;
    private MenuAdapter mAdapter;

    public TopRightMenu(Activity context) {
        mContext = context;
        init();
    }

    private void init() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.right_popup_menu, null);
        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.rv_menu);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(UIUtils.getColor(R.color.split))
                .sizeResId((int) UIUtils.getDimen(R.dimen.y2)).build());

        mMenuItemList = new ArrayList<>();
        mAdapter = new MenuAdapter(mContext, mMenuItemList, this, showIcon);
    }

    private PopupWindow getPopupWindow() {
        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setContentView(mContentView);
        if (needAnimationStyle) {
            mPopupWindow.setAnimationStyle(mAanimationStyle <= 0 ? DEFAULT_ANIM_STYLE : mAanimationStyle);
        }

        mPopupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOnDismissListener(() -> SystemUtils.backgroundAlpha(mContext, 1.0f));

        mAdapter.setData(mMenuItemList);
        mAdapter.setShowIcon(showIcon);
        mRecyclerView.setAdapter(mAdapter);
        return mPopupWindow;
    }

    /**
     * 是否显示菜单图标
     *
     * @param show
     * @return
     */
    public TopRightMenu showIcon(boolean show) {
        this.showIcon = show;
        return this;
    }

    /**
     * 添加单个菜单
     *
     * @param item
     * @return
     */
    public TopRightMenu addMenuItem(MenuItem item) {
        mMenuItemList.add(item);
        return this;
    }

    /**
     * 添加多个菜单
     *
     * @param list
     * @return
     */
    public TopRightMenu addMenuList(List<MenuItem> list) {
        mMenuItemList.addAll(list);
        return this;
    }

    public TopRightMenu setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        mAdapter.setOnMenuItemClickListener(listener);
        return this;
    }

    public TopRightMenu showAsDropDown(View anchor) {
        showAsDropDown(anchor, 0, 0);
        SystemUtils.backgroundAlpha(mContext, 0.8f);
        return this;
    }

    public TopRightMenu showAsDropDown(View anchor, int xoff, int yoff) {
        if (mPopupWindow == null) {
            getPopupWindow();
        }
        if (!mPopupWindow.isShowing()) {
            SystemUtils.backgroundAlpha(mContext, 0.8f);
            mPopupWindow.showAsDropDown(anchor, xoff, yoff);
        }
        return this;
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(int position);
    }
}
