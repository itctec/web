package itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic;

import java.io.Serializable;

/**
 * Created by yangwenjiang on 2018/10/9.
 */

public class TopicListDataMode implements Serializable{
    private String id="";
    private String title="";
    private String summary="";
    private String image_url="";
    private String image_update_datetime="";

    public TopicListDataMode() {
    }

    public TopicListDataMode(String id, String title, String summary, String image_url, String image_update_datetime) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.image_url = image_url;
        this.image_update_datetime = image_update_datetime;
    }

    @Override
    public String toString() {
        return "TopicListDataMode{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
