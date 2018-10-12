package itc.ink.explorefuture_android.sort.mode.mode_sort;

/**
 * Created by yangwenjiang on 2018/10/11.
 */

public class SubSortListDataMode {
    private String sort_id="";
    private String sort_title="";
    private String sort_summary="";
    private String image_url = "";
    private String image_update_datetime="";

    public SubSortListDataMode() {
    }

    public SubSortListDataMode(String sort_id, String sort_title, String sort_summary, String image_url, String image_update_datetime) {
        this.sort_id = sort_id;
        this.sort_title = sort_title;
        this.sort_summary = sort_summary;
        this.image_url = image_url;
        this.image_update_datetime = image_update_datetime;
    }

    @Override
    public String toString() {
        return "SubSortListDataMode{" +
                "sort_id='" + sort_id + '\'' +
                ", sort_title='" + sort_title + '\'' +
                ", sort_summary='" + sort_summary + '\'' +
                ", image_url='" + image_url + '\'' +
                ", image_update_datetime='" + image_update_datetime + '\'' +
                '}';
    }

    public String getSort_id() {
        return sort_id;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getSort_title() {
        return sort_title;
    }

    public void setSort_title(String sort_title) {
        this.sort_title = sort_title;
    }

    public String getSort_summary() {
        return sort_summary;
    }

    public void setSort_summary(String sort_summary) {
        this.sort_summary = sort_summary;
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
