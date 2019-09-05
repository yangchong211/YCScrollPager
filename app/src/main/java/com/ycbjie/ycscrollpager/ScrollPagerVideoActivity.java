package com.ycbjie.ycscrollpager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yc.pagerlib.inter.OnPagerListener;
import com.yc.pagerlib.pager.AbsPagerAdapter;
import com.yc.pagerlib.pager.DirectionalViewPager;

import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.util.ArrayList;
import java.util.List;


public class ScrollPagerVideoActivity extends AppCompatActivity {

    private DirectionalViewPager vp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager2);
        vp = findViewById(R.id.vp);
        initViewPager();
    }

    private void initViewPager() {
        List<Video> list = new ArrayList<>();
        for (int a = 0; a< DataProvider.VideoPlayerList.length ; a++){
            Video video = new Video(DataProvider.VideoPlayerTitle[a],
                    10,"",DataProvider.VideoPlayerList[a]);
            list.add(video);
        }
        vp.setOffscreenPageLimit(1);
        vp.setCurrentItem(0);
        vp.setOrientation(DirectionalViewPager.VERTICAL);
        final BannerPagerAdapter adapter = new BannerPagerAdapter(this,list);
        vp.setAdapter(adapter);
        vp.setAnimationDuration(3000);
        adapter.setOnViewPagerListener(new OnPagerListener() {
            @Override
            public void onInitComplete() {
                System.out.println("OnPagerListener---onInitComplete--"+"初始化完成");
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                System.out.println("OnPagerListener---onPageRelease--"+position+"-----"+isNext);
                //VideoPlayer videoPlayer = ((VideoAdapter.VideoViewHolder) holder).mVideoPlayer;
//                VideoPlayer videoPlayer = adapter.videoPlayer;
//                if (videoPlayer == VideoPlayerManager.instance().getCurrentVideoPlayer()) {
//                    VideoPlayerManager.instance().releaseVideoPlayer();
//                }
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                System.out.println("OnPagerListener---onPageSelected--"+position+"-----"+isBottom);
//                adapter.setController();
            }
        });
    }

    class BannerPagerAdapter extends AbsPagerAdapter {

        private List<Video> videos;
        private VideoPlayerController controller;
        private VideoPlayer videoPlayer;

        public BannerPagerAdapter(Context context, List<Video> dataList) {
            super(dataList);
            this.videos = dataList;
            //创建视频播放控制器，主要只要创建一次就可以呢
            controller = new VideoPlayerController(context);
        }

        /**
         * 设置视频控制器参数
         */
        void setController() {
            //只设置一次
            if (videoPlayer!=null){
                videoPlayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_IJK);
                videoPlayer.setController(controller);
            }
        }

        @Override
        public View getView(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(
                    R.layout.item_video_pager, container, false);
            videoPlayer = view.findViewById(R.id.video_player);
            Video video = videos.get(position);

            //controller.setTitle(video.getTitle());
            //mController.setLength(video.getLength());
            ImageUtils.loadImgByPicasso(view.getContext(),video.getImageUrl(),
                    R.drawable.image_default,controller.imageView());
            //videoPlayer.setUp(video.getVideoUrl(), null);
            return view;
        }

        @Override
        public int getCount() {
            return videos.size();
        }
    }
}
