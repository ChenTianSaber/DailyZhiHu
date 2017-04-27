package com.example.chentian.myzhihudaily.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chentian.myzhihudaily.R;

/**
 * Created by chentian on 12/04/2017.
 */

public class DownloadDetaiContentActivity extends BaseActivity {
    String body,title;
    WebView contentView;
    ImageView topImage;
    TextView topTitle,topSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        initTheme();
        setContentView(R.layout.detail_layout);
        //将状态栏透明
        if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        init();
        fillContent();
    }

    private void initTheme() {
        setTheme(R.style.NightTheme);
    }

    private void init(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.Download_layout);
        layout.setVisibility(View.GONE);

        Intent intent = getIntent();
        body = intent.getStringExtra("body");
        title = intent.getStringExtra("title");
        contentView = (WebView) findViewById(R.id.content_webview);
        contentView.setHorizontalScrollBarEnabled(false);//水平不显示
        contentView.setVerticalScrollBarEnabled(false); //垂直不显示

        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        contentView.setWebViewClient(new WebViewClient());

        topImage = (ImageView) findViewById(R.id.top_image);
        topTitle = (TextView) findViewById(R.id.top_titlt);
        topSource = (TextView) findViewById(R.id.top_source);
    }

    //进行网络请求，请求首页的详细内容
    private void fillContent() {
        topTitle.setText(title);
        topSource.setText("图片:"+"懒得获取~");
        topImage.setImageResource(R.mipmap.default_img);

        String css;
        if(!isDay){
            css = "<link type=\"text/css\" href=\"" +
                    "file:///android_asset/zhihu_dark.css" +
                    "\" " +
                    "rel=\"stylesheet\" />\n";
        }else {
            css = "<link type=\"text/css\" href=\"" +
                    "file:///android_asset/zhihu.css" +
                    "\" " +
                    "rel=\"stylesheet\" />\n";
        }
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "\t<meta charset=\"utf-8\" />\n</head>\n" +
                "<body>\n" + css +
                body.replace("<div class=\"img-place-holder\">", "") + "\n<body>";
        contentView.loadDataWithBaseURL("x-data://base", html, "text/html", "utf-8", null);
    }
}
