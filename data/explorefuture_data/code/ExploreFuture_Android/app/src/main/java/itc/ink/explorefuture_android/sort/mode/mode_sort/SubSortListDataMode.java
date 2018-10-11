package itc.ink.explorefuture_android.sort.mode.mode_sort;

/**
 * Created by yangwenjiang on 2018/10/11.
 */

public class SubSortListDataMode {
    private String sort_id="";
    private String sort_title="";
    private String sort_summary="";
    private String left_image_url = "";
    private String left_image_update_datetime="";
    private String right_image_url="";
    private String right_image_update_datetime="";

    public SubSortListDataMode() {
    }

    public SubSortListDataMode(String sort_id, String sort_title, String sort_summary, String left_image_url, String left_image_update_datetime, String right_image_url, String right_image_update_datetime) {
        this.sort_id = sort_id;
        this.sort_title = sort_title;
        this.sort_summary = sort_summary;
        this.left_image_url = left_image_url;
        this.left_image_update_datetime = left_image_update_datetime;
        this.right_image_url = right_image_url;
        this.right_image_update_datetime = right_image_update_datetime;
    }

    @Override
    public String toString() {
        return "SubSortListDataMode{" +
                "sort_id='" + sort_id + '\'' +
                ", sort_title='" + sort_title + '\'' +
                ", sort_summary='" + sort_summary + '\'' +
                ", left_image_url='" + left_image_url + '\'' +
                ", left_image_update_datetime='" + left_image_update_datetime + '\'' +
                ", right_image_url='" + right_image_url + '\'' +
                ", right_image_update_datetime='" + right_image_update_datetime + '\'' +
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

    public String getLeft_image_url() {
        return left_image_url;
    }

    public void setLeft_image_url(String left_image_url) {
        this.left_image_url = left_image_url;
    }

    public String getLeft_image_update_datetime() {
        return left_image_update_datetime;
    }

    public void setLeft_image_update_datetime(String left_image_update_datetime) {
        this.left_image_update_datetime = left_image_update_datetime;
    }

    public String getRight_image_url() {
        return right_image_url;
    }

    public void setRight_image_url(String right_image_url) {
        this.right_image_url = right_image_url;
    }

    public String getRight_image_update_datetime() {
        return right_image_update_datetime;
    }

    public void setRight_image_update_datetime(String right_image_update_datetime) {
        this.right_image_update_datetime = right_image_update_datetime;
    }
}
