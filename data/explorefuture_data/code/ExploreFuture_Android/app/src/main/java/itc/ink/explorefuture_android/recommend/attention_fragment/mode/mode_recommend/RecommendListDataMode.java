package itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_recommend;

import java.io.Serializable;

/**
 * Created by yangwenjiang on 2018/9/28.
 */

public class RecommendListDataMode{
    private String id="";
    private String name="昵称";
    private String summary="简介";
    private String image_url="";
    private String image_update_datetime = "";

    public RecommendListDataMode() {
    }

    public RecommendListDataMode(String id, String name, String summary, String image_url, String image_update_datetime) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.image_url = image_url;
        this.image_update_datetime = image_update_datetime;
    }

    @Override
    public String toString() {
        return "SortListDataMode{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", image_url='" + image_url + '\'' +
                ", image_update_datetime='" + image_update_datetime + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_update_datetime() {
        return image_update_datetime;
    }

    public void setImage_update_datetime(String image_update_datetime) {
        this.image_update_datetime = image_update_datetime;
    }
}
