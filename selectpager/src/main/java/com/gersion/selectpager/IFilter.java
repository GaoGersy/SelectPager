package com.gersion.selectpager;

/**
 * Created by a3266 on 2017/5/28.
 */

public interface IFilter<T> {
    boolean onSelected(T t);
    boolean onFilter(T t);
}
