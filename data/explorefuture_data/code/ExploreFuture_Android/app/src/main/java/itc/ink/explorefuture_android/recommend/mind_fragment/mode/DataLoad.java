package itc.ink.explorefuture_android.recommend.mind_fragment.mode;

import android.content.Context;

/**
 * Created by yangwenjiang on 2018/9/16.
 */

public class DataLoad {
    public static final int IMPLEMENT_NET_RESOURCE = 0X01;

    public OutService outService = null;

    public DataLoad() {
        setServiceInterfaceImplement(IMPLEMENT_NET_RESOURCE);
    }

    public DataLoad(int mImplementCode) {
        setServiceInterfaceImplement(mImplementCode);
    }

    private void setServiceInterfaceImplement(int mImplementCode) {
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
        Object loadTopicData();

        /**
         * 加载Mind Hottest数据
         */
        Object loadMindHottestData();

        /**
         * 加载Mind Newest数据
         */
        Object loadMindNewestData();

    }
}
