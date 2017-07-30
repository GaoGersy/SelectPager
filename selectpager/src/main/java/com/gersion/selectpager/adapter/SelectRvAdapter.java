package com.gersion.selectpager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gersion.selectpager.viewholder.BaseViewHolder;
import com.jojo.sns.R;
import com.jojo.sns.base.BaseViewHolder;
import com.jojo.sns.bean.FriendBean;
import com.jojo.sns.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Gersy on 2017/3/17.
 */

public abstract class SelectRvAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<T> list;
    private String[] sections;
    private int mRightWidth = 0;
    private int mTotalCount = 0;
    private OnSelectItemChangeListener mListener;
    private String mFriendId;
    private boolean hasMaxSize = false;
    private int maxSize = 5;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_TITLE = 1;
    protected List<T> mData = new ArrayList<>();
    protected BaseViewHolder mViewHolder;
    private int mPosition;
    public Context mContext;
    private TextView mTvIndex;
    private String maxSizeTip;

    public SelectRvAdapter(List<T> data) {
        if (data != null && data.size() > 0) {
            mData.addAll(data);
        }
    }

    public void addAllData(List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setHasMaxSize(boolean hasMaxSize) {
        this.hasMaxSize = hasMaxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setMaxSizeTip(String tip) {
        this.maxSizeTip = tip;
    }

    public T getItem(int position) {
        return mData.get(position);
    }

//    public int getPositionForCategory(String category) {
//        for (int i = 0; i < getItemCount(); i++) {
//            String firstChar = getAlpha(getItem(i).firstletter);
//            if (TextUtils.equals(firstChar, category)) {
//                return i;
//            }
//        }
//        return -1;
//    }

    public List<T> getList() {
        return mData;
    }

    public void setTotalCount(int count) {
        mTotalCount = count;
    }

    public void changeAllDataStatus(boolean seleted) {
        for (T bean : mData) {
            if (!isFilter(bean)) {
                setSelected(bean,seleted);
            }
        }
        notifyDataSetChanged();
    }

    public abstract void setSelected(T t,boolean isSelected);
    public abstract boolean isFilter(T t);
    public abstract boolean isTitle(T t);

    private boolean isTitle(int pos) {
        if (isTitle(mData.get(pos))) {
            return true;
        }
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case TYPE_NORMAL:
                View vImage = mInflater.inflate(R.layout.item_select_circle, parent, false);
                SupportCicleViewHolder itemHolder = new SupportCicleViewHolder(vImage);
                return itemHolder;
            case TYPE_TITLE:
                View vGroup = mInflater.inflate(R.layout.item_index_name, parent, false);
                TitleViewHolder titleViewHolder = new TitleViewHolder(vGroup);
                return titleViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_NORMAL:
                SupportCicleViewHolder itemHolder = (SupportCicleViewHolder) holder;
                itemHolder.setData(mData.get(position));
                break;
            case TYPE_TITLE:
                TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
                titleViewHolder.setData(mData.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (!isTitle(position)) {
            viewType = TYPE_NORMAL;
        } else {
            viewType = TYPE_TITLE;
        }
        return viewType;
    }

    public void updateItem(T bean) {
        for (T friendDataBean : mData) {
            if (friendDataBean == bean) {
                setSelected(friendDataBean,false);
            }
        }
        notifyDataSetChanged();
    }

    class SupportCicleViewHolder extends BaseViewHolder<T> {
        private CheckBox mSelectMember;
        private LinearLayout mItem;
        private TextView mName;
        private ImageView mIcon;
        private T mBean;

        public SupportCicleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void initView(View itemView) {
            mName = (TextView) itemView.findViewById(R.id.tv_name);
            mIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            mSelectMember = (CheckBox) itemView.findViewById(R.id.cb_selectmember);
            mItem = (LinearLayout) itemView.findViewById(R.id.item);
            mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeCheckboxState();
                }
            });

        }

        @Override
        public void setData(T bean) {
            convert(bean,)
            mBean = bean;
            mName.setText(bean.friendAlias);
            mSelectMember.setChecked(bean.isChecked);

            if (bean.isFiltered) {
                mItem.setEnabled(false);
                mItem.setClickable(false);
                mSelectMember.setBackgroundResource(R.mipmap.geycheck);
            } else {
                mItem.setEnabled(true);
                mItem.setClickable(true);
//                if (hasMaxSize) {//如果有最多选择数的限制
//                    if (mTotalCount >= maxSize) {
//                        if (!bean.isChecked) {
//                            mItem.setEnabled(false);
//                            mItem.setClickable(false);
//                        } else {
//                            mItem.setEnabled(true);
//                            mItem.setClickable(true);
//                        }
//                    } else {
//                        mItem.setEnabled(true);
//                        mItem.setClickable(true);
//                    }
//                }
                mSelectMember.setBackgroundResource(R.drawable.btn_check);
            }

            String url = bean.icon;
            GlideUtils.loadCircleImage(SelectRvAdapter.this.mContext, url, mIcon);
        }

        public void changeCheckboxState() {
            if (hasMaxSize) {
                changeAllDataStatus(false);
//                if (mTotalCount > maxSize) {
//                    ToastUtil.show(mContext, maxSizeTip);
//                    return;
//                }
            }
            mBean.isChecked = !mBean.isChecked;
            if (mListener == null) {
                return;
            }
            if (mBean.isChecked) {
                mTotalCount++;
                mListener.onAdd(mBean);
            } else {
                mTotalCount--;
                mListener.onRemove(mBean);
            }

            notifyDataSetChanged();
        }
    }

    class TitleViewHolder extends BaseViewHolder<T> {

        private TextView mTextView;

        public TitleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void initView(View itemView) {
            mTextView = (TextView) itemView.findViewById(R.id.tv_item_index_name);
        }

        @Override
        public void setData(T friendDataBean) {
            mTextView.setText(friendDataBean.firstletter);
        }
    }

    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else {
            return "#";
        }
    }


    public void setOnSelectItemChangeListener(OnSelectItemChangeListener listener) {
        mListener = listener;
    }

    public interface OnSelectItemChangeListener {
        void onAdd(T bean);

        void onRemove(T bean);
    }
}
