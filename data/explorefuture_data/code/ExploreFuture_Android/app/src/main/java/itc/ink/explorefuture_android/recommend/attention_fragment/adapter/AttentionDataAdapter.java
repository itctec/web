package itc.ink.explorefuture_android.recommend.attention_fragment.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_attention.AttentionListDataMode;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_recommend.RecommendListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class AttentionDataAdapter extends RecyclerView.Adapter<AttentionDataAdapter.VH> {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "AttentionAdapter";
    private Context mContext;

    private ArrayList<RecommendListDataMode> mRecommendListData;
    private ArrayList<AttentionListDataMode> mAttentionListData;

    public AttentionDataAdapter(Context mContext, ArrayList<RecommendListDataMode> mRecommendListData, ArrayList<AttentionListDataMode> mAttentionListData) {
        this.mContext = mContext;
        this.mRecommendListData = mRecommendListData;
        this.mAttentionListData = mAttentionListData;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.RECOMMEND_LIST.ordinal()) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_attention_fragment_recommend_item, parent, false);
            return new VH(rootView, ITEM_TYPE.RECOMMEND_LIST);
        } else {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_attention_fragment_attention_list_item, parent, false);
            return new VH(rootView, ITEM_TYPE.ATTENTION_LIST);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {

        if (position == 0) {

        } else {
            //AttentionListDataMode attentionListDataItem = mAttentionListData.get(position-1);
        }

    }

    @Override
    public int getItemCount() {
        return mAttentionListData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.RECOMMEND_LIST.ordinal();
        } else {
            return ITEM_TYPE.ATTENTION_LIST.ordinal();
        }
    }

    public class VH extends RecyclerView.ViewHolder {
        public RecyclerView recommendRecyclerView;

        public VH(View view, ITEM_TYPE item_type) {
            super(view);
            if (item_type == ITEM_TYPE.RECOMMEND_LIST) {
                recommendRecyclerView = view.findViewById(R.id.recommend_Attention_Recommend_RecyclerView);
                RecommendDataAdapter contentRvAdapter = new RecommendDataAdapter(mContext, mRecommendListData);
                recommendRecyclerView.setAdapter(contentRvAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                recommendRecyclerView.setLayoutManager(contentRvLayoutManager);
            }
        }
    }

    private enum ITEM_TYPE {
        RECOMMEND_LIST,
        ATTENTION_LIST
    }
}
