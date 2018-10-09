package itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic;

/**
 * Created by yangwenjiang on 2018/10/9.
 */

public class TopicListDataMode {
    private String id="";
    private String image_url="";
    private String image_update_datetime="";

    public TopicListDataMode() {
    }

    public TopicListDataMode(String id, String image_url, String image_update_datetime) {
        this.id = id;
        this.image_url = image_url;
        this.image_update_datetime = image_update_datetime;
    }

    @Override
    public String toString() {
        return "TopicListDataMode{" +
                "id='" + id + '\'' +
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
