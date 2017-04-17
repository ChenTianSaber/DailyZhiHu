package com.example.chentian.myzhihudaily.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chentian.myzhihudaily.R;
import com.example.chentian.myzhihudaily.adapter.ContentRecyerAdapter;
import com.example.chentian.myzhihudaily.api.ZhiHuDailyAPI;
import com.example.chentian.myzhihudaily.been.HotNewsBeen;
import com.example.chentian.myzhihudaily.been.Recent;
import com.example.chentian.myzhihudaily.service.HotNewsService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chentian on 15/04/2017.
 */

public class HotNewsFragment extends Fragment {
    View view;

    RecyclerView contentRecycler;
    ArrayList<String> titleData;
    ArrayList<String> bmpData;
    ArrayList<Integer> idData;
    List<Recent> recents;
    ContentRecyerAdapter contentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.activity_content,container,false);
        }
        init();
        fillContent();
        setHomeRecycler();
        return view;
    }

    private void init() {
        contentRecycler = (RecyclerView) view.findViewById(R.id.content_recyler);
        titleData = new ArrayList<String>();
        bmpData = new ArrayList<String>();
        idData = new ArrayList<Integer>();
    }

    //进行网络请求，获取首页数据
    private void fillContent() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ZhiHuDailyAPI.HOT_NEWS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final HotNewsService service =  retrofit.create(HotNewsService.class);
        Call<HotNewsBeen> call =  service.getHotNews();
        call.enqueue(new Callback<HotNewsBeen>() {
            @Override
            public void onResponse(Call<HotNewsBeen> call, Response<HotNewsBeen> response) {
                //成功
                if(response.isSuccessful()&&response.body()!=null){
                    //解析成功
                    Log.d("TAG", "onResponse: 解析成功");
                    recents = response.body().getRecent();
                    initDatas();
                }else{
                    //解析失败
                    Log.d("TAG", "onResponse: 解析失败");
                }
            }

            @Override
            public void onFailure(Call<HotNewsBeen> call, Throwable t) {
                //失败
                Log.d("TAG", "onFailure: 响应失败");
            }
        });
    }

    private void setHomeRecycler() {
        contentAdapter = new ContentRecyerAdapter(getContext(),titleData,bmpData,idData);
        contentRecycler.setAdapter(contentAdapter);
        contentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    //初始化数据，填充首页数据
    private void initDatas() {
        for (Recent r:recents){
            titleData.add(r.getTitle());
            bmpData.add(r.getThumbnail());
            idData.add(r.getNewsId());
        }
        contentAdapter.notifyDataSetChanged();
    }
}
