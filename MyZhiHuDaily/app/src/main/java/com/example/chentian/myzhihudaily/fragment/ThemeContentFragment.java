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
import com.example.chentian.myzhihudaily.been.Stories;
import com.example.chentian.myzhihudaily.been.ThemeContentBeen;
import com.example.chentian.myzhihudaily.service.ThemeContentService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chentian on 16/04/2017.
 */

public class ThemeContentFragment extends Fragment {
    View view;

    String themeId;

    RecyclerView themeContentRecycler;
    ArrayList<String> titleData;
    ArrayList<String> bmpData;
    ArrayList<Integer> idData;
    List<Stories> stories;
    ContentRecyerAdapter contentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_content, container, false);
        }
        init();
        fillContent();
        setHomeRecycler();
        return view;
    }

    private void init() {
        themeContentRecycler = (RecyclerView) view.findViewById(R.id.content_recyler);
        titleData = new ArrayList<String>();
        bmpData = new ArrayList<String>();
        idData = new ArrayList<Integer>();
    }

    @Override
    public void onResume() {
        fillContent();
        super.onResume();
    }

    public void setThemeId(String themeId){
        this.themeId = themeId;
    }

    //进行网络请求，获取首页数据
    private void fillContent() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ZhiHuDailyAPI.THEME_CONTENT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ThemeContentService service = retrofit.create(ThemeContentService.class);
        Call<ThemeContentBeen> call = service.getThemeContent(themeId);
        call.enqueue(new Callback<ThemeContentBeen>() {
            @Override
            public void onResponse(Call<ThemeContentBeen> call, Response<ThemeContentBeen> response) {
                //成功
                if (response.isSuccessful() && response.body() != null) {
                    //解析成功
                    Log.d("TAG", "onResponse: 解析成功"+themeId);
                    stories = response.body().getStories();
                    initDatas();
                } else {
                    //解析失败
                    Log.d("TAG", "onResponse: 解析失败");
                }
            }

            @Override
            public void onFailure(Call<ThemeContentBeen> call, Throwable t) {
                //失败
                Log.d("TAG", "onFailure: 响应失败");
            }
        });
    }

    private void setHomeRecycler() {
        contentAdapter = new ContentRecyerAdapter(getContext(), titleData, bmpData, idData);
        themeContentRecycler.setAdapter(contentAdapter);
        themeContentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    //初始化数据，填充首页数据
    private void initDatas() {
        Log.d("TAG", "initDatas: ");
        for (Stories s:stories){
            titleData.add(s.getTitle());
            if(s.getImages()!=null) {
                bmpData.add(s.getImages().get(0));
            }else {
                bmpData.add("http://pic2.zhimg.com/afecdc04983a8e261326386995150599_t.jpg");
            }
            idData.add(s.getId());
        }
        contentAdapter.notifyDataSetChanged();
    }
}
