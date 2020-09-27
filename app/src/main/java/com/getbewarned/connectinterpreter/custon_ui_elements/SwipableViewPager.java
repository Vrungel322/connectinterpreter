package com.getbewarned.connectinterpreter.custon_ui_elements;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SwipableViewPager extends ViewPager {

    private boolean isSwipeEnabled = true;

    public SwipableViewPager(Context context) {
        super(context);
    }

    public SwipableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isSwipeEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isSwipeEnabled && super.onInterceptTouchEvent(event);
    }

    public void setSwipeEnabled(boolean b) {
        this.isSwipeEnabled = b;
    }
}