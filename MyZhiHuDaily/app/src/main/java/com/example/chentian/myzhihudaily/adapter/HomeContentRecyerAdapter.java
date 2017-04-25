package com.example.chentian.myzhihudaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chentian.myzhihudaily.R;
import com.example.chentian.myzhihudaily.activity.DetaiContentActivity;

import java.util.ArrayList;

/**
 * Created by chentian on 11/04/2017.
 */

public class HomeContentRecyerAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<String> titleData;
    ArrayList<String> bmpData;
    ArrayList<Integer> idData;

    ArrayList<String> date;

    public HomeContentRecyerAdapter(Context context, ArrayList<String> titleData, ArrayList<String> bmpData, ArrayList<Integer> idData,ArrayList<String> date) {
        this.context = context;
        this.titleData = titleData;
        this.bmpData = bmpData;
        this.idData = idData;
        this.date = date;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recycler_item,parent,false);
        ContentRecyclerViewHolder viewHolder = new ContentRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentRecyclerViewHolder viewHolder = (ContentRecyclerViewHolder) holder;
        viewHolder.textView.setText(titleData.get(position));
        Glide.with(context).load(bmpData.get(position)).into(viewHolder.imageView);
        viewHolder.textContentId.setText(idData.get(position)+"");
        if(position>1){
            String titleUp = titleData.get(position-1);
            String titleCur = titleData.get(position);

            if(titleData.get(position-1).startsWith("这里是广告")){
                viewHolder.titleDate.setVisibility(View.VISIBLE);
                viewHolder.titleDate.setText(formatDate(date.get(position)));
            }
            else if(titleUp.startsWith("瞎扯")&&!titleCur.startsWith("这里是广告")){
                viewHolder.titleDate.setVisibility(View.VISIBLE);
                viewHolder.titleDate.setText(formatDate(date.get(position)));
            }
            else {
                viewHolder.titleDate.setVisibility(View.GONE);
            }
        }
    }

    public String formatDate(String date){
        String one = date.substring(0,4);
        String two = date.substring(4,6);
        String three = date.substring(6,8);
        return one+"-"+two+"-"+three;
    }

    @Override
    public int getItemCount() {
        return titleData.size();
    }

    class ContentRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textView,textContentId,titleDate;
        ImageView imageView;
        LinearLayout itemLayout;

        public ContentRecyclerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycler_title);
            imageView = (ImageView) itemView.findViewById(R.id.recycler_img);
            textContentId = (TextView) itemView.findViewById(R.id.tv_content_id);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.recycler_item);
            titleDate = (TextView) itemView.findViewById(R.id.item_home_title);

            itemLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DetaiContentActivity.class);
            intent.putExtra("contentId",textContentId.getText().toString());
            context.startActivity(intent);
        }
    }
}
