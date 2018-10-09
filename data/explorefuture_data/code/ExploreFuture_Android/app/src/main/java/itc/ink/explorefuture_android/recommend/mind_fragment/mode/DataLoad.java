package itc.ink.explorefuture_android.recommend.mind_fragment.mode;

import android.content.Context;

/**
 * Created by yangwenjiang on 2018/9/16.
 */

public class DataLoad {
    public static final int IMPLEMENT_NET_RESOURCE = 0X01;

    public OutService outService = null;

    public DataLoad(Context mContext) {
        setServiceInterfaceImplement(mContext, IMPLEMENT_NET_RESOURCE);
    }

    public DataLoad(Context mContext, int mImplementCode) {
        setServiceInterfaceImplement(mContext, mImplementCode);
    }

    private void setServiceInterfaceImplement(Context mContext, int mImplementCode) {
        switch (mImplementCode) {
            case IMPLEMENT_NET_RESOURCE:
                outService = new DefResDataLoader();
                break;
            default:
                outService = new DefResDataLoader();
        }
    }

    public interface OutService {
        /**
         * 准备数据数据
         */
        boolean prepareData(Context mContext);

        /**
         * 加载Topic数据
         */
        Object loadTopicData(Context mContext);

        /**
         * 加载Mind数据
         */
        Object loadMindData(Context mContext);



    }
}
