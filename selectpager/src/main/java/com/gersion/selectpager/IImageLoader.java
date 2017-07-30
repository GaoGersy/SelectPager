package com.gersion.selectpager;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by a3266 on 2017/5/28.
 */

public interface IImageLoader<T> {
    void showImage(Context context, T bean, ImageView imageView);
}
