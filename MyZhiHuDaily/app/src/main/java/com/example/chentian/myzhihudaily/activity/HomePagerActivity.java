package com.example.chentian.myzhihudaily.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chentian.myzhihudaily.R;
import com.example.chentian.myzhihudaily.adapter.MenuRecyclerAdapter;
import com.example.chentian.myzhihudaily.fragment.AboutFragment;
import com.example.chentian.myzhihudaily.fragment.HomePagerFragment;
import com.example.chentian.myzhihudaily.fragment.HotNewsFragment;
import com.example.chentian.myzhihudaily.fragment.SettingFragment;
import com.example.chentian.myzhihudaily.fragment.ThemeContentFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static com.example.chentian.myzhihudaily.R.id.fragment_homepager;

/**
 * Created by chentian on 15/04/2017.
 */

public class HomePagerActivity extends BaseActivity implements View.OnClickListener{

    SlidingMenu menu;
    View menuView;

    ImageView openMenu,DayNightImg;
    LinearLayout hotNewsLayout,gotoHomeLayout,aboutLayout,settingLayout,dayNightLayout;
    TextView homeTitle,dayNightText;

    LinearLayout menuOne;

    ArrayList<LinearLayout> colorLayout = new ArrayList<>();
    ArrayList<LinearLayout> colorLayoutDark = new ArrayList<>();
    ArrayList<RelativeLayout> colorRelayout = new ArrayList<>();
    ArrayList<TextView> colorText = new ArrayList<>();
    ArrayList<TextView> colorTextDark = new ArrayList<>();

        HomePagerFragment homeFragment;
        HotNewsFragment hotNewsFragment;
        ThemeContentFragment themeContentFragment;
        AboutFragment aboutFragment;
        SettingFragment settingFragment;

        RecyclerView menuRecycler;
        MenuRecyclerAdapter menuAdapter;
        ArrayList<String> menuDatas;
        String[] menuData = {"日常心理学","用户推荐日报","电影日报","不许无聊","设计日报","大公司日报",
                "财经日报","互联网安全","开始游戏","音乐日报","动漫日报","体育日报"};


        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            initTheme();
            setContentView(R.layout.activity_home);

