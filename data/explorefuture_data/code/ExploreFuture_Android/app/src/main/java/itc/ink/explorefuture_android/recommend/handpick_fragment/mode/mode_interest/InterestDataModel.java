package itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_interest;

/**
 * Created by yangwenjiang on 2018/9/13.
 */

public class InterestDataModel {
    private String id="";
    private String title = "产品/方案名称";
    private String summary = "产品/方案描述";
    private String supportnum = "支持票数";
    private String imageurl = "";
    private String image_update_datetime = "";

    public InterestDataModel() {
    }

    public InterestDataModel(String id, String title, String summary, String supportnum, String imageurl, String image_update_datetime) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.supportnum = supportnum;
        this.imageurl = imageurl;
        this.image_update_datetime = image_update_datetime;
    }

    @Override
    public String toString() {
        return "InterestDataModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", supportnum='" + supportnum + '\'' +
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSupportnum() {
        return supportnum;
    }

    public void setSupportnum(String supportnum) {
        this.supportnum = supportnum;
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
