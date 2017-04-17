package com.example.chentian.myzhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chentian.myzhihudaily.R;

import java.util.ArrayList;

/**
 * Created by chentian on 14/04/2017.
 */

public class MenuRecyclerAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    Context context;
    ArrayList<String> Data;

    public MenuRecyclerAdapter(Context context, ArrayList<String> Data) {
        this.context = context;
        this.Data = Data;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , String data);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.menu_item, parent, false);
        MenuRecyclerViewHolder viewHolder = new MenuRecyclerViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MenuRecyclerViewHolder viewHolder = (MenuRecyclerViewHolder) holder;
        viewHolder.menuText.setText(Data.get(position));
        viewHolder.itemView.setTag(Data.get(position));
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    class MenuRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView menuText;
        LinearLayout menuItemLayout;

        public MenuRecyclerViewHolder(View itemView) {
            super(itemView);
            menuText = (TextView) itemView.findViewById(R.id.menu_text);
            menuItemLayout = (LinearLayout) itemView.findViewById(R.id.menu_recycler_layout);
        }
    }
}