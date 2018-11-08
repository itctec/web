package itc.ink.explorefuture_android.mine.settings.mode;

/**
 * Created by yangwenjiang on 2018/11/8.
 */

public class HelpCenterDataMode {
    private String id="";
    private String title="";

    public HelpCenterDataMode() {
    }

    public HelpCenterDataMode(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return "HelpCenterDataMode{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
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
}
