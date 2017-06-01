package com.neuroandroid.pyweather.widget.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.neuroandroid.pyweather.R;
import com.neuroandroid.pyweather.utils.UIUtils;
import com.neuroandroid.pyweather.widget.NoPaddingTextView;

import java.util.List;

/**
 * Created by NeuroAndroid on 2017/2/9.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {
    private Context mContext;
    private List<MenuItem> mMenuItemList;
    private TopRightMenu mRightMenu;
    private boolean showIcon;
    private TopRightMenu.OnMenuItemClickListener onMenuItemClickListener;

    public void setOnMenuItemClickListener(TopRightMenu.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public MenuAdapter(Context context, List<MenuItem> menuItemList, TopRightMenu rightMenu, boolean showIcon) {
        mContext = context;
        mMenuItemList = menuItemList;
        mRightMenu = rightMenu;
        this.showIcon = showIcon;
    }

    public void setData(List<MenuItem> menuItemList) {
        this.mMenuItemList = menuItemList;
        notifyDataSetChanged();
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
        notifyDataSetChanged();
    }

    @Override
    public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_menu, null);
        MenuHolder menuHolder = new MenuHolder(view);
        return menuHolder;
    }

    @Override
    public void onBindViewHolder(MenuHolder holder, int position) {
        holder.setDataAndRefreshUI(mMenuItemList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMenuItemList == null) {
            return 0;
        } else {
            return mMenuItemList.size();
        }
    }

    public class MenuHolder extends RecyclerView.ViewHolder {
        private NoPaddingTextView mTvMenuText;
        private ImageView mIvMenuIcon;
        private LinearLayout mLlMenuItem;

        public MenuHolder(View itemView) {
            super(itemView);
            mTvMenuText = (NoPaddingTextView) itemView.findViewById(R.id.tv_menu_text);
            mIvMenuIcon = (ImageView) itemView.findViewById(R.id.iv_menu_icon);
            mLlMenuItem = (LinearLayout) itemView.findViewById(R.id.ll_menu_item);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams((int) UIUtils.getDimen(R.dimen.x260), (int) UIUtils.getDimen(R.dimen.y80));
            itemView.setLayoutParams(params);
        }

        public void setDataAndRefreshUI(MenuItem menuItem) {
            if (showIcon) {
                mIvMenuIcon.setVisibility(View.VISIBLE);
                mIvMenuIcon.setImageResource(menuItem.getIcon());
            } else {
                mIvMenuIcon.setVisibility(View.GONE);
            }
            mTvMenuText.setText(menuItem.getText());
            mLlMenuItem.setOnClickListener(view -> {
                mRightMenu.dismiss();
                if (onMenuItemClickListener != null) {
                    onMenuItemClickListener.onMenuItemClick(getLayoutPosition());
                }
            });
        }
    }
}
