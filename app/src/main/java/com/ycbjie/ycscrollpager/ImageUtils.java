package com.ycbjie.ycscrollpager;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageUtils {

    /**
     * 加载图片
     * @param resId
     * @param target        控件
     */
    public static void loadImgByPicasso(Context context , int path , int resId, ImageView target) {
        if(target==null){
            return;
        }
        if(path!=0){
            Picasso.with(context)
                    .load(path)
                    .placeholder(resId)
                    .error(resId)
                    .into(target);
        }else {
            Picasso.with(context)
                    .load(resId)
                    .placeholder(resId)
                    .error(resId)
                    .into(target);
        }
    }


}
