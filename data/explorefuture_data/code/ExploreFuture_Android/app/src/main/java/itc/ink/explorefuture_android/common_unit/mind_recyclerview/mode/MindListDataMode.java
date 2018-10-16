package itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwenjiang on 2018/9/28.
 */

public class MindListDataMode {
    public static final String STATE_UN_RELEASE="UN_RELEASE";
    public static final String STATE_COMPACTED="COMPACTED";
    private String id="";
    private String name="昵称";
    private String datetime="";
    private String release_state="";
    private String compact_state="";
    private String content_text="";
    private String accept_num="";
    private String comment_num="";
    private String retransmission_num="";
    private String head_portrait_image_url="";
    private String head_portrait_image_update_datetime="";
    private List<String> image_url_list=new ArrayList<>();
    private String video_url="";

    public MindListDataMode() {
    }

    public MindListDataMode(String id, String name, String datetime, String release_state, String compact_state, String content_text, String accept_num, String comment_num, String retransmission_num, String head_portrait_image_url, String head_portrait_image_update_datetime, List<String> image_url_list, String video_url) {
        this.id = id;
        this.name = name;
        this.datetime = datetime;
        this.release_state = release_state;
        this.compact_state = compact_state;
        this.content_text = content_text;
        this.accept_num = accept_num;
        this.comment_num = comment_num;
        this.retransmission_num = retransmission_num;
        this.head_portrait_image_url = head_portrait_image_url;
        this.head_portrait_image_update_datetime = head_portrait_image_update_datetime;
        this.image_url_list = image_url_list;
        this.video_url = video_url;
    }

    @Override
    public String toString() {
        return "MindListDataMode{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", datetime='" + datetime + '\'' +
                ", release_state='" + release_state + '\'' +
                ", compact_state='" + compact_state + '\'' +
                ", content_text='" + content_text + '\'' +
                ", accept_num='" + accept_num + '\'' +
                ", comment_num='" + comment_num + '\'' +
                ", retransmission_num='" + retransmission_num + '\'' +
                ", head_portrait_image_url='" + head_portrait_image_url + '\'' +
                ", head_portrait_image_update_datetime='" + head_portrait_image_update_datetime + '\'' +
                ", image_url_list=" + image_url_list +
                ", video_url='" + video_url + '\'' +
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRelease_state() {
        return release_state;
    }

    public void setRelease_state(String release_state) {
        this.release_state = release_state;
    }

    public String getCompact_state() {
        return compact_state;
    }

    public void setCompact_state(String compact_state) {
        this.compact_state = compact_state;
    }

    public String getContent_text() {
        return content_text;
    }

    public void setContent_text(String content_text) {
        this.content_text = content_text;
    }

    public String getAccept_num() {
        return accept_num;
    }

    public void setAccept_num(String accept_num) {
        this.accept_num = accept_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getRetransmission_num() {
        return retransmission_num;
    }

    public void setRetransmission_num(String retransmission_num) {
        this.retransmission_num = retransmission_num;
    }

    public String getHead_portrait_image_url() {
        return head_portrait_image_url;
    }

    public void setHead_portrait_image_url(String head_portrait_image_url) {
        this.head_portrait_image_url = head_portrait_image_url;
    }

    public String getHead_portrait_image_update_datetime() {
        return head_portrait_image_update_datetime;
    }

    public void setHead_portrait_image_update_datetime(String head_portrait_image_update_datetime) {
        this.head_portrait_image_update_datetime = head_portrait_image_update_datetime;
    }

    public List<String> getImage_url_list() {
        return image_url_list;
    }

    public void setImage_url_list(List<String> image_url_list) {
        this.image_url_list = image_url_list;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
