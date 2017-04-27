package com.example.chentian.myzhihudaily.database;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by chentian on 27/04/2017.
 */

public class Artical extends DataSupport {

    private String articalBody;

    @Column(unique = true)
    private String articalTitle;

    public String getArticalBody() {
        return articalBody;
    }

    public void setArticalBody(String articalBody) {
        this.articalBody = articalBody;
    }

    public String getArticalTitle() {
        return articalTitle;
    }

    public void setArticalTitle(String articalTitle) {
        this.articalTitle = articalTitle;
    }
}
