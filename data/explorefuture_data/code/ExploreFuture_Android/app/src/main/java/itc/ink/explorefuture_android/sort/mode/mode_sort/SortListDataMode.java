package itc.ink.explorefuture_android.sort.mode.mode_sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwenjiang on 2018/9/28.
 */

public class SortListDataMode {
    private String sort_id="";
    private String sort_title="";

    private String product_left_id="";
    private String product_left_title="";
    private String product_left_summary = "";
    private String product_left_image_url="";
    private String product_left_image_update_datetime="";

    private String product_right_id="";
    private String product_right_title="";
    private String product_right_summary = "";
    private String product_right_image_url="";
    private String product_right_image_update_datetime="";

    private List<SubSortListDataMode> array_sub_sort=new ArrayList<>();

    public SortListDataMode() {
    }

    public SortListDataMode(String sort_id, String sort_title, String product_left_id, String product_left_title, String product_left_summary, String product_left_image_url, String product_left_image_update_datetime, String product_right_id, String product_right_title, String product_right_summary, String product_right_image_url, String product_right_image_update_datetime, List<SubSortListDataMode> array_sub_sort) {
        this.sort_id = sort_id;
        this.sort_title = sort_title;
        this.product_left_id = product_left_id;
        this.product_left_title = product_left_title;
        this.product_left_summary = product_left_summary;
        this.product_left_image_url = product_left_image_url;
        this.product_left_image_update_datetime = product_left_image_update_datetime;
        this.product_right_id = product_right_id;
        this.product_right_title = product_right_title;
        this.product_right_summary = product_right_summary;
        this.product_right_image_url = product_right_image_url;
        this.product_right_image_update_datetime = product_right_image_update_datetime;
        this.array_sub_sort = array_sub_sort;
    }

    @Override
    public String toString() {
        return "SortListDataMode{" +
                "sort_id='" + sort_id + '\'' +
                ", sort_title='" + sort_title + '\'' +
                ", product_left_id='" + product_left_id + '\'' +
                ", product_left_title='" + product_left_title + '\'' +
                ", product_left_summary='" + product_left_summary + '\'' +
                ", product_left_image_url='" + product_left_image_url + '\'' +
                ", product_left_image_update_datetime='" + product_left_image_update_datetime + '\'' +
                ", product_right_id='" + product_right_id + '\'' +
                ", product_right_title='" + product_right_title + '\'' +
                ", product_right_summary='" + product_right_summary + '\'' +
                ", product_right_image_url='" + product_right_image_url + '\'' +
                ", product_right_image_update_datetime='" + product_right_image_update_datetime + '\'' +
                ", array_sub_sort=" + array_sub_sort +
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

    public String getProduct_left_id() {
        return product_left_id;
    }

    public void setProduct_left_id(String product_left_id) {
        this.product_left_id = product_left_id;
    }

    public String getProduct_left_title() {
        return product_left_title;
    }

    public void setProduct_left_title(String product_left_title) {
        this.product_left_title = product_left_title;
    }

    public String getProduct_left_summary() {
        return product_left_summary;
    }

    public void setProduct_left_summary(String product_left_summary) {
        this.product_left_summary = product_left_summary;
    }

    public String getProduct_left_image_url() {
        return product_left_image_url;
    }

    public void setProduct_left_image_url(String product_left_image_url) {
        this.product_left_image_url = product_left_image_url;
    }

    public String getProduct_left_image_update_datetime() {
        return product_left_image_update_datetime;
    }

    public void setProduct_left_image_update_datetime(String product_left_image_update_datetime) {
        this.product_left_image_update_datetime = product_left_image_update_datetime;
    }

    public String getProduct_right_id() {
        return product_right_id;
    }

    public void setProduct_right_id(String product_right_id) {
        this.product_right_id = product_right_id;
    }

    public String getProduct_right_title() {
        return product_right_title;
    }

    public void setProduct_right_title(String product_right_title) {
        this.product_right_title = product_right_title;
    }

    public String getProduct_right_summary() {
        return product_right_summary;
    }

    public void setProduct_right_summary(String product_right_summary) {
        this.product_right_summary = product_right_summary;
    }

    public String getProduct_right_image_url() {
        return product_right_image_url;
    }

    public void setProduct_right_image_url(String product_right_image_url) {
        this.product_right_image_url = product_right_image_url;
    }

    public String getProduct_right_image_update_datetime() {
        return product_right_image_update_datetime;
    }

    public void setProduct_right_image_update_datetime(String product_right_image_update_datetime) {
        this.product_right_image_update_datetime = product_right_image_update_datetime;
    }

    public List<SubSortListDataMode> getArray_sub_sort() {
        return array_sub_sort;
    }

    public void setArray_sub_sort(List<SubSortListDataMode> array_sub_sort) {
        this.array_sub_sort = array_sub_sort;
    }
}
