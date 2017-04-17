package com.example.chentian.myzhihudaily.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;

import static com.example.chentian.myzhihudaily.R.id.fragment_homepager;

/**
 * Created by chentian on 15/04/2017.
 */

public class HomePagerActivity extends BaseActivity implements View.OnClickListener{
    SlidingMenu menu;
    View menuView;

    ImageView openMenu,openCalendar;
    LinearLayout hotNewsLayout,gotoHomeLayout,aboutLayout,settingLayout;
    TextView homeTitle;

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
        setContentView(R.layout.activity_home);

        initDatas();
        initFragment();
        initMenuRecycler();
        initView();
        setSlidingMenu();
    }

    private void initView() {
        openMenu = (ImageView) findViewById(R.id.open_menu);
        openCalendar = (ImageView) findViewById(R.id.open_calendar);
        hotNewsLayout = (LinearLayout) menuView.findViewById(R.id.hot_news);
        homeTitle = (TextView) findViewById(R.id.home_title);
        gotoHomeLayout = (LinearLayout) menuView.findViewById(R.id.goto_home);
        aboutLayout = (LinearLayout) menuView.findViewById(R.id.about);
        settingLayout = (LinearLayout) menuView.findViewById(R.id.setting);

        openMenu.setOnClickListener(this);
        openCalendar.setOnClickListener(this);
        hotNewsLayout.setOnClickListener(this);
        gotoHomeLayout.setOnClickListener(this);
        aboutLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open_menu:
                menu.toggle();
                break;

            case R.id.open_calendar:

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
        }
    }
}
