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
import com.example.chentian.myzhihudaily.adapter.HomeContentRecyerAdapter;
import com.example.chentian.myzhihudaily.api.ZhiHuDailyAPI;
import com.example.chentian.myzhihudaily.been.ContentListBeen;
import com.example.chentian.myzhihudaily.been.Stories;
import com.example.chentian.myzhihudaily.listener.EndLessOnScrollListener;
import com.example.chentian.myzhihudaily.service.ContentListService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Integer.parseInt;

/**
 * Created by chentian on 15/04/2017.
 */

public class HomePagerFragment extends Fragment {
    View view;

    RecyclerView contentRecycler;
    ArrayList<String> titleData;
    ArrayList<String> bmpData;
    ArrayList<Integer> idData;
    ArrayList<String> dateData;
    List<Stories> stories;
    HomeContentRecyerAdapter contentAdapter;

    TextView activityTitle,RecyclerTitle;

    String date,dateUse,toDay;

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
        getCurrentDate();
        getToday();
        contentRecycler = (RecyclerView) view.findViewById(R.id.content_recyler);
        titleData = new ArrayList<String>();
        bmpData = new ArrayList<String>();
        idData = new ArrayList<Integer>();
        dateData = new ArrayList<String>();
    }

    public void setActivityTitle(TextView activityTitle){
        this.activityTitle = activityTitle;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void Refresh(){
        fillContent();
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

        if((Integer.parseInt(month)<8&&Integer.parseInt(month)%2==0&&(Integer.parseInt(day)+1)>30)
                ||(Integer.parseInt(month)<8&&Integer.parseInt(month)%2!=0&&(Integer.parseInt(day)+1)>31)
                ||(Integer.parseInt(month)>=8&&Integer.parseInt(month)%2!=0&&(Integer.parseInt(day)+1)>31)
                ||(Integer.parseInt(month)>=8&&Integer.parseInt(month)%2==0&&(Integer.parseInt(day)+1)>30)){
            day = "01";
            if(Integer.parseInt(month)+1<10){
                month = "0"+String.valueOf(Integer.parseInt(month)+1);
            }else {
                month = "0"+String.valueOf(Integer.parseInt(month)+1);
            }
        }

        //在个位数的day前面加0
        if(Integer.parseInt(day)+1<10){
            day = "0"+day;
        }

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
                    dateUse = response.body().getDate();
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
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        contentAdapter = new HomeContentRecyerAdapter(getContext(),titleData,bmpData,idData,dateData);
        contentRecycler.setAdapter(contentAdapter);
        contentRecycler.setLayoutManager(linearLayoutManager);
        contentRecycler.addOnScrollListener(new EndLessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                int dataLast = parseInt(date)-1;
                setDate(String.valueOf(dataLast));
                Refresh();
            }
        });
        contentRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = linearLayoutManager.findFirstVisibleItemPosition();

                View view = linearLayoutManager.findViewByPosition(position);
                RecyclerTitle = (TextView) view.findViewById(R.id.item_home_title);
                if(RecyclerTitle.getVisibility()!=View.GONE){
                    if(dy>0){
                        activityTitle.setText(RecyclerTitle.getText().toString());
                    }else if(dy<0){
                        activityTitle.setText(lastDate(RecyclerTitle.getText().toString()));
                    }
                }
            }
        });
    }

    public String lastDate(String date){
        String one = date.substring(0,4);
        String two = date.substring(5,7);
        String three = date.substring(8,10);

        if((Integer.parseInt(two)<8&&Integer.parseInt(two)%2==0&&(Integer.parseInt(three)+1)>30)
                ||(Integer.parseInt(two)<8&&Integer.parseInt(two)%2!=0&&(Integer.parseInt(three)+1)>31)
                ||(Integer.parseInt(two)>=8&&Integer.parseInt(two)%2!=0&&(Integer.parseInt(three)+1)>31)
                ||(Integer.parseInt(two)>=8&&Integer.parseInt(two)%2==0&&(Integer.parseInt(three)+1)>30)){
            three = "01";
            if(Integer.parseInt(two)+1<10){
                two = "0"+String.valueOf(Integer.parseInt(two)+1);
            }else {
                two = "0"+String.valueOf(Integer.parseInt(two)+1);
            }

            if((one+two+three).equals(toDay)){
                return "今日热闻";
            }

            return formatDate(one+two+three);
        }else {
            int i = Integer.parseInt(one+two+three)+1;
            if(String.valueOf(i).equals(toDay)){
                return "今日热闻";
            }
            return formatDate(String.valueOf(i));
        }


    }

    private void getToday() {
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
        String day = (now.get(Calendar.DAY_OF_MONTH))+"";

        if((Integer.parseInt(month)<8&&Integer.parseInt(month)%2==0&&(Integer.parseInt(day)+1)>30)
                ||(Integer.parseInt(month)<8&&Integer.parseInt(month)%2!=0&&(Integer.parseInt(day)+1)>31)
                ||(Integer.parseInt(month)>=8&&Integer.parseInt(month)%2!=0&&(Integer.parseInt(day)+1)>31)
                ||(Integer.parseInt(month)>=8&&Integer.parseInt(month)%2==0&&(Integer.parseInt(day)+1)>30)){
            day = "01";
            if(Integer.parseInt(month)+1<10){
                month = "0"+String.valueOf(Integer.parseInt(month)+1);
            }else {
                month = "0"+String.valueOf(Integer.parseInt(month)+1);
            }
        }

        //在个位数的day前面加0
        if(Integer.parseInt(day)+1<10){
            day = "0"+day;
        }

        toDay = year+month+day;
        Log.d("TAG", "getToday: "+toDay);
    }

    public String formatDate(String date){
        String one = date.substring(0,4);
        String two = date.substring(4,6);
        String three = date.substring(6,8);
        return one+"-"+two+"-"+three;
    }

    //初始化数据，填充首页数据
    private void initDatas() {
        for (Stories s:stories){
            titleData.add(s.getTitle());
            bmpData.add(s.getImages().get(0));
            idData.add(s.getId());
            dateData.add(dateUse);
        }
        contentAdapter.notifyDataSetChanged();
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
