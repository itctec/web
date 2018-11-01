package itc.ink.explorefuture_android.common_unit.person_details.mode;

import java.io.Serializable;

/**
 * Created by yangwenjiang on 2018/11/1.
 */

public class SimplePersonInfoDataMode implements Serializable{
    private String id="";
    private String nickname="";
    private String head_portrait_image_url="";
    private String head_portrait_image_update_datetime="";

    public SimplePersonInfoDataMode(String id) {
        this.id = id;
    }

    public SimplePersonInfoDataMode(String id, String nickname, String head_portrait_image_url, String head_portrait_image_update_datetime) {
        this.id = id;
        this.nickname = nickname;
        this.head_portrait_image_url = head_portrait_image_url;
        this.head_portrait_image_update_datetime = head_portrait_image_update_datetime;
    }

    @Override
    public String toString() {
        return "SimplePersonInfoDataMode{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
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
