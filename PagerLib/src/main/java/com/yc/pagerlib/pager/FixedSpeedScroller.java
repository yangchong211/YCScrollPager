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

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/6/20
 *     desc  : 自定义Scroller，控制滚动速率
 *     revise:
 * </pre>
 */
public class FixedSpeedScroller extends Scroller {

    private int mDuration = 1500;
    private long mRecentTouchTime;

    FixedSpeedScroller(Context context, Interpolator interpolator, long touchTime) {
        super(context, interpolator);
        this.mRecentTouchTime = touchTime;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // 如果手工滚动,则加速滚动
        int time = 2000;
        if (System.currentTimeMillis() - mRecentTouchTime > time) {
            duration = mDuration;
        } else {
            duration /= 2;
        }
        super.startScroll(startX, startY, dx, dy, duration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    void setDuration(int time) {
        mDuration = time;
    }

}
