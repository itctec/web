package itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_action;

/**
 * Created by yangwenjiang on 2018/9/13.
 */

public class ActionListDataModel {
    private String id = "";
    private String title = "活动名称";
    private String publicitytext = "活动宣传语";
    private String datetime = "活动时间";
    private String site = "活动地点";
    private String summary = "活动描述";
    private String imageurl = "";
    private String image_update_datetime = "";

    public ActionListDataModel() {
    }

    public ActionListDataModel(String id, String title, String publicitytext, String datetime, String site, String summary, String imageurl, String image_update_datetime) {
        this.id = id;
        this.title = title;
        this.publicitytext = publicitytext;
        this.datetime = datetime;
        this.site = site;
        this.summary = summary;
        this.imageurl = imageurl;
        this.image_update_datetime = image_update_datetime;
    }

    @Override
    public String toString() {
        return "ActionListDataModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", publicitytext='" + publicitytext + '\'' +
                ", datetime='" + datetime + '\'' +
                ", site='" + site + '\'' +
                ", summary='" + summary + '\'' +
                ", imageurl='" + imageurl + '\'' +
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

    public String getPublicitytext() {
        return publicitytext;
    }

    public void setPublicitytext(String publicitytext) {
        this.publicitytext = publicitytext;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getImage_update_datetime() {
        return image_update_datetime;
    }

    public void setImage_update_datetime(String image_update_datetime) {
        this.image_update_datetime = image_update_datetime;
    }
}
