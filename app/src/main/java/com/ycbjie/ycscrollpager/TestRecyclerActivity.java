package com.ycbjie.ycscrollpager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;

import com.yc.cn.ycbannerlib.snap.ScrollPageHelper;

import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yc
 */
public class TestRecyclerActivity extends BaseActivity {


    RecyclerView recyclerView;

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
    public int getContentView() {
        return R.layout.activity_recyclerview;
    }

    @Override
    public void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        List<Video> list = new ArrayList<>();
        for (int a = 0; a< DataProvider.VideoPlayerList.length ; a++){
            Video video = new Video(DataProvider.VideoPlayerTitle[a],
                    10,"",DataProvider.VideoPlayerList[a]);
            list.add(video);
        }
        TestVideoAdapter adapter = new TestVideoAdapter(this, list);
        recyclerView.setAdapter(adapter);
        ScrollPageHelper snapHelper = new ScrollPageHelper(Gravity.TOP,false);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                VideoPlayer videoPlayer = ((TestVideoAdapter.VideoViewHolder) holder).mVideoPlayer;
                if (videoPlayer == VideoPlayerManager.instance().getCurrentVideoPlayer()) {
                    VideoPlayerManager.instance().releaseVideoPlayer();
                }
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

}
