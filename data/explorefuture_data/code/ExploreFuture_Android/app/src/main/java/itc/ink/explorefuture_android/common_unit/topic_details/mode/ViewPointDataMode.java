package itc.ink.explorefuture_android.common_unit.topic_details.mode;

import java.io.Serializable;

/**
 * Created by yangwenjiang on 2018/10/31.
 */

public class ViewPointDataMode implements Serializable{
    private String id="";
    private String title="";
    private String content="";
    private String accept_num="";
    private String oppose_num="";
    private String release_datetime="";
    private String view_point_author_id="";
    private String view_point_author_nickname="";
    private String view_point_author_head_portrait_image_url="";
    private String view_point_author_head_portrait_image_update_datetime="";
    private String comment_num="";
    private String cover_image_url="";
    private String cover_image_update_datetime="";

    public ViewPointDataMode() {
    }

    public ViewPointDataMode(String id, String title, String content, String accept_num, String oppose_num, String release_datetime, String view_point_author_id, String view_point_author_nickname, String view_point_author_head_portrait_image_url, String view_point_author_head_portrait_image_update_datetime, String comment_num, String cover_image_url, String cover_image_update_datetime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.accept_num = accept_num;
        this.oppose_num = oppose_num;
        this.release_datetime = release_datetime;
        this.view_point_author_id = view_point_author_id;
        this.view_point_author_nickname = view_point_author_nickname;
        this.view_point_author_head_portrait_image_url = view_point_author_head_portrait_image_url;
        this.view_point_author_head_portrait_image_update_datetime = view_point_author_head_portrait_image_update_datetime;
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
                ", release_datetime='" + release_datetime + '\'' +
                ", view_point_author_id='" + view_point_author_id + '\'' +
                ", view_point_author_nickname='" + view_point_author_nickname + '\'' +
                ", view_point_author_head_portrait_image_url='" + view_point_author_head_portrait_image_url + '\'' +
                ", view_point_author_head_portrait_image_update_datetime='" + view_point_author_head_portrait_image_update_datetime + '\'' +
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

    public String getRelease_datetime() {
        return release_datetime;
    }

    public void setRelease_datetime(String release_datetime) {
        this.release_datetime = release_datetime;
    }

    public String getView_point_author_id() {
        return view_point_author_id;
    }

    public void setView_point_author_id(String view_point_author_id) {
        this.view_point_author_id = view_point_author_id;
    }

    public String getView_point_author_nickname() {
        return view_point_author_nickname;
    }

    public void setView_point_author_nickname(String view_point_author_nickname) {
        this.view_point_author_nickname = view_point_author_nickname;
    }

    public String getView_point_author_head_portrait_image_url() {
        return view_point_author_head_portrait_image_url;
    }

    public void setView_point_author_head_portrait_image_url(String view_point_author_head_portrait_image_url) {
        this.view_point_author_head_portrait_image_url = view_point_author_head_portrait_image_url;
    }

    public String getView_point_author_head_portrait_image_update_datetime() {
        return view_point_author_head_portrait_image_update_datetime;
    }

    public void setView_point_author_head_portrait_image_update_datetime(String view_point_author_head_portrait_image_update_datetime) {
        this.view_point_author_head_portrait_image_update_datetime = view_point_author_head_portrait_image_update_datetime;
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
