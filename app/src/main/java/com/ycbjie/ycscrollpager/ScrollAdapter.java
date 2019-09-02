package com.ycbjie.ycscrollpager;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class ScrollAdapter extends RecyclerView.Adapter<ScrollAdapter.MyViewHolder> {


    private Context mContext;
    public interface OnItemClickListener {
        /**
         * item中点击监听接口
         * @param position          索引
         */
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public ScrollAdapter(Context context) {
        this.mContext = context;
    }

    private List<PersonData> data = new ArrayList<>();

    public void setData(List<PersonData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<PersonData> getData() {
        return data;
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_pager, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setBackgroundResource(data.get(position).getImage());
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        MyViewHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


}
