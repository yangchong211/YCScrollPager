package com.ycbjie.ycscrollpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yc.pagerlib.pager.AbsPagerAdapter;
import com.yc.pagerlib.pager.VerticalViewPager;

import java.util.List;


public class ScrollPagerActivity extends AppCompatActivity {

    private VerticalViewPager vp;

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
        vp.setVertical(true);
        BannerPagerAdapter adapter = new BannerPagerAdapter(list);
        vp.setAdapter(adapter);
        vp.setAnimationDuration(3000);
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
