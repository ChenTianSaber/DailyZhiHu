package com.example.chentian.myzhihudaily.service;

import com.example.chentian.myzhihudaily.been.HotNewsBeen;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chentian on 15/04/2017.
 */

public interface HotNewsService {
    @GET("hot")
    Call<HotNewsBeen> getHotNews();
}
