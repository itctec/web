package itc.ink.explorefuture_android.common_unit.mind_details.mode;

/**
 * Created by yangwenjiang on 2018/10/30.
 */

public class CommentDataMode {
    private String id="";
    private String nickname="";
    private String comment="";
    private String comment_datetime="";
    private String head_portrait_image_url="";
    private String head_portrait_image_update_datetime="";

    public CommentDataMode() {
    }

    public CommentDataMode(String id, String nickname, String comment, String comment_datetime, String head_portrait_image_url, String head_portrait_image_update_datetime) {
        this.id = id;
        this.nickname = nickname;
        this.comment = comment;
        this.comment_datetime = comment_datetime;
        this.head_portrait_image_url = head_portrait_image_url;
        this.head_portrait_image_update_datetime = head_portrait_image_update_datetime;
    }

    @Override
    public String toString() {
        return "CommentDataMode{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", comment='" + comment + '\'' +
                ", comment_datetime='" + comment_datetime + '\'' +
                ", head_portrait_image_url='" + head_portrait_image_url + '\'' +
                ", head_portrait_image_update_datetime='" + head_portrait_image_update_datetime + '\'' +
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_datetime() {
        return comment_datetime;
    }

    public void setComment_datetime(String comment_datetime) {
        this.comment_datetime = comment_datetime;
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
}
