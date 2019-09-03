package com.yc.pagerlib.pager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.yc.pagerlib.inter.OnPagerListener;

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
	private OnPagerListener mOnViewPagerListener;
	private int startPosition;

	public AbsPagerAdapter(List<T> dataList) {
		this.mDataList = dataList;
	}

	/**
	 * 设置监听
	 * @param listener                      listener
	 */
	public void setOnViewPagerListener(OnPagerListener listener){
		this.mOnViewPagerListener = listener;
		listener.onInitComplete();
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
		this.startPosition = position;
		View view = getView(container,position);
		if (mOnViewPagerListener!=null){
			mOnViewPagerListener.onPageSelected(position,true);
		}
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		if (startPosition>position){
			if (mOnViewPagerListener!=null){
				mOnViewPagerListener.onPageRelease(true , position);
			}
		} else {
			if (mOnViewPagerListener!=null){
				mOnViewPagerListener.onPageRelease(false , position);
			}
		}
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
