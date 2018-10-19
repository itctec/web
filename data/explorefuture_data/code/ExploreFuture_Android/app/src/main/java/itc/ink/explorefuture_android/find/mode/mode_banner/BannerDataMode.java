package itc.ink.explorefuture_android.find.mode.mode_banner;

/**
 * Created by yangwenjiang on 2018/9/26.
 */

public class BannerDataMode {
    private String id = "";
    private String imageurl = "";
    private String image_update_datetime = "";

    public BannerDataMode() {
    }

    public BannerDataMode(String id, String imageurl, String image_update_datetime) {
        this.id = id;
        this.imageurl = imageurl;
        this.image_update_datetime = image_update_datetime;
    }

    @Override
    public String toString() {
        return "BannerDataMode{" +
                "id='" + id + '\'' +
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
