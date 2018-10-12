package itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_product;

/**
 * Created by yangwenjiang on 2018/9/17.
 */

public class ProductDataMode {
    private String id="";
    private String title = "产品名称";
    private String summary = "产品描述";
    private String imageurl = "";
    private String image_update_datetime = "";
    private String imageurl_left = "";
    private String image_left_update_datetime = "";
    private String imageurl_right = "";
    private String image_right_update_datetime = "";

    public ProductDataMode() {
    }

    public ProductDataMode(String id, String title, String summary, String imageurl, String image_update_datetime, String imageurl_left, String image_left_update_datetime, String imageurl_right, String image_right_update_datetime) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.imageurl = imageurl;
        this.image_update_datetime = image_update_datetime;
        this.imageurl_left = imageurl_left;
        this.image_left_update_datetime = image_left_update_datetime;
        this.imageurl_right = imageurl_right;
        this.image_right_update_datetime = image_right_update_datetime;
    }

    @Override
    public String toString() {
        return "ProductDataMode{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", image_update_datetime='" + image_update_datetime + '\'' +
                ", imageurl_left='" + imageurl_left + '\'' +
                ", image_left_update_datetime='" + image_left_update_datetime + '\'' +
                ", imageurl_right='" + imageurl_right + '\'' +
                ", image_right_update_datetime='" + image_right_update_datetime + '\'' +
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

    public String getImageurl_left() {
        return imageurl_left;
    }

    public void setImageurl_left(String imageurl_left) {
        this.imageurl_left = imageurl_left;
    }

    public String getImage_left_update_datetime() {
        return image_left_update_datetime;
    }

    public void setImage_left_update_datetime(String image_left_update_datetime) {
        this.image_left_update_datetime = image_left_update_datetime;
    }

    public String getImageurl_right() {
        return imageurl_right;
    }

    public void setImageurl_right(String imageurl_right) {
        this.imageurl_right = imageurl_right;
    }

    public String getImage_right_update_datetime() {
        return image_right_update_datetime;
    }

    public void setImage_right_update_datetime(String image_right_update_datetime) {
        this.image_right_update_datetime = image_right_update_datetime;
    }
}
