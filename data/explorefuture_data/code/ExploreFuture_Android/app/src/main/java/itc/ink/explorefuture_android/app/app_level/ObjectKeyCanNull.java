package itc.ink.explorefuture_android.app.app_level;

import com.bumptech.glide.signature.ObjectKey;

/**
 * Created by yangwenjiang on 2018/9/26.
 */

public class ObjectKeyCanNull {
    private Object mObject;

    public ObjectKeyCanNull(Object mObject) {
        this.mObject = mObject;
    }

    public ObjectKey getObject() {
        if(mObject!=null){
            return new ObjectKey(mObject);
        }else{
            return new ObjectKey("");
        }
    }
}
