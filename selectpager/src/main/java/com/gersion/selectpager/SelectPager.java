package com.gersion.selectpager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.gersion.selectpager.selectView.SelectView;
import com.gersion.selectpager.viewholder.BaseSelectViewHolder;

/**
 * Created by gersy on 2017/5/28.
 */

public class SelectPager {
    private SelectView mSelectView;
    private Context mContext;
    private IFilter mFilter;
    private View mTitleView;
    private View mBottmonView;
    private boolean mEnableTitle;
    private boolean mShowDivider;
    private int mDividerColor;
    private int mEnableAnimation;
    private BaseSelectViewHolder mSelectViewHolder;
    private ViewGroup mContainerView;
    private boolean mEnableSearch;

    public SelectPager with(Context context) {
        mContext = context;
        return this;
    }

    public SelectPager setContanerView(ViewGroup containerView) {
        mContainerView = containerView;
        return this;
    }

    public SelectPager filter(IFilter filter) {
        mFilter = filter;
        return this;
    }

    public SelectPager titleView(View titleView) {
        mTitleView = titleView;
        return this;
    }

    public SelectPager enableSearch(boolean enableSearch) {
        mEnableSearch = enableSearch;
        return this;
    }

    public SelectPager bottomView(View bottmonView) {
        mBottmonView = bottmonView;
        return this;
    }

    public SelectPager enableTitle(boolean enableTitle) {
        mEnableTitle = enableTitle;
        return this;
    }

    public SelectPager showDivider(boolean showDivider) {
        mShowDivider = showDivider;
        return this;
    }

    public SelectPager dividerColor(int dividerColor) {
        if (mContext == null) {
            throw new NullPointerException("先调用with()传入context");
        }
        mDividerColor = mContext.getResources().getColor(dividerColor);
        return this;
    }

    public SelectPager enableAnimation(int enableAnimation) {
        mEnableAnimation = enableAnimation;
        return this;
    }

    public SelectPager setViewHolder(BaseSelectViewHolder selectViewHolder) {
        mSelectViewHolder = selectViewHolder;
        return this;
    }

    public SelectView build() {
        if (mContext == null) {
            throw new NullPointerException("先调用with()传入context");
        }
        if (mContainerView == null) {
            throw new NullPointerException("父级View不能为空");
        }
        if (mSelectViewHolder == null) {
            throw new NullPointerException("viewHolder不能为空");
        }
        mSelectView = new SelectView(mContext);
        mSelectView.mBottmonView = mBottmonView;
        mSelectView.mFilter = mFilter;
        mSelectView.mEnableSearch = mEnableSearch;
        mSelectView.mDividerColor = mDividerColor;
        mSelectView.mEnableTitle = mEnableTitle;
        mSelectView.mEnableAnimation = mEnableAnimation;
        mSelectView.mShowDivider = mShowDivider;
        mSelectView.mTitleView = mTitleView;
        mSelectView.mSelectViewHolder = mSelectViewHolder;
        mContainerView.addView(mSelectView);
        return mSelectView;
    }
}
