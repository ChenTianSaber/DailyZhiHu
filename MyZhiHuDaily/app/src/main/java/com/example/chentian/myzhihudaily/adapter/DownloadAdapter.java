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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chentian.myzhihudaily.R;
import com.example.chentian.myzhihudaily.activity.DownloadDetaiContentActivity;
import com.example.chentian.myzhihudaily.database.Artical;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by chentian on 27/04/2017.
 */

public class DownloadAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<String> titleData;
    ArrayList<String> bodyData;

    public DownloadAdapter(Context context, ArrayList<String> titleData,ArrayList<String> bodyData) {
        this.context = context;
        this.titleData = titleData;
        this.bodyData = bodyData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recycler_item, parent, false);
        ContentRecyclerViewHolder viewHolder = new ContentRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentRecyclerViewHolder viewHolder = (ContentRecyclerViewHolder) holder;
        viewHolder.textView.setText(titleData.get(position));
        Glide.with(context).load(R.mipmap.default_img).into(viewHolder.imageView);
        viewHolder.bodyText.setText(bodyData.get(position));
    }

    @Override
    public int getItemCount() {
        return titleData.size();
    }

    class ContentRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView,bodyText;
        ImageView imageView;
        LinearLayout itemLayout;

        public ContentRecyclerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycler_title);
            bodyText = (TextView) itemView.findViewById(R.id.tv_content_id);
            imageView = (ImageView) itemView.findViewById(R.id.recycler_img);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.recycler_item);

            itemLayout.setOnClickListener(this);
            itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    titleData.remove(textView.getText().toString());
                    notifyDataSetChanged();
                    DataSupport.deleteAll(Artical.class, "articalTitle = ?" , textView.getText().toString());
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,DownloadDetaiContentActivity.class);
            intent.putExtra("body",bodyText.getText().toString());
            intent.putExtra("title",textView.getText().toString());
            context.startActivity(intent);
        }
    }
}
