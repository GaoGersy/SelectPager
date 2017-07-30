package com.gersion.selectpager.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gersion.selectpager.OnItemClickListener;


/**
 * @作者 Gersy
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    private OnItemClickListener mListener;
    private View mItemView;

    public BaseViewHolder(View itemView) {
        this(itemView,null);
    }
    public BaseViewHolder(View itemView, OnItemClickListener listener) {
        super(itemView);
        mListener = listener;
        mItemView = itemView;
        initView(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mListener!=null) {
            mListener.onItemClick(view, getAdapterPosition());
        }
    }

    protected View findViewById(int resouceId){
       return mItemView.findViewById(resouceId);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    protected abstract void initView(View itemView);

    public abstract void setData(T t);
}