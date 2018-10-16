package itc.ink.explorefuture_android.recommend.mind_fragment.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.youth.banner.Banner;

import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.adapter.MindDataAdapter;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.app.utils.SharedPreferenceUtil;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.recommend.mind_fragment.adapter.implement.TopicDelegateImplement;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic.TopicListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class MindWrapAdapter extends RecyclerView.Adapter<MindWrapAdapter.WrapperVH> {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "MindWrapAdapter";
    public static final String RECOMMEND_MIND_TAB_KEY = "recommend_mind_current_tab";
    public static final String RECOMMEND_MIND_VALUE_HOTTEST = "hottest";
    public static final String RECOMMEND_MIND_VALUE_NEWEST = "newest";
    private WeakReference<Context> mWeakContextReference;

    private ArrayList<TopicListDataMode> mTopicData;
    private static ArrayList<MindListDataMode> mMindListData;
    private static MindDataAdapter mMindDataAdapter;

    private DelegateInterface mDelegateInterface;

    public MindWrapAdapter(Context mContext, ArrayList<TopicListDataMode> mTopicData, ArrayList<MindListDataMode> mMindListData) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mTopicData = mTopicData;
        this.mMindListData = mMindListData;
        this.mMindDataAdapter = new MindDataAdapter(mContext, mMindListData);
    }

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
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
            mDelegateInterface.handleTransaction(getContext(), holder, mTopicData);
        } else if (position == 1) {
            holder.hottestBtn.setOnClickListener(new HottestBtnClickListener(holder));
            holder.newestBtn.setOnClickListener(new NewestBtnClickListener(holder));
            if (SharedPreferenceUtil.getString(RECOMMEND_MIND_TAB_KEY).equals(RECOMMEND_MIND_VALUE_NEWEST)) {
                updateNavigationTopBtnState(holder, holder.newestBtn);
            } else {
                updateNavigationTopBtnState(holder, holder.hottestBtn);
            }
        } else {
            holder.mindRecyclerView.setFocusableInTouchMode(false);
            if (holder.mindRecyclerView.getAdapter() == null) {
                holder.mindRecyclerView.setAdapter(mMindDataAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext());
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

            SharedPreferenceUtil.putString(RECOMMEND_MIND_TAB_KEY, RECOMMEND_MIND_VALUE_HOTTEST);

            UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(getContext());
            updateAsyncTask.execute();
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

            SharedPreferenceUtil.putString(RECOMMEND_MIND_TAB_KEY, RECOMMEND_MIND_VALUE_NEWEST);

            UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(getContext());
            updateAsyncTask.execute();
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

    static class UpdateAsyncTask extends AsyncTask<Void, Void, String> {
        private WeakReference<Context> mWeakContextReference;

        UpdateAsyncTask(Context mContext) {
            this.mWeakContextReference = new WeakReference<>(mContext);
        }

        private Context getContext() {
            if(mWeakContextReference.get() != null){
                return mWeakContextReference.get();
            }
            return null;
        }

        @Override
        protected String doInBackground(Void... params) {
            DataUpdateMode recommendMindDataUpdateMode = null;
            if (SharedPreferenceUtil.getString(RECOMMEND_MIND_TAB_KEY).equals(RECOMMEND_MIND_VALUE_NEWEST)) {
                recommendMindDataUpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                        DataUpdateMode.RECOMMEND_MIND_NEWEST_DATA_FILE_URL,
                        DataUpdateMode.RECOMMEND_MIND_NEWEST_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                        DataUpdateMode.RECOMMEND_MIND_NEWEST_LOCAL_DATA_FILE_NAME);
            } else {
                recommendMindDataUpdateMode = new DataUpdateMode(DataUpdateMode.APP_UPDATE_DATETIME_FILE_URL,
                        DataUpdateMode.RECOMMEND_MIND_HOTTEST_DATA_FILE_URL,
                        DataUpdateMode.RECOMMEND_MIND_HOTTEST_DATA_NEWEST_UPDATE_DATE_TIME_KEY,
                        DataUpdateMode.RECOMMEND_MIND_HOTTEST_LOCAL_DATA_FILE_NAME);
            }

            String resultStr = DataUpdateUtil.updateData(recommendMindDataUpdateMode);
            return resultStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                String showDataStr = "";
                switch (s) {
                    case DataUpdateUtil.UPDATE_RESULT_NEWEST_ALREADY:
                        Toast.makeText(getContext(), "暂无更新", Toast.LENGTH_SHORT).show();
                        if (SharedPreferenceUtil.getString(RECOMMEND_MIND_TAB_KEY).equals(RECOMMEND_MIND_VALUE_NEWEST)) {
                            showDataStr = DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR;
                        } else {
                            showDataStr = DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR;
                        }
                        break;
                    case DataUpdateUtil.UPDATE_RESULT_FAILED:
                        if (SharedPreferenceUtil.getString(RECOMMEND_MIND_TAB_KEY).equals(RECOMMEND_MIND_VALUE_NEWEST)) {
                            showDataStr = DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR;
                        } else {
                            showDataStr = DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR;
                        }
                        Toast.makeText(getContext(), "更新数据失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        showDataStr = s;
                        Toast.makeText(getContext(), "数据已更新", Toast.LENGTH_SHORT).show();
                }

                ArrayList<MindListDataMode> mindDataArray;
                JsonReader jsonReader = new JsonReader(new StringReader(showDataStr));
                jsonReader.setLenient(true);
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(jsonReader);
                JsonObject rootObj = jsonElement.getAsJsonObject();
                JsonArray mindDataJsonArray = rootObj.getAsJsonArray("array_mind");
                Gson gson = new Gson();
                mindDataArray = gson.fromJson(mindDataJsonArray, new TypeToken<List<MindListDataMode>>() {
                }.getType());

                mMindListData.clear();
                for (MindListDataMode mindListDataMode : mindDataArray) {
                    mMindListData.add(mindListDataMode);
                }
                mMindDataAdapter.notifyDataSetChanged();
            }
        }
    }

}
