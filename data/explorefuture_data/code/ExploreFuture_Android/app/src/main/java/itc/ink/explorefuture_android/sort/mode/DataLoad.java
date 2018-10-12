package itc.ink.explorefuture_android.sort.mode;

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
        boolean prepareData();

        /**
         * 加载Sort数据
         */
        Object loadSortData();



    }
}
