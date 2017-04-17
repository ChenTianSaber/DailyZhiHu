package com.example.chentian.myzhihudaily.service;

import com.example.chentian.myzhihudaily.been.ContentBeen;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by chentian on 12/04/2017.
 */

public interface ContentService {
    @GET("{id}")
    Call<ContentBeen> getContent(@Path("id") String contentId);
}
