package com.example.chentian.myzhihudaily.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chentian.myzhihudaily.R;
import com.example.chentian.myzhihudaily.adapter.ContentRecyerAdapter;
import com.example.chentian.myzhihudaily.api.ZhiHuDailyAPI;
import com.example.chentian.myzhihudaily.been.Stories;
import com.example.chentian.myzhihudaily.been.ThemeContentBeen;
import com.example.chentian.myzhihudaily.service.ThemeContentService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public void RefreshUI(){
        if(getActivity()!=null){
            TypedValue colorbackground = new TypedValue();//背景色
            TypedValue textColor = new TypedValue();//字体颜色
            TypedValue lineColor = new TypedValue();//字体颜色
            Resources.Theme theme = getActivity().getTheme();
            theme.resolveAttribute(R.attr.colorBackground, colorbackground, true);
            theme.resolveAttribute(R.attr.colorTextColor, textColor, true);
            theme.resolveAttribute(R.attr.lineColor, lineColor, true);
            //刷新home的RecyclerView
            int homechildCount = themeContentRecycler.getChildCount();
            for (int childIndex = 0; childIndex < homechildCount; childIndex++) {
                ViewGroup childView = (ViewGroup) themeContentRecycler.getChildAt(childIndex);
                childView.setBackgroundResource(colorbackground.resourceId);
                TextView recyclerTitle = (TextView) childView.findViewById(R.id.recycler_title);
                recyclerTitle.setTextColor(getResources().getColor(textColor.resourceId));
                LinearLayout recyclerLayoutone = (LinearLayout) childView.findViewById(R.id.recycler_item);
                recyclerLayoutone.setBackgroundResource(colorbackground.resourceId);
                LinearLayout lineLayout = (LinearLayout) childView.findViewById(R.id.line_layout);
                lineLayout.setBackgroundResource(lineColor.resourceId);
                LinearLayout homeRecyclerLayout = (LinearLayout) childView.findViewById(R.id.home_recycler);
                homeRecyclerLayout.setBackgroundResource(colorbackground.resourceId);
            }

            //让 RecyclerView 缓存在 Pool 中的 Item 失效
            //那么，如果是ListView，要怎么做呢？这里的思路是通过反射拿到 AbsListView 类中的 RecycleBin 对象，然后同样再用反射去调用 clear 方法
            Class<RecyclerView> recyclerViewClass = RecyclerView.class;
            try {
                Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
                declaredField.setAccessible(true);
                Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(declaredField.get(themeContentRecycler), new Object[0]);
                RecyclerView.RecycledViewPool recycledViewPool = themeContentRecycler.getRecycledViewPool();
                recycledViewPool.clear();

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
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
