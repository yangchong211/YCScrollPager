/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.pagerlib.pager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/6/20
 *     desc  : 自定义ViewPager，主要是处理边界极端情况
 *     revise:
 * </pre>
 */
public class VerticalViewPager extends ViewPager {

    private boolean isVertical = false;
    private long mRecentTouchTime;

    public VerticalViewPager(@NonNull Context context) {
        super(context);
    }

    public VerticalViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        setPageTransformer(true, new HorizontalVerticalPageTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public boolean isVertical() {
        return isVertical;
    }

    public void setVertical(boolean vertical) {
        isVertical = vertical;
        init();
    }

    private class HorizontalVerticalPageTransformer implements PageTransformer {

        private static final float MIN_SCALE = 0.25f;

        @Override
        public void transformPage(@NonNull View page, float position) {
            if (isVertical) {
                if (position < -1) {
                    page.setAlpha(0);
                } else if (position <= 1) {
                    page.setAlpha(1);
                    // Counteract the default slide transition
                    float xPosition = page.getWidth() * -position;
                    page.setTranslationX(xPosition);
                    //set Y position to swipe in from top
                    float yPosition = position * page.getHeight();
                    page.setTranslationY(yPosition);
                } else {
                    page.setAlpha(0);
                }
            } else {
                int pageWidth = page.getWidth();
                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setAlpha(0);
                } else if (position <= 0) { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    page.setAlpha(1);
                    page.setTranslationX(0);
                    page.setScaleX(1);
                    page.setScaleY(1);
                } else if (position <= 1) { // (0,1]
                    // Fade the page out.
                    page.setAlpha(1 - position);
                    // Counteract the default slide transition
                    page.setTranslationX(pageWidth * -position);
                    page.setTranslationY(0);
                    // Scale the page down (between MIN_SCALE and 1)
                    float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.setAlpha(0);
                }
            }
        }
    }

    /**
     * 交换x轴和y轴的移动距离
     * @param event 获取事件类型的封装类MotionEvent
     */
    private MotionEvent swapXY(MotionEvent event) {
        //获取宽高
        float width = getWidth();
        float height = getHeight();
        //将Y轴的移动距离转变成X轴的移动距离
        float swappedX = (event.getY() / height) * width;
        //将X轴的移动距离转变成Y轴的移动距离
        float swappedY = (event.getX() / width) * height;
        //重设event的位置
        event.setLocation(swappedX, swappedY);
        return event;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mRecentTouchTime = System.currentTimeMillis();
        if (getCurrentItem() == 0 && getChildCount() == 0) {
            return false;
        }
        if (isVertical) {
            boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
            swapXY(ev);
            // return touch coordinates to original reference frame for any child views
            return intercepted;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getCurrentItem() == 0 && getChildCount() == 0) {
            return false;
        }
        if (isVertical) {
            return super.onTouchEvent(swapXY(ev));
        } else {
            return super.onTouchEvent(ev);
        }
    }


    /**
     * 设置viewPager滑动动画持续时间
     * API>19
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setAnimationDuration(final int during){
        try {
            // viewPager平移动画事件
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            // 动画效果与ViewPager的一致
            Interpolator interpolator = new Interpolator() {
                @Override
                public float getInterpolation(float t) {
                    t -= 1.0f;
                    return t * t * t * t * t + 1.0f;
                }
            };
            Scroller mScroller = new Scroller(getContext(),interpolator){
                final int time = 2000;
                @Override
                public void startScroll(int x, int y, int dx, int dy, int duration) {
                    // 如果手工滚动,则加速滚动
                    if (System.currentTimeMillis() - mRecentTouchTime > time) {
                        duration = during;
                    } else {
                        duration /= 2;
                    }
                    super.startScroll(x, y, dx, dy, duration);
                }

                @Override
                public void startScroll(int x, int y, int dx, int dy) {
                    super.startScroll(x, y, dx, dy,during);
                }
            };
            mField.set(this, mScroller);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改滑动灵敏度
     * @param flingDistance                     滑动惯性，默认是75
     * @param minimumVelocity                   最小滑动值，默认是1200
     */
    public void setScrollFling(int flingDistance , int minimumVelocity){
        try {
            Field mFlingDistance = ViewPager.class.getDeclaredField("mFlingDistance");
            mFlingDistance.setAccessible(true);
            Object o = mFlingDistance.get(this);
            Log.d("setScrollFling",o.toString());
            //默认值75
            mFlingDistance.set(this, flingDistance);

            Field mMinimumVelocity = ViewPager.class.getDeclaredField("mMinimumVelocity");
            mMinimumVelocity.setAccessible(true);
            Object o1 = mMinimumVelocity.get(this);
            Log.d("setScrollFling",o1.toString());
            //默认值1200
            mMinimumVelocity.set(this,minimumVelocity);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setCurrentItem(int item) {
        /*
         * true表示有过渡效果，false表示直接切换到对应页面，给人感觉是会闪烁一下
         */
        setCurrentItem(item,true);
    }
}
