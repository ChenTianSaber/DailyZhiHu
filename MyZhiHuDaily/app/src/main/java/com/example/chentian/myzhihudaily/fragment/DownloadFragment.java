package com.example.chentian.myzhihudaily.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chentian.myzhihudaily.R;
import com.example.chentian.myzhihudaily.adapter.DownloadAdapter;
import com.example.chentian.myzhihudaily.database.Artical;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chentian on 27/04/2017.
 */

public class DownloadFragment extends Fragment {
    View view;
    RecyclerView contentRecycler;
    ArrayList<String> titleData;
    ArrayList<String> bodyData;
    DownloadAdapter contentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.activity_content,container,false);
        }
        init();
        setHomeRecycler();
        fillContent();
        return view;
    }

    private void init() {
        contentRecycler = (RecyclerView) view.findViewById(R.id.content_recyler);
        titleData = new ArrayList<String>();
        bodyData = new ArrayList<String>();
    }

    private void fillContent() {
        List<Artical> allArticals = DataSupport.findAll(Artical.class);

        for (Artical a:allArticals){
            titleData.add(a.getArticalTitle());
            bodyData.add(a.getArticalBody());
        }
        contentAdapter.notifyDataSetChanged();
    }

    private void setHomeRecycler() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        contentAdapter = new DownloadAdapter(getContext(),titleData,bodyData);
        contentRecycler.setAdapter(contentAdapter);
        contentRecycler.setLayoutManager(linearLayoutManager);
    }

    public void RefreshUI(){
        if(getActivity()!=null){
            TypedValue colorbackground = new TypedValue();//背景色
            TypedValue textColor = new TypedValue();//字体颜色
            TypedValue lineColor = new TypedValue();//字体颜色
            TypedValue colorTitlebackground = new TypedValue();//背景色
            Resources.Theme theme = getActivity().getTheme();
            theme.resolveAttribute(R.attr.colorBackground, colorbackground, true);
            theme.resolveAttribute(R.attr.colorTextColor, textColor, true);
            theme.resolveAttribute(R.attr.lineColor, lineColor, true);
            theme.resolveAttribute(R.attr.colorPrimary, colorTitlebackground, true);
            //刷新home的RecyclerView
            int homechildCount = contentRecycler.getChildCount();
            for (int childIndex = 0; childIndex < homechildCount; childIndex++) {
                ViewGroup childView = (ViewGroup) contentRecycler.getChildAt(childIndex);
                childView.setBackgroundResource(colorbackground.resourceId);
                TextView recyclerTitle = (TextView) childView.findViewById(R.id.recycler_title);
                recyclerTitle.setTextColor(getResources().getColor(textColor.resourceId));
                LinearLayout recyclerLayoutone = (LinearLayout) childView.findViewById(R.id.recycler_item);
                recyclerLayoutone.setBackgroundResource(colorbackground.resourceId);
                LinearLayout lineLayout = (LinearLayout) childView.findViewById(R.id.line_layout);
                lineLayout.setBackgroundResource(lineColor.resourceId);
                LinearLayout homeRecyclerLayout = (LinearLayout) childView.findViewById(R.id.home_recycler);
                homeRecyclerLayout.setBackgroundResource(colorbackground.resourceId);
                TextView itemTitleDate = (TextView) childView.findViewById(R.id.item_home_title);
                if(itemTitleDate.getVisibility()!=View.GONE){
                    itemTitleDate.setBackgroundResource(colorTitlebackground.resourceId);
                }
            }

            //让 RecyclerView 缓存在 Pool 中的 Item 失效
            //那么，如果是ListView，要怎么做呢？这里的思路是通过反射拿到 AbsListView 类中的 RecycleBin 对象，然后同样再用反射去调用 clear 方法
            Class<RecyclerView> recyclerViewClass = RecyclerView.class;
            try {
                Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
                declaredField.setAccessible(true);
                Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(declaredField.get(contentRecycler), new Object[0]);
                RecyclerView.RecycledViewPool recycledViewPool = contentRecycler.getRecycledViewPool();
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
}
