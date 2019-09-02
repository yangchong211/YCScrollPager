package com.ycbjie.ycscrollpager;

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
import com.yc.pagerlib.pager.LayoutViewPager;

import java.util.List;


public class ScrollPagerActivity extends AppCompatActivity {

    private DirectionalViewPager vp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        vp = findViewById(R.id.vp);
        initViewPager();
    }

    private void initViewPager() {
        List<PersonData> list = DataProvider.getList(this,30);
        vp.setOffscreenPageLimit(1);
        vp.setCurrentItem(0);
        vp.setOrientation(DirectionalViewPager.VERTICAL);
        BannerPagerAdapter adapter = new BannerPagerAdapter(list);
        vp.setAdapter(adapter);
        vp.setAnimationDuration(3000);
        vp.setOnViewPagerListener(new OnPagerListener() {
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
    }

    class BannerPagerAdapter extends AbsPagerAdapter {

        private List<PersonData> data;

        public BannerPagerAdapter(List<PersonData> dataList) {
            super(dataList);
            this.data = dataList;
        }


        @Override
        public View getView(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(
                    R.layout.item_image_pager, container, false);
            ImageView imageView = view.findViewById(R.id.iv_image);
            imageView.setImageResource(data.get(position).getImage());
            return view;
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }
}
