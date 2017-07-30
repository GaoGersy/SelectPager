package com.gersion.selectpager.adapter;

import android.view.View;
import android.widget.ImageView;

import com.gersion.selectpager.IImageLoader;
import com.gersion.selectpager.R;
import com.gersion.selectpager.viewholder.BaseViewHolder;

import java.util.List;

/**
 * Created by Gersy on 2017/3/17.
 */

public class SelectIconRvAdapter<T> extends BaseRVAdapter {

    private IconViewHolder mIconViewHolder;
    private IImageLoader<T> mImageLoader;

    public SelectIconRvAdapter(List<T> data) {
        super(data);
    }

    public void add(T bean){
        mData.add(bean);
        notifyDataSetChanged();
    }

    public void remove(T bean){
        mData.remove(bean);
        notifyDataSetChanged();
    }

    public void setImageLoader(IImageLoader<T> imageLoader){
        this.mImageLoader = imageLoader;
    }

    public int getItemWidth(){
        return mIconViewHolder.getItemWidth();
    }

    public List<T> getList(){
        return mData;
    }

    @Override
    protected BaseViewHolder setViewHolder(View view) {
        mIconViewHolder = new IconViewHolder(view);
        return mIconViewHolder;
    }
    public void removeItem(T bean){
        mData.remove(bean);
        notifyDataSetChanged();
    }


    @Override
    protected int setResourseId() {
        return R.layout.item_icon;
    }

    class IconViewHolder extends BaseViewHolder<T>{

        private ImageView mIcon;

        public IconViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void initView(View itemView) {
            mIcon = (ImageView) itemView.findViewById(R.id.icon);

        }

        @Override
        public void setData(T bean) {
            mImageLoader.showImage(mContext,bean,mIcon);
        }

        public int getItemWidth(){
            return itemView.getMeasuredWidth();
        }
    }
}
