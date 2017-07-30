package com.gersion.selectpager.selectView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gersion.selectpager.IFilter;
import com.gersion.selectpager.IMatch;
import com.gersion.selectpager.R;
import com.gersion.selectpager.adapter.SelectIconRvAdapter;
import com.gersion.selectpager.adapter.SelectRvAdapter;
import com.gersion.selectpager.viewholder.BaseSelectViewHolder;

import java.util.List;

/**
 * Created by a3266 on 2017/5/28.
 */

public class SelectView<T> extends FrameLayout {
    public IFilter mFilter;
    public View mTitleView;
    public View mBottmonView;
    public boolean mEnableTitle;
    public boolean mShowDivider;
    public boolean mEnableSearch;
    public int mDividerColor;
    public int mEnableAnimation;
    public BaseSelectViewHolder mSelectViewHolder;
    private View mView;
    private RecyclerView mContentRecyclerView;
    private TextView mTvError;
    private ImageView mIvError;
    private FrameLayout mFlCover;
    private TextView mTvConfirm;
    private LinearLayout mLlSelectAll;
    private EditText mEtSearch;
    private LinearLayout mContainer;
    private RecyclerView mIconRecyclerView;
    private IMatch mMatch;
    private List<T> mDatas;
    private List<T> mMatchList;
    private SelectIconRvAdapter mIconListRvAdapter;
    private SelectRvAdapter mSelectRvAdapter;
    private List<T> mSelectList;
    private boolean isSingleSelect;
    private ImageView mIvSelectAll;

    public SelectView(@NonNull Context context) {
        this(context,null);
    }

    public SelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mView = LayoutInflater.from(context).inflate(R.layout.view_select_view,this);
        initView();
        initListener();
    }

    private <E extends View> E findView(int id){
        return (E) mView.findViewById(id);
    }

    private void initView() {
        mContentRecyclerView = findView(R.id.content_recyclerView);
        mIconRecyclerView = findView(R.id.icon_recyclerView);
        mContainer = findView(R.id.container);
        mEtSearch = findView(R.id.et_search);
        mLlSelectAll = findView(R.id.ll_select_all);
        mIvSelectAll = findView(R.id.iv_select_all);
        mTvConfirm = findView(R.id.tv_confirm);
        mFlCover = findView(R.id.fl_cover);
        mIvError = findView(R.id.iv_error);
        mTvError = findView(R.id.tv_error);

        mEtSearch.setVisibility(mEnableSearch?VISIBLE:GONE);
        initRecycleView();
    }

    private void initRecycleView() {
        mIconListRvAdapter = new SelectIconRvAdapter(null);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setAutoMeasureEnabled(true);

        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mIconRecyclerView.setLayoutManager(mLinearLayoutManager);
        mIconRecyclerView.setAdapter(mIconListRvAdapter);

        mSelectRvAdapter = new SelectRvAdapter(null);
        LinearLayoutManager supportLinearLayoutManager = new LinearLayoutManager(this);
        supportLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mContentRecyclerView.setLayoutManager(supportLinearLayoutManager);
        mContentRecyclerView.setAdapter(mSelectRvAdapter);

        mSelectList = mIconListRvAdapter.getList();
//        TitleItemDecoration decoration = new TitleItemDecoration();
//        mContentRecyclerView.addItemDecoration(decoration);

        if (isSingleSelect) {
            mLlSelectAll.setVisibility(View.GONE);
        } else {
            mLlSelectAll.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResultList.size() > 0) {
                    mResultList.clear();
                }
                for (FriendBean.FriendDataBean friendDataBean : mSelectList) {
                    mResultList.add(friendDataBean.friendId);
                }
                onSelected(mResultList);
            }
        });

        mIndexView.setTipTv(mTvIndexviewTip);
        mIndexView.setDelegate(new IndexView.Delegate() {
            @Override
            public void onIndexViewSelectedChanged(IndexView indexView, String text) {
                int position = mSelectRvAdapter.getPositionForCategory(text);
                if (position != -1) {
                    mRecyclerViewScrollHelper.smoothScrollToPosition(position);
                }
            }
        });

        mLlSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIvSelectAll.isSelected()) {
                    mIconListRvAdapter.addAllData(mDatas);
                } else {
                    mIconListRvAdapter.clear();
                }
                mSelectRvAdapter.changeAllDataStatus(!mIvSelectAll.isSelected());
                refreshLayout(!mIvSelectAll.isSelected());
            }
        });

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = s.toString();
                mMatchList.clear();
                if (TextUtils.isEmpty(content)) {
                    mSelectRvAdapter.addAllData(mDatas);
                } else {
                    for (T bean : mDatas) {
                        if (mMatch.isMatch(bean,content)) {
                            mMatchList.add(bean);
                        }
                    }
                    mSelectRvAdapter.addAllData(mMatchList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (isSingleSelect) {
            mSelectRvAdapter.setHasMaxSize(true);
            mSelectRvAdapter.setMaxSize(1);
            mSelectRvAdapter.setOnSelectItemChangeListener(new SelectRvAdapter.OnSelectItemChangeListener() {
                @Override
                public void onAdd(FriendBean.FriendDataBean bean) {
                    mIconListRvAdapter.clear();
                    mIconListRvAdapter.add(bean);
                    refreshLayout(mIvSelectAll.isSelected());
                }

                @Override
                public void onRemove(FriendBean.FriendDataBean bean) {
                    mIconListRvAdapter.clear();
                    refreshLayout(false);
                }
            });

            mIconListRvAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    FriendBean.FriendDataBean friendDataBean = mSelectList.get(position);
                    mIconListRvAdapter.removeItem(friendDataBean);
                    mSelectRvAdapter.updateItem(friendDataBean);
                    refreshLayout(false);
                }
            });
        } else {
            mSelectRvAdapter.setHasMaxSize(false);
            mSelectRvAdapter.setOnSelectItemChangeListener(new SelectRvAdapter.OnSelectItemChangeListener() {
                @Override
                public void onAdd(FriendBean.FriendDataBean bean) {
                    mIconListRvAdapter.add(bean);
                    refreshLayout(mIvSelectAll.isSelected());
                }

                @Override
                public void onRemove(FriendBean.FriendDataBean bean) {
                    mIconListRvAdapter.remove(bean);
                    refreshLayout(false);
                }
            });

            mIconListRvAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    FriendBean.FriendDataBean friendDataBean = mSelectList.get(position);
                    mIconListRvAdapter.removeItem(friendDataBean);
                    mSelectRvAdapter.updateItem(friendDataBean);
                    refreshLayout(false);
                }
            });
        }
    }

    public void setSearchHint(String hint){
        mEtSearch.setHint(hint);
    }

    public void setSearchHintColor(int color){
        mEtSearch.setHintTextColor(color);
    }

    private void refreshLayout(boolean isSelected) {
        mIvSelectAll.setSelected(isSelected);
        int size = mSelectList.size();
        if (size == 0) {
            mTvConfirm.setText("确定");
            mTvConfirm.setEnabled(false);
        } else {
            mTvConfirm.setText("确定(" + size + ")");
            mTvConfirm.setEnabled(true);
        }
//        int width = SizeUtils.dp2px(this, 45) * size;
//        if (width > mMaxWidth) {//只能是屏幕宽度的3/4
//            width = mMaxWidth;
//        }
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
//        mIconRecyclerView.setLayoutParams(params);
//        mContainer.requestLayout();
    }
}