            initDatas();
            initFragment();
            initMenuRecycler();
            initView();
            setSlidingMenu();
        }

    private void initTheme() {
        setTheme(R.style.DayTheme);
    }

    private void initView() {
        openMenu = (ImageView) findViewById(R.id.open_menu);
        hotNewsLayout = (LinearLayout) menuView.findViewById(R.id.hot_news);
        homeTitle = (TextView) findViewById(R.id.home_title);
        gotoHomeLayout = (LinearLayout) menuView.findViewById(R.id.goto_home);
        aboutLayout = (LinearLayout) menuView.findViewById(R.id.about);
        settingLayout = (LinearLayout) menuView.findViewById(R.id.setting);
        dayNightLayout = (LinearLayout) menuView.findViewById(R.id.day_night_mode);
        menuOne = (LinearLayout) menuView.findViewById(R.id.menu_one);
        DayNightImg = (ImageView) menuView.findViewById(R.id.day_night_img);
        dayNightText = (TextView) menuView.findViewById(R.id.menu_text_seven);

        homeFragment.setActivityTitle(homeTitle);

        colorLayout.add(menuOne);
        colorLayoutDark.add(gotoHomeLayout);
        colorLayout.add(hotNewsLayout);
        colorLayout.add(aboutLayout);
        colorLayout.add(settingLayout);
        colorLayoutDark.add((LinearLayout) menuView.findViewById(R.id.menu_layout_one));
        colorLayoutDark.add((LinearLayout) menuView.findViewById(R.id.menu_layout_two));
        colorLayoutDark.add((LinearLayout) menuView.findViewById(R.id.menu_layout_three));
        colorLayoutDark.add((LinearLayout) menuView.findViewById(R.id.menu_layout_four));
        colorLayoutDark.add(dayNightLayout);

        colorText.add((TextView) menuView.findViewById(R.id.menu_text_one));
        colorText.add((TextView) menuView.findViewById(R.id.menu_text_two));
        colorText.add((TextView) menuView.findViewById(R.id.menu_text_three));
        colorText.add((TextView) menuView.findViewById(R.id.menu_text_four));
        colorText.add(homeTitle);
        colorTextDark.add((TextView) menuView.findViewById(R.id.menu_text_five));
        colorTextDark.add((TextView) menuView.findViewById(R.id.menu_text_six));
        colorTextDark.add(dayNightText);

        colorRelayout.add((RelativeLayout) findViewById(R.id.home_layout));

        openMenu.setOnClickListener(this);
        hotNewsLayout.setOnClickListener(this);
        gotoHomeLayout.setOnClickListener(this);
        aboutLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
        dayNightLayout.setOnClickListener(this);
    }

    private void initDatas() {
        menuDatas = new ArrayList<String>();
        for(String s:menuData){
            menuDatas.add(s);
        }
    }

    private void initMenuRecycler() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        menuView = layoutInflater.inflate(R.layout.activity_menu,null);
        menuAdapter = new MenuRecyclerAdapter(this,menuDatas);
        menuRecycler = (RecyclerView) menuView.findViewById(R.id.recycler_menu);
        menuRecycler.setLayoutManager(new LinearLayoutManager(this));
        menuRecycler.setAdapter(menuAdapter);

        menuAdapter.setOnItemClickListener(new MenuRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Toast.makeText(HomePagerActivity.this, data, Toast.LENGTH_SHORT).show();
                homeTitle.setText(data);
                themeContentFragment = new ThemeContentFragment();
                themeContentFragment.setThemeId(getTheThemeId(data));
                changeFragment(themeContentFragment);
            }
        });
    }

    //通过名字返回themeId
    private String getTheThemeId(String data){
        switch (data){
            case "日常心理学":
                return "13";
            case "用户推荐日报":
                return "12";
            case "电影日报":
                return "3";
            case "不许无聊":
                return "11";
            case "设计日报":
                return "4";
            case "大公司日报":
                return "5";
            case "财经日报":
                return "6";
            case "互联网安全":
                return "10";
            case "开始游戏":
                return "2";
            case "音乐日报":
                return "7";
            case "动漫日报":
                return "9";
            case "体育日报":
                return "8";

            default:
                return null;
        }
    }

    private void setSlidingMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setBehindScrollScale(1.0f);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(menuView);
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        homeFragment = new HomePagerFragment();
        transaction.add(fragment_homepager, homeFragment);
        transaction.commit();

        hotNewsFragment = new HotNewsFragment();
        aboutFragment = new AboutFragment();
        settingFragment = new SettingFragment();
    }

    public void changeFragment(Fragment to){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(fragment_homepager, to).commit();
    }

    private void changeTheme(){
        toggleTheme();
        refreshUI();
        homeFragment.RefreshUI();
        hotNewsFragment.RefreshUI();
        if(themeContentFragment!=null){
            themeContentFragment.RefreshUI();
        }
    }

    private void toggleTheme() {
        if (isDay) {
            setTheme(R.style.NightTheme);
            getWindow().setStatusBarColor(Color.parseColor("#3A3A3A"));
            isDay = false;
        } else {
            setTheme(R.style.DayTheme);
            getWindow().setStatusBarColor(Color.parseColor("#5072A5"));
            isDay = true;
        }
    }

    private void refreshUI() {
        TypedValue colorbackground = new TypedValue();//背景色
        TypedValue menubackground = new TypedValue();
        TypedValue menubackgroundDark = new TypedValue();
        TypedValue textColor = new TypedValue();//字体颜色
        TypedValue menutextColor = new TypedValue();//字体颜色
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorBackground, colorbackground, true);
        theme.resolveAttribute(R.attr.menuBackground, menubackground, true);
        theme.resolveAttribute(R.attr.colorTextColor, textColor, true);
        theme.resolveAttribute(R.attr.menuTextColor, menutextColor, true);
        theme.resolveAttribute(R.attr.menuBackgroundDark, menubackgroundDark, true);

        for(RelativeLayout layout : colorRelayout){
            layout.setBackgroundResource(menubackground.resourceId);
        }

        for(LinearLayout layout : colorLayout){
            layout.setBackgroundResource(menubackground.resourceId);
        }

        for(TextView textView : colorText){
            textView.setBackgroundResource(menubackground.resourceId);
            textView.setTextColor(getResources().getColor(menutextColor.resourceId));
        }

        //设置深色的，首页，夜间，下载三个
        for(LinearLayout layout : colorLayoutDark){
            layout.setBackgroundResource(menubackgroundDark.resourceId);
        }

        for(TextView textView : colorTextDark){
            textView.setBackgroundResource(menubackgroundDark.resourceId);
            textView.setTextColor(getResources().getColor(menutextColor.resourceId));
        }

        int childCount = menuRecycler.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) menuRecycler.getChildAt(childIndex);
            childView.setBackgroundResource(menubackground.resourceId);
            LinearLayout layoutOne = (LinearLayout) childView.findViewById(R.id.menu_recy_layoutone);
            LinearLayout layoutTwo = (LinearLayout) childView.findViewById(R.id.menu_recy_layouttwo);
            TextView textView = (TextView) childView.findViewById(R.id.menu_text);
            layoutOne.setBackgroundResource(menubackground.resourceId);
            layoutTwo.setBackgroundResource(menubackground.resourceId);
            textView.setBackgroundResource(menubackground.resourceId);
            textView.setTextColor(getResources().getColor(menutextColor.resourceId));
        }

        //让 RecyclerView 缓存在 Pool 中的 Item 失效
        //那么，如果是ListView，要怎么做呢？这里的思路是通过反射拿到 AbsListView 类中的 RecycleBin 对象，然后同样再用反射去调用 clear 方法
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(menuRecycler), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = menuRecycler.getRecycledViewPool();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open_menu:
                menu.toggle();
                break;

            case R.id.goto_home:
                homeTitle.setText("今日热闻");
                changeFragment(homeFragment);
                break;

            case R.id.hot_news:
                homeTitle.setText("最近热门");
                changeFragment(hotNewsFragment);
                break;

            case R.id.about:
                homeTitle.setText("关于");
                changeFragment(aboutFragment);
                break;

            case R.id.setting:
                homeTitle.setText("设置");
                changeFragment(settingFragment);
                break;

            case R.id.day_night_mode:
                //夜间模式
                DayNightImg.setImageResource(isDay?R.drawable.sun:R.drawable.dark);
                dayNightText.setText(isDay?"日间":"夜间");
                changeTheme();
                break;
        }
    }
}
