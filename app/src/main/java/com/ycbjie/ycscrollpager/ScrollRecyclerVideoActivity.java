package com.ycbjie.ycscrollpager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.yc.pagerlib.recycler.OnPagerListener;
import com.yc.pagerlib.recycler.PagerLayoutManager;

import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.util.ArrayList;
import java.util.List;

public class ScrollRecyclerVideoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onStop() {
        super.onStop();
        VideoPlayerManager.instance().releaseVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (VideoPlayerManager.instance().onBackPressed()){
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        recyclerView = findViewById(R.id.recyclerView);
        initRecyclerView();
    }

    private void initRecyclerView() {
        PagerLayoutManager viewPagerLayoutManager = new PagerLayoutManager(
                this, OrientationHelper.VERTICAL);
        List<Video> list = new ArrayList<>();
        for (int a = 0; a< DataProvider.VideoPlayerList.length ; a++){
            Video video = new Video(DataProvider.VideoPlayerTitle[a],
                    10,"",DataProvider.VideoPlayerList[a]);
            list.add(video);
        }
        final VideoAdapter adapter = new VideoAdapter(this,list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(viewPagerLayoutManager);
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
        recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                VideoPlayer videoPlayer = ((VideoAdapter.VideoViewHolder) holder).mVideoPlayer;
                if (videoPlayer == VideoPlayerManager.instance().getCurrentVideoPlayer()) {
                    VideoPlayerManager.instance().releaseVideoPlayer();
                }
            }
        });
    }



}
