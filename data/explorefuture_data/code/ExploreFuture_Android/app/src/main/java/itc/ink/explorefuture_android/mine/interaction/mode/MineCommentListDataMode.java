package itc.ink.explorefuture_android.mine.interaction.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;

/**
 * Created by yangwenjiang on 2018/9/28.
 */

public class MineCommentListDataMode implements Serializable{
    private String id="";
    private String nickname="";
    private String datetime="";
    private String comment_text="";
    private String head_portrait_image_url="";
    private String head_portrait_image_update_datetime="";
    private MindListDataMode mind_item;

    public MineCommentListDataMode() {
    }

    public MineCommentListDataMode(String id, String nickname, String datetime, String comment_text, String head_portrait_image_url, String head_portrait_image_update_datetime, MindListDataMode mind_item) {
        this.id = id;
        this.nickname = nickname;
        this.datetime = datetime;
        this.comment_text = comment_text;
        this.head_portrait_image_url = head_portrait_image_url;
        this.head_portrait_image_update_datetime = head_portrait_image_update_datetime;
        this.mind_item = mind_item;
    }

    @Override
    public String toString() {
        return "MineCommentListDataMode{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", datetime='" + datetime + '\'' +
                ", comment_text='" + comment_text + '\'' +
                ", head_portrait_image_url='" + head_portrait_image_url + '\'' +
                ", head_portrait_image_update_datetime='" + head_portrait_image_update_datetime + '\'' +
                ", mind_item=" + mind_item +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
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

    public MindListDataMode getMind_item() {
        return mind_item;
    }

    public void setMind_item(MindListDataMode mind_item) {
        this.mind_item = mind_item;
    }
}
