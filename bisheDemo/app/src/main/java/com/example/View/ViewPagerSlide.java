package com.example.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerSlide extends ViewPager {

    //重写ViewPager 取消左右滑动
    private boolean isSlide=false;

    public void setSlide(boolean slide) {
        this.isSlide = slide;
    }

    public ViewPagerSlide(Context context) {
        super(context);
    }

    public ViewPagerSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return isSlide;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev){

        return isSlide;
    }
   /* @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false);  //取消动画
    }*/

}