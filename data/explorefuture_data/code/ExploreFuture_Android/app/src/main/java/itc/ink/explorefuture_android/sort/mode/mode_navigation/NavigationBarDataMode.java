package itc.ink.explorefuture_android.sort.mode.mode_navigation;

/**
 * Created by yangwenjiang on 2018/10/11.
 */

public class NavigationBarDataMode {
    private String id="";
    private String title="";

    public NavigationBarDataMode() {
    }

    public NavigationBarDataMode(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return "NavigationBarDataMode{" +
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
