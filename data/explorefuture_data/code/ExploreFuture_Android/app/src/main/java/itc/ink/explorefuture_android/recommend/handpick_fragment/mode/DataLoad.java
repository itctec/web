package itc.ink.explorefuture_android.recommend.handpick_fragment.mode;

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
         * 准备数据
         */
        boolean prepareData(Context mContext);

        /**
         * 加载Banner数据
         */
        Object loadBannerData(Context mContext);

        /**
         * 加载Solution数据
         */
        Object loadSolutionData(Context mContext);

        /**
         * 加载Action数据
         */
        Object loadActionSubjectData(Context mContext);

        /**
         * 加载Action数据
         */
        Object loadActionListData(Context mContext);

        /**
         * 加载Product数据
         */
        Object loadProductData(Context mContext);

        /**
         * 加载Interest数据
         */
        Object loadInterestData(Context mContext);

    }
}
