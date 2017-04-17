package com.example.chentian.myzhihudaily.api;

/**
 * Created by chentian on 11/04/2017.
 */

public class ZhiHuDailyAPI {
    //这个是获取消息的列表，斜杠后面加日期
    public static String CONTENT_LIST = "http://news-at.zhihu.com/api/4/news/before/";

    //这个是获取详细的内容，斜杠后面加的是文章的id
    public static String CONTENT_DETAIL = "http://news-at.zhihu.com/api/4/news/";

    //这个是获取热门消息
    public static String HOT_NEWS = "http://news-at.zhihu.com/api/3/news/";

    //这个是获取主题日报
    public static String THEME_CONTENT= "http://news-at.zhihu.com/api/4/theme/";
}
