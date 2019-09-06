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

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yc.pagerlib.recycler.OnPagerListener;

import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/7/20
 *     desc  : 动态管理的Adapter。
 *             每次都会创建新view，销毁旧View。节省内存消耗性能
 *     revise: 比如使用场景是启动引导页
 * </pre>
 */
public abstract class AbsPagerAdapter<T> extends PagerAdapter {

	private List<T> mDataList;

	public AbsPagerAdapter(List<T> dataList) {
		this.mDataList = dataList;
	}

	@Override
	public int getCount() {
		if (mDataList == null) {
			return 0;
		}
		return mDataList.size();
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == object;
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		View view = getView(container,position);
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		container.removeView((View) object);
	}

	/**
	 * 子类重写的方法
	 * @param container						container
	 * @param position						索引
	 * @return								返回view
	 */
	public abstract View getView(ViewGroup container, int position);


}
