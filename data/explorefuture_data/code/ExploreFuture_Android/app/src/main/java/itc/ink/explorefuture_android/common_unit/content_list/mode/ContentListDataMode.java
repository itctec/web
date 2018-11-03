package itc.ink.explorefuture_android.common_unit.content_list.mode;

/**
 * Created by yangwenjiang on 2018/11/2.
 */

public class ContentListDataMode {
    private String id="";
    private String title = "产品/方案名称";
    private String summary = "产品/方案描述";
    private String price = "00.00";
    private String tag = "标签";
    private String support_num = "支持票数";
    private String image_url = "";
    private String image_update_datetime = "";

    public ContentListDataMode() {
    }

    public ContentListDataMode(String id, String title, String summary, String price, String tag, String support_num, String image_url, String image_update_datetime) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.price = price;
        this.tag = tag;
        this.support_num = support_num;
        this.image_url = image_url;
        this.image_update_datetime = image_update_datetime;
    }

    @Override
    public String toString() {
        return "ContentListDataMode{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", price='" + price + '\'' +
                ", tag='" + tag + '\'' +
                ", support_num='" + support_num + '\'' +
                ", image_url='" + image_url + '\'' +
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSupport_num() {
        return support_num;
    }

    public void setSupport_num(String support_num) {
        this.support_num = support_num;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_update_datetime() {
        return image_update_datetime;
    }

    public void setImage_update_datetime(String image_update_datetime) {
        this.image_update_datetime = image_update_datetime;
    }
}
