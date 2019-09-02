package com.ycbjie.ycscrollpager;


import android.content.Context;
import android.content.res.TypedArray;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DataProvider {



    private static ArrayList<Integer> getData2(Context context){
        ArrayList<Integer> data = new ArrayList<>();
        TypedArray bannerImage = context.getResources().obtainTypedArray(R.array.data_narrow_Image);
        for (int i = 0; i < 30 ; i++) {
            int image = bannerImage.getResourceId(i, R.drawable.bg_autumn_tree_min);
            data.add(image);
        }
        bannerImage.recycle();
        return data;
    }

    public static List<PersonData> getList(Context context,int size){
        if (size==0){
            size = 16;
        }
        ArrayList<PersonData> arr = new ArrayList<>();
        ArrayList<Integer> data = getData2(context);
        for (int i=0 ; i< size ; i++){
            PersonData person = new PersonData();
            person.setName("小杨逗比"+i);
            person.setImage(data.get(i));
            person.setSign("杨充"+i);
            arr.add(person);
        }
        return arr;
    }


}
