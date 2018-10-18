package itc.ink.explorefuture_android.mind.edit_activity.mode;

/**
 * Created by yangwenjiang on 2018/10/17.
 */

public class MindEditImageListDataMode {
    private String image_url="";
    private boolean isImage=true;

    public MindEditImageListDataMode() {
    }

    public MindEditImageListDataMode(String image_url, boolean isImage) {
        this.image_url = image_url;
        this.isImage = isImage;
    }

    @Override
    public String toString() {
        return "MindEditImageListDataMode{" +
                "image_url='" + image_url + '\'' +
                ", isImage=" + isImage +
                '}';
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }
}
