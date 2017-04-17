package com.example.chentian.myzhihudaily.service;

import com.example.chentian.myzhihudaily.been.ThemeContentBeen;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by chentian on 16/04/2017.
 */

public interface ThemeContentService {
    @GET("{id}")
    Call<ThemeContentBeen> getThemeContent(@Path("id") String themeId);
}
