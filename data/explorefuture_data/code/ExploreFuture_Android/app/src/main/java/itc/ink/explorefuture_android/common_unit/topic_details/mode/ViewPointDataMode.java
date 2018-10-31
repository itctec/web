package itc.ink.explorefuture_android.common_unit.topic_details.mode;

/**
 * Created by yangwenjiang on 2018/10/31.
 */

public class ViewPointDataMode {
    private String id="";
    private String title="";
    private String content="";
    private String accept_num="";
    private String oppose_num="";
    private String comment_num="";
    private String cover_image_url="";
    private String cover_image_update_datetime="";

    public ViewPointDataMode() {
    }

    public ViewPointDataMode(String id, String title, String content, String accept_num, String oppose_num, String comment_num, String cover_image_url, String cover_image_update_datetime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.accept_num = accept_num;
        this.oppose_num = oppose_num;
        this.comment_num = comment_num;
        this.cover_image_url = cover_image_url;
        this.cover_image_update_datetime = cover_image_update_datetime;
    }

    @Override
    public String toString() {
        return "ViewPointDataMode{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", accept_num='" + accept_num + '\'' +
                ", oppose_num='" + oppose_num + '\'' +
                ", comment_num='" + comment_num + '\'' +
                ", cover_image_url='" + cover_image_url + '\'' +
                ", cover_image_update_datetime='" + cover_image_update_datetime + '\'' +
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAccept_num() {
        return accept_num;
    }

    public void setAccept_num(String accept_num) {
        this.accept_num = accept_num;
    }

    public String getOppose_num() {
        return oppose_num;
    }

    public void setOppose_num(String oppose_num) {
        this.oppose_num = oppose_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getCover_image_url() {
        return cover_image_url;
    }

    public void setCover_image_url(String cover_image_url) {
        this.cover_image_url = cover_image_url;
    }

    public String getCover_image_update_datetime() {
        return cover_image_update_datetime;
    }

    public void setCover_image_update_datetime(String cover_image_update_datetime) {
        this.cover_image_update_datetime = cover_image_update_datetime;
    }
}
