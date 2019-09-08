# YCScrollPager目录介绍
#### 目录介绍
- 01.先来看一下需求
- 02.有几种实现方式
    - 2.1 使用ViewPager
    - 2.2 使用RecyclerView
- 03.具体使用
    - 3.0 如何引用该lib
    - 3.1 ViewPager实现方式
    - 3.2 RecyclerView实现方式
- 04.部分优化点
    - 4.1 ViewPager改变滑动速率
    - 4.2 PagerSnapHelper注意点
    - 4.3 自定义LayoutManager注意点
- 06.其他说明介绍



### 00.先来看一下效果图
- ![image](https://img-blog.csdnimg.cn/20190908214327374.gif)


### 01.先来看一下需求
- 项目中的视频播放，要求实现抖音那种竖直方向一次滑动一页的效果。滑动要流畅不卡顿，并且手动触摸滑动超过1/2的时候松开可以滑动下一页，没有超过1/2返回原页。
- 手指拖动页面滑动，只要没有切换到其他的页面，视频都是在播放的。切换了页面，上一个视频销毁，该页面则开始初始化播放。
- 切换页面的时候过渡效果要自然，避免出现闪屏。具体的滑动效果，可以直接参考抖音……



### 02.有几种实现方式
#### 2.1 使用ViewPager
- 使用ViewPager实现竖直方法上下切换视频分析
    - 1.最近项目需求中有用到需要在ViewPager中播放视频，就是竖直方法上下滑动切换视频，视频是网络视频，最开始的实现思路是ViewPager中根据当前item位置去初始化SurfaceView，同时销毁时根据item的位置移除SurfaceView。
    - 2.上面那种方式确实是可以实现的，但是存在2个问题，第一，MediaPlayer的生命周期不容易控制并且存在内存泄漏问题。第二，连续三个item都是视频时，来回滑动的过程中发现会出现上个视频的最后一帧画面的bug。
    - 3.未提升用户体验，视频播放器初始化完成前上面会覆盖有该视频的第一帧图片,但是发现存在第一帧图片与视频第一帧信息不符的情况，后面会通过代码给出解决方案。
- 大概的实现思路是这样
    - 1.需要自定义一个竖直方向滑动的ViewPager，注意这个特别重要。
    - 2.一次滑动一页，建议采用ViewPager+FragmentStatePagerAdapter+Fragment方式来做，后面会详细说。
    - 3.视频的初始化逻辑和销毁逻辑优化，性能和滑动流畅度分析和探讨。
- 不太建议使用ViewPager
    - 1.ViewPager 自带的滑动效果完全满足场景，而且支持Fragment和View等UI绑定，只要对布局和触摸事件部分作一些修改，就可以把横向的 ViewPager 改成竖向。
    - 2.但是没有复用是个最致命的问题。在onLayout方法中，所有子View会实例化并一字排开在布局上。当Item数量很大时，将会是很大的性能浪费。
    - 3.其次是可见性判断的问题。很多人会以为 Fragment 在 onResume 的时候就是可见的，而 ViewPager 中的 Fragment 就是个反例，尤其是多个 ViewPager 嵌套时，会同时有多个父 Fragment 多个子 Fragment 处于 onResume 的状态，却只有其中一个是可见的。除非放弃 ViewPager 的预加载机制。在页面内容曝光等重要的数据上报时，就需要判断很多条件：onResumed 、 setUserVisibleHint 、 setOnPageChangeListener 等。



#### 2.2 使用RecyclerView
- 使用RecyclerView实现树枝方向上下切换视频分析
    - 1.首先RecyclerView它设置竖直方向滑动是十分简单的，同时关于item的四级缓存也做好了处理，而且滑动的效果相比ViewPager要好一些。
    - 2.滑动事件处理比viewPager好，即使你外层潜逃了下拉刷新上拉加载的布局，也不影响后期事件冲突处理，详细可以看demo案例。
- 大概的实现思路是这样
    - 1.自定义一个LinearLayoutManager，重写onScrollStateChanged方法，注意是拿到滑动状态。
    - 2.一次滑动切换一个页面，可以使用PagerSnapHelper来实现，十分方便简单。
    - 3.在recyclerView对应的adapter中，在onCreateViewHolder初始化视频操作，同时当onViewRecycled时，销毁视频资源。


### 03.具体使用
#### 3.0 如何引用该lib
- 直接添加依赖即可
```
implementation 'com.yc:PagerLib:1.0.1'
```



#### 3.1 ViewPager实现方式
##### 3.1.1 使用VerticalViewPager的方式如下所示
- 在布局中
    ```
    <com.yc.pagerlib.pager.VerticalViewPager
        android:id="@+id/vp"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
    ```
- 在activity中，这里是Viewpager+FragmentStatePagerAdapter+Fragment
    ```
    private void initViewPager() {
        List<Video> list = new ArrayList<>();
        ArrayList<Fragment> fragments = new ArrayList<>();
        //数据源注意，使用的时候替换成你的，这里设置一些假数据
        for (int a = 0; a< DataProvider.VideoPlayerList.length ; a++){
            Video video = new Video(DataProvider.VideoPlayerTitle[a],
                    10,"",DataProvider.VideoPlayerList[a]);
            list.add(video);
            fragments.add(VideoFragment.newInstant(DataProvider.VideoPlayerList[a]));
        }
        vp.setOffscreenPageLimit(1);
        vp.setCurrentItem(0);
        vp.setOrientation(DirectionalViewPager.VERTICAL);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(fragments, supportFragmentManager);
        vp.setAdapter(myPagerAdapter);
    }
    
    
    class MyPagerAdapter extends FragmentStatePagerAdapter{
    
        private ArrayList<Fragment> list;
    
        public MyPagerAdapter(ArrayList<Fragment> list , FragmentManager fm){
            super(fm);
            this.list = list;
        }
    
        @Override
        public Fragment getItem(int i) {
            return list.get(i);
        }
    
        @Override
        public int getCount() {
            return list!=null ? list.size() : 0;
        }
    }
    ```
- 在fragment中做视频播放相关操作
    ```
    @Override
    public void onStop() {
        super.onStop();
        VideoPlayerManager.instance().releaseVideoPlayer();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videoPlayer = view.findViewById(R.id.video_player);
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("初始化操作","------"+index++);
        VideoPlayerController controller = new VideoPlayerController(getActivity());
        videoPlayer.setUp(url,null);
        videoPlayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_IJK);
        videoPlayer.setController(controller);
        ImageUtils.loadImgByPicasso(getActivity(),"",
                R.drawable.image_default,controller.imageView());
    }
    ```


#### 3.2 RecyclerView实现方式
- 代码中直接使用PagerLayoutManager，如下所示
    ```
    PagerLayoutManager viewPagerLayoutManager = new PagerLayoutManager(
            this, OrientationHelper.VERTICAL);
    recyclerView.setLayoutManager(viewPagerLayoutManager);
    ```
- 设置layoutManager初始化和销毁相关的监听
    ```
    viewPagerLayoutManager.setOnViewPagerListener(new OnPagerListener() {
        @Override
        public void onInitComplete() {
            System.out.println("OnPagerListener---onInitComplete--"+"初始化完成");
        }
    
        @Override
        public void onPageRelease(boolean isNext, int position) {
            System.out.println("OnPagerListener---onPageRelease--"+position+"-----"+isNext);
        }
    
        @Override
        public void onPageSelected(int position, boolean isBottom) {
            System.out.println("OnPagerListener---onPageSelected--"+position+"-----"+isBottom);
        }
    });
    ```


### 04.部分优化点
#### 4.1 ViewPager改变滑动速率
- 可以通过反射修改属性，注意，使用反射的时候，建议手动try-catch，避免异常导致崩溃。代码如下所示：
    ```
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
    ```



#### 4.2 PagerSnapHelper注意点
- 好多时候会抛出一个异常"illegalstateexception an instance of onflinglistener already set".
- 看SnapHelper源码attachToRecyclerView(xxx)方法时，可以看到如果recyclerView不为null，则先destoryCallback()，它作用在于取消之前的RecyclerView的监听接口。然后通过setupCallbacks()设置监听器，如果当前RecyclerView已经设置了OnFlingListener，会抛出一个状态异常。那么这个如何复现了，很容易，你初始化多次就可以看到这个bug。
- 建议手动捕获一下该异常，代码设置如下所示。源码中判断了，如果onFlingListener已经存在的话，再次设置就直接抛出异常，那么这里可以增强一下逻辑判断，ok，那么问题便解决呢！
    ```
    try {
        //attachToRecyclerView源码上的方法可能会抛出IllegalStateException异常，这里手动捕获一下
        RecyclerView.OnFlingListener onFlingListener = mRecyclerView.getOnFlingListener();
        //源码中判断了，如果onFlingListener已经存在的话，再次设置就直接抛出异常，那么这里可以判断一下
        if (onFlingListener==null){
            mPagerSnapHelper.attachToRecyclerView(mRecyclerView);
        }
    } catch (IllegalStateException e){
        e.printStackTrace();
    }
    ```

#### 4.3 自定义LayoutManager注意点
- 网上有人已经写了一篇自定义LayoutManager实现抖音的效果的博客，我自己也很仔细看了这篇文章。不过我觉得有几个注意要点，因为要用到线上app，则一定要尽可能减少崩溃率……
- 通过SnapHelper调用findSnapView方法，得到的view，一定要增加非空判断逻辑，否则很容易造成崩溃。
- 在监听滚动位移scrollVerticallyBy的时候，注意要增加判断，就是getChildCount()如果为0时，则需要返回0。
- 在onDetachedFromWindow调用的时候，可以把listener监听事件给remove掉。


### 06.其他说明介绍
#### 关于其他内容介绍
![image](https://upload-images.jianshu.io/upload_images/4432347-7100c8e5a455c3ee.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 关于博客汇总链接
- 1.[技术博客汇总](https://www.jianshu.com/p/614cb839182c)
- 2.[开源项目汇总](https://blog.csdn.net/m0_37700275/article/details/80863574)
- 3.[生活博客汇总](https://blog.csdn.net/m0_37700275/article/details/79832978)
- 4.[喜马拉雅音频汇总](https://www.jianshu.com/p/f665de16d1eb)
- 5.[其他汇总](https://www.jianshu.com/p/53017c3fc75d)


#### 其他推荐
- 博客笔记大汇总【15年10月到至今】，包括Java基础及深入知识点，Android技术博客，Python学习笔记等等，还包括平时开发中遇到的bug汇总，当然也在工作之余收集了大量的面试题，长期更新维护并且修正，持续完善……开源的文件是markdown格式的！同时也开源了生活博客，从12年起，积累共计47篇[近20万字]，转载请注明出处，谢谢！
- 链接地址：https://github.com/yangchong211/YCBlogs
- 如果觉得好，可以star一下，谢谢！当然也欢迎提出建议，万事起于忽微，量变引起质变！



#### 关于LICENSE
```
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
```




