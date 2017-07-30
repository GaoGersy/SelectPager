package com.gersion.selectpager.builder;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by a3266 on 2017/5/28.
 */

public class ItemBuilder {
    private Context mContext;
    private Drawable mDrawable;
    private int mWidth;
    private int mHeight;

    public ItemBuilder with(Context context){
        mContext = context;
        return this;
    }
    public ItemBuilder setCheckBox(int drawableId) {
        if (mContext==null){
            throw new NullPointerException("先调用with()传入context");
        }
        mDrawable = mContext.getResources().getDrawable(drawableId);
        return this;
    }

    public ItemBuilder setCheckBox(Drawable drawable) {
        mDrawable = drawable;
        return this;
    }

    public ItemBuilder iconWidth(int width) {
        mWidth = width;
        return this;
    }

    public ItemBuilder iconHeight(int height) {
        mHeight = height;
        return this;
    }
}
