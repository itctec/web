package itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic;

/**
 * Created by yangwenjiang on 2018/10/9.
 */

public class TopicListDataMode {
    private String id="";
    private String title="";
    private String image_url="";
    private String image_update_datetime="";
    private String accept_num="";

    public TopicListDataMode() {
    }

    public TopicListDataMode(String id, String title, String image_url, String image_update_datetime, String accept_num) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
        this.image_update_datetime = image_update_datetime;
        this.accept_num = accept_num;
    }

    @Override
    public String toString() {
        return "TopicListDataMode{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", image_url='" + image_url + '\'' +
                ", image_update_datetime='" + image_update_datetime + '\'' +
                ", accept_num='" + accept_num + '\'' +
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

    public String getAccept_num() {
        return accept_num;
    }

    public void setAccept_num(String accept_num) {
        this.accept_num = accept_num;
    }
}
