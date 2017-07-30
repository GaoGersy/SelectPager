package com.gersion.selectpager;

/**
 * Created by a3266 on 2017/5/29.
 */

public interface IMatch<T> {
    boolean isMatch(T t,String condition);
}
