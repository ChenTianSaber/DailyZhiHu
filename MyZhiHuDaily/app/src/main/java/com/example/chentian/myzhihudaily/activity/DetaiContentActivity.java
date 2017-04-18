package com.example.chentian.myzhihudaily.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chentian.myzhihudaily.R;
import com.example.chentian.myzhihudaily.api.ZhiHuDailyAPI;
import com.example.chentian.myzhihudaily.been.ContentBeen;
import com.example.chentian.myzhihudaily.service.ContentService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chentian on 12/04/2017.
 */

public class DetaiContentActivity extends Activity {
    String contentId;
    WebView contentView;
    ImageView topImage;
    TextView topTitle,topSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void init(){
        Intent intent = getIntent();
        contentId = intent.getStringExtra("contentId");
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ZhiHuDailyAPI.CONTENT_DETAIL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ContentService service =  retrofit.create(ContentService.class);
        Call<ContentBeen> call =  service.getContent(contentId);
        call.enqueue(new Callback<ContentBeen>() {
            @Override
            public void onResponse(Call<ContentBeen> call, Response<ContentBeen> response) {
                //成功
                if(response.isSuccessful()&&response.body()!=null){
                    //解析成功

                    //添加css样式
                    String css = "<link type=\"text/css\" href=\"" +
                            "file:///android_asset/zhihu.css" +
                            "\" " +
                            "rel=\"stylesheet\" />\n";
                    String html = response.body().getBody();
                    final String shareUrl = response.body().getShareUrl();
                    //有时候返回的没有body,我们得用ShareUrl来代替
                    if(html!=null){
                        html = "<!DOCTYPE html>\n" +
                                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                                "<head>\n" +
                                "\t<meta charset=\"utf-8\" />\n</head>\n" +
                                "<body>\n" + css +
                                response.body().getBody().replace("<div class=\"img-place-holder\">", "") + "\n<body>";
                        contentView.loadDataWithBaseURL("x-data://base", html, "text/html", "utf-8", null);
                    }else {
                        contentView.loadUrl(shareUrl);
                    }

                    String imageUrl = response.body().getImage();
                    if(imageUrl!=null){
                        Glide.with(DetaiContentActivity.this).load(imageUrl).into(topImage);
                    }else {
                        Glide.with(DetaiContentActivity.this)
                                .load("http://pic2.zhimg.com/71c8bcd3d99958de45ed87b8fc213224.jpg")
                                .into(topImage);
                    }

                    topTitle.setText(response.body().getTitle());
                    topSource.setText("图片:"+response.body().getImageSource());
                }else{
                    //解析失败
                    Log.d("TAG", "onResponse: 解析失败");
                }
            }

            @Override
            public void onFailure(Call<ContentBeen> call, Throwable t) {
                //失败
                Log.d("TAG", "onFailure: 响应失败");
            }
        });
    }

}
