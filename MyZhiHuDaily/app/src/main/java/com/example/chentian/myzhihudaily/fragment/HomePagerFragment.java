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
import com.example.chentian.myzhihudaily.been.ContentListBeen;
import com.example.chentian.myzhihudaily.been.Stories;
import com.example.chentian.myzhihudaily.service.ContentListService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chentian on 15/04/2017.
 */

public class HomePagerFragment extends Fragment {
    View view;

    RecyclerView contentRecycler;
    ArrayList<String> titleData;
    ArrayList<String> bmpData;
    ArrayList<Integer> idData;
    List<Stories> stories;
    ContentRecyerAdapter contentAdapter;

    String date;

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
        getCurrentDate();
        contentRecycler = (RecyclerView) view.findViewById(R.id.content_recyler);
        titleData = new ArrayList<String>();
        bmpData = new ArrayList<String>();
        idData = new ArrayList<Integer>();
    }

    private void getCurrentDate() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar now = Calendar.getInstance();
        String month;
        String year = now.get(Calendar.YEAR)+"";
        if((now.get(Calendar.MONTH) + 1)<10){
            month = "0"+(now.get(Calendar.MONTH) + 1)+"";
        }else {
            month = (now.get(Calendar.MONTH) + 1)+"";
        }
        //知乎日报的api用的是before，比如今天是20170412那么今天的热闻应该请求20170413
        String day = (now.get(Calendar.DAY_OF_MONTH)+1)+"";
        date = year+month+day;
        Log.d("TAG", "getCurrentDate: "+date);
    }

    //进行网络请求，获取首页数据
    private void fillContent() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ZhiHuDailyAPI.CONTENT_LIST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ContentListService service =  retrofit.create(ContentListService.class);
        Call<ContentListBeen> call =  service.getContentList(date);
        call.enqueue(new Callback<ContentListBeen>() {
            @Override
            public void onResponse(Call<ContentListBeen> call, Response<ContentListBeen> response) {
                //成功
                if(response.isSuccessful()&&response.body()!=null){
                    //解析成功
                    Log.d("TAG", "onResponse: 解析成功");
                    stories = response.body().getStories();
                    initDatas();
                }else{
                    //解析失败
                    Log.d("TAG", "onResponse: 解析失败");
                }
            }

            @Override
            public void onFailure(Call<ContentListBeen> call, Throwable t) {
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
        for (Stories s:stories){
            titleData.add(s.getTitle());
            bmpData.add(s.getImages().get(0));
            idData.add(s.getId());
        }
        contentAdapter.notifyDataSetChanged();
    }
}
