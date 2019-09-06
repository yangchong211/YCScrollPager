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
package com.yc.pagerlib.recycler;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/7/23
 *     desc  : 自定义LinearLayoutManager
 *     revise:
 * </pre>
 */
public interface OnPagerListener {

    /**
     * 初始化完成
     */
    void onInitComplete();

    /**
     * 释放的监听
     * @param isNext                    是否下一个，true表示下一个，false表示上一个
     * @param position                  索引
     */
    void onPageRelease(boolean isNext, int position);

    /***
     * 选中的监听以及判断是否滑动到底部
     * @param position                  索引
     * @param isBottom                  是否到了底部
     */
    void onPageSelected(int position, boolean isBottom);


}
