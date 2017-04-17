package com.example.chentian.myzhihudaily.service;

import com.example.chentian.myzhihudaily.been.ContentListBeen;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by chentian on 11/04/2017.
 */

public interface ContentListService {
    @GET("{date}")
    Call<ContentListBeen> getContentList(@Path("date") String date);
}
