package itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_action;

/**
 * Created by yangwenjiang on 2018/9/17.
 */

public class ActionSubjectDataMode {
    private String id = "";
    private String title = "TITLE";
    private String summary = "SUMMARY";
    private String gifurl = "";
    private String gif_update_datetime = "";

    public ActionSubjectDataMode() {
    }

    public ActionSubjectDataMode(String id, String title, String summary, String gifurl, String gif_update_datetime) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.gifurl = gifurl;
        this.gif_update_datetime = gif_update_datetime;
    }

    @Override
    public String toString() {
        return "ActionSubjectDataMode{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", gifurl='" + gifurl + '\'' +
                ", gif_update_datetime='" + gif_update_datetime + '\'' +
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

    public String getGifurl() {
        return gifurl;
    }

    public void setGifurl(String gifurl) {
        this.gifurl = gifurl;
    }

    public String getGif_update_datetime() {
        return gif_update_datetime;
    }

    public void setGif_update_datetime(String gif_update_datetime) {
        this.gif_update_datetime = gif_update_datetime;
    }
}
