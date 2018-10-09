package itc.ink.explorefuture_android.recommend.mind_fragment.adapter;

import android.content.Context;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youth.banner.Banner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.activity.MainActivity;
import itc.ink.explorefuture_android.app.app_level.mind_recyclerview.adapter.MindDataAdapter;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.app_level.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.recommend.mind_fragment.MindFragment;
import itc.ink.explorefuture_android.recommend.mind_fragment.adapter.implement.TopicDelegateImplement;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.DataLoad;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic.TopicListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class MindWrapAdapter extends RecyclerView.Adapter<MindWrapAdapter.WrapperVH> {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "MindWrapAdapter";
    private Context mContext;

    public static Handler mHandler;

    private ArrayList<TopicListDataMode> mTopicData;
    private ArrayList<MindListDataMode> mMindListData;
    private MindDataAdapter mMindDataAdapter;

    private DelegateInterface mDelegateInterface;

    public MindWrapAdapter(Context mContext, ArrayList<TopicListDataMode> mTopicData, ArrayList<MindListDataMode> mMindListData) {
        initHandler();
        this.mContext = mContext;
        this.mTopicData = mTopicData;
        this.mMindListData = mMindListData;
        this.mMindDataAdapter = new MindDataAdapter(mContext, mMindListData);
    }

    @Override
    public WrapperVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.TOPIC_LIST.ordinal()) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_mind_fragment_topic_item, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.TOPIC_LIST);
        } else if (viewType == ITEM_TYPE.SORT_NAVIGATION.ordinal()) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_mind_fragment_mind_top_navigation, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.SORT_NAVIGATION);
        } else {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_attention_fragment_attention_item, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.MIND_LIST);
        }
    }

    @Override
    public void onBindViewHolder(WrapperVH holder, final int position) {
        if (position == 0) {
            mDelegateInterface = new TopicDelegateImplement();
            mDelegateInterface.handleTransaction(mContext, holder, mTopicData);
        } else if (position == 1) {
            holder.hottestBtn.setOnClickListener(new HottestBtnClickListener(holder));
            holder.newestBtn.setOnClickListener(new NewestBtnClickListener(holder));
        } else {
            if (holder.mindRecyclerView.getAdapter() == null) {
                holder.mindRecyclerView.setAdapter(mMindDataAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(mContext);
                holder.mindRecyclerView.setLayoutManager(contentRvLayoutManager);
            }
        }

    }


    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.TOPIC_LIST.ordinal();
        } else if (position == 1) {
            return ITEM_TYPE.SORT_NAVIGATION.ordinal();
        } else {
            return ITEM_TYPE.MIND_LIST.ordinal();
        }
    }

    public class WrapperVH extends RecyclerView.ViewHolder {
        /*Topic Banner*/
        public Banner topicBanner;

        /*Sort Navigation*/
        private TextView hottestBtn;
        private TextView newestBtn;
        private View navigationIndicator;

        /*Mind RecyclerView*/
        public RecyclerView mindRecyclerView;

        public WrapperVH(View view, ITEM_TYPE item_type) {
            super(view);
            if (item_type == ITEM_TYPE.TOPIC_LIST) {
                topicBanner = view.findViewById(R.id.recommend_Mind_Topic_Banner);
            } else if (item_type == ITEM_TYPE.SORT_NAVIGATION) {
                hottestBtn = view.findViewById(R.id.mind_Sort_Top_Navigation_Hottest_Btn);
                newestBtn = view.findViewById(R.id.mind_Sort_Top_Navigation_Newest_Btn);
                navigationIndicator = view.findViewById(R.id.mind_Sort_Top_Navigation_Indicator);
            } else {
                mindRecyclerView = view.findViewById(R.id.recommend_Attention_Mind_RecyclerView);
            }
        }
    }

    private enum ITEM_TYPE {
        TOPIC_LIST,
        SORT_NAVIGATION,
        MIND_LIST
    }

    class HottestBtnClickListener implements View.OnClickListener {
        WrapperVH holder;

        public HottestBtnClickListener(WrapperVH holder) {
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            updateNavigationTopBtnState(holder, view);

            List<DataUpdateMode> dataUpdateList = new ArrayList<>();
            DataUpdateMode recommend_Mind_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_DATA_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.RECOMMEND_MIND_LOCAL_DATA_FILE_NAME,
                    false);
            dataUpdateList.add(recommend_Mind_Data_UpdateMode);

            DataUpdateUtil dataUpdateUtil = new DataUpdateUtil(mContext, dataUpdateList, mHandler);
            dataUpdateUtil.updateData();

            //Bug Point
            DataLoad mDataLoad = new DataLoad(mContext);
            if (mDataLoad.outService.prepareData(mContext)) {
                mMindListData.clear();
                for (MindListDataMode mindListDataMode : (ArrayList<MindListDataMode>) mDataLoad.outService.loadMindData(mContext)) {
                    mMindListData.add(mindListDataMode);
                }
            }
            mMindDataAdapter.notifyDataSetChanged();
        }
    }

    class NewestBtnClickListener implements View.OnClickListener {
        WrapperVH holder;

        public NewestBtnClickListener(WrapperVH holder) {
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            updateNavigationTopBtnState(holder, view);

            List<DataUpdateMode> dataUpdateList = new ArrayList<>();
            DataUpdateMode recommend_Mind_Data_UpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_DATA_FILE_URL,
                    DataUpdateMode.RECOMMEND_MIND_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                    DataUpdateMode.RECOMMEND_MIND_LOCAL_DATA_FILE_NAME,
                    false);
            dataUpdateList.add(recommend_Mind_Data_UpdateMode);

            DataUpdateUtil dataUpdateUtil = new DataUpdateUtil(mContext, dataUpdateList, mHandler);
            dataUpdateUtil.updateData();

            //Bug Point
            DataLoad mDataLoad = new DataLoad(mContext);
            if (mDataLoad.outService.prepareData(mContext)) {
                mMindListData.clear();
                for (MindListDataMode mindListDataMode : (ArrayList<MindListDataMode>) mDataLoad.outService.loadMindData(mContext)) {
                    mMindListData.add(mindListDataMode);
                }
            }
            mMindDataAdapter.notifyDataSetChanged();
        }
    }

    private void updateNavigationTopBtnState(WrapperVH holder, View currentActiveBtn) {
        holder.hottestBtn.setAlpha(0.5F);
        holder.newestBtn.setAlpha(0.5F);

        currentActiveBtn.setAlpha(1F);
        if (currentActiveBtn.equals(holder.hottestBtn)) {
            holder.navigationIndicator.setTranslationX(0);
        } else {
            holder.navigationIndicator.setTranslationX(ExploreFutureApplication.screenWidth / 2);
        }

    }

    //define interface
    public interface DelegateInterface {
        /**
         * 委派ViewHolder处理事物
         */
        void handleTransaction(Context mContext, WrapperVH mHolder, Object mData);
    }

    private void initHandler() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == DataUpdateUtil.UPDATE_DATA_FINISH_MSG) {

                }
            }
        };
    }


}
