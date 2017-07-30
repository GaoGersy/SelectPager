package com.gersion.selectpager.builder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.gersion.selectpager.IFilter;
import com.gersion.selectpager.selectView.SelectView;
import com.gersion.selectpager.viewholder.BaseSelectViewHolder;

public class SelectPagerBuilder {
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

    public SelectPagerBuilder with(Context context) {
        mContext = context;
        return this;
    }

    public SelectPagerBuilder setContanerView(ViewGroup containerView) {
        mContainerView = containerView;
        return this;
    }

    public SelectPagerBuilder filter(IFilter filter) {
        mFilter = filter;
        return this;
    }

    public SelectPagerBuilder titleView(View titleView) {
        mTitleView = titleView;
        return this;
    }

    public SelectPagerBuilder bottomView(View bottmonView) {
        mBottmonView = bottmonView;
        return this;
    }

    public SelectPagerBuilder enableTitle(boolean enableTitle) {
        mEnableTitle = enableTitle;
        return this;
    }

    public SelectPagerBuilder showDivider(boolean showDivider) {
        mShowDivider = showDivider;
        return this;
    }

    public SelectPagerBuilder dividerColor(int dividerColor) {
        if (mContext == null) {
            throw new NullPointerException("先调用with()传入context");
        }
        mDividerColor = mContext.getResources().getColor(dividerColor);
        return this;
    }

    public SelectPagerBuilder enableAnimation(int enableAnimation) {
        mEnableAnimation = enableAnimation;
        return this;
    }

    public SelectPagerBuilder setViewHolder(BaseSelectViewHolder selectViewHolder) {
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