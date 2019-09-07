package com.ycbjie.ycscrollpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context mContext;
    private List<Video> mVideoList;
    private ArrayList<Integer> data;

    public VideoAdapter(Context context, List<Video> videoList) {
        mContext = context;
        mVideoList = videoList;
        data = DataProvider.getData(context);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_video_pager,
                parent, false);
        VideoViewHolder holder = new VideoViewHolder(itemView);
        //创建视频播放控制器，主要只要创建一次就可以呢
        VideoPlayerController controller = new VideoPlayerController(mContext);
        holder.setController(controller);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = mVideoList.get(position);
        holder.bindData(video,position);
    }

    @Override
    public int getItemCount() {
        return mVideoList==null ? 0 : mVideoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        public VideoPlayerController mController;
        public VideoPlayer mVideoPlayer;

        VideoViewHolder(View itemView) {
            super(itemView);
            mVideoPlayer = itemView.findViewById(R.id.video_player);
        }

        /**
         * 设置视频控制器参数
         * @param controller            控制器对象
         */
        void setController(VideoPlayerController controller) {
            mController = controller;
            mVideoPlayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_IJK);
            mVideoPlayer.setController(mController);
        }

        void bindData(Video video, int position) {
            mController.setTitle(video.getTitle());
            //mController.setLength(video.getLength());
            ImageUtils.loadImgByPicasso(itemView.getContext(),data.get(position),
                    R.drawable.image_default,mController.imageView());
            mVideoPlayer.setUp(video.getVideoUrl(), null);
            //mVideoPlayer.start();

            //mVideoPlayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_IJK);
            //mVideoPlayer.setController(mController);
        }
    }


}
