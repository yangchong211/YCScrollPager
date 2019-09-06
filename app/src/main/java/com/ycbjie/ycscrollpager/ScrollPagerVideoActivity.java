package com.ycbjie.ycscrollpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.yc.pagerlib.pager.DirectionalViewPager;

import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;

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

    @Override
    public void onBackPressed() {
        if (VideoPlayerManager.instance().onBackPressed()){
            return;
        }
        super.onBackPressed();
    }


    private void initViewPager() {
        ArrayList<Integer> data = DataProvider.getData(this);
        List<Video> list = new ArrayList<>();
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int a = 0; a< DataProvider.VideoPlayerList.length ; a++){
            Video video = new Video(DataProvider.VideoPlayerTitle[a],
                    10,"",DataProvider.VideoPlayerList[a]);
            list.add(video);
            fragments.add(VideoFragment.newInstant(DataProvider.VideoPlayerList[a],data.get(a)));
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

}
