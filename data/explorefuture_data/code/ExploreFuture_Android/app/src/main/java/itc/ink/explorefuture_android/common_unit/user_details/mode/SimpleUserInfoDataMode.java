package itc.ink.explorefuture_android.common_unit.user_details.mode;

import java.io.Serializable;

/**
 * Created by yangwenjiang on 2018/11/1.
 */

public class SimpleUserInfoDataMode implements Serializable{
    private String id="";
    private String nickname="";
    private String str_personalized_signature="";
    private String str_fans_count ="";
    private String head_portrait_image_url="";
    private String head_portrait_image_update_datetime="";

    public SimpleUserInfoDataMode() {
    }

    public SimpleUserInfoDataMode(String id, String nickname, String str_personalized_signature, String str_fans_count, String head_portrait_image_url, String head_portrait_image_update_datetime) {
        this.id = id;
        this.nickname = nickname;
        this.str_personalized_signature = str_personalized_signature;
        this.str_fans_count = str_fans_count;
        this.head_portrait_image_url = head_portrait_image_url;
        this.head_portrait_image_update_datetime = head_portrait_image_update_datetime;
    }

    @Override
    public String toString() {
        return "SimpleUserInfoDataMode{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", str_personalized_signature='" + str_personalized_signature + '\'' +
                ", str_fans_count='" + str_fans_count + '\'' +
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

    public String getStr_personalized_signature() {
        return str_personalized_signature;
    }

    public void setStr_personalized_signature(String str_personalized_signature) {
        this.str_personalized_signature = str_personalized_signature;
    }

    public String getStr_fans_count() {
        return str_fans_count;
    }

    public void setStr_fans_count(String str_fans_count) {
        this.str_fans_count = str_fans_count;
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
