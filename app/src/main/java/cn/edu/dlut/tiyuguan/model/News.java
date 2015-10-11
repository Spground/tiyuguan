package cn.edu.dlut.tiyuguan.model;

import cn.edu.dlut.tiyuguan.base.BaseModel;

/**
 * Created by asus on 2015/10/11.
 */
public class News extends BaseModel{
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFirstImageSrc() {
        return firstImageSrc;
    }

    public void setFirstImageSrc(String firstImageSrc) {
        this.firstImageSrc = firstImageSrc;
    }

    private String time;
    private String title;
    private String url;
    private String firstImageSrc;

}
