package itc.ink.explorefuture_android.recommend.attention_fragment.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.app_level.mind_recyclerview.adapter.MindDataAdapter;
import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.adapter_recommend.RecommendDataAdapter;
import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.implement.RecommendDelegateImplement;
import itc.ink.explorefuture_android.app.app_level.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_recommend.RecommendListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class AttentionWrapAdapter extends RecyclerView.Adapter<AttentionWrapAdapter.WrapperVH> {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "MindWrapAdapter";
    private Context mContext;

    private RecommendDataAdapter mRecommendDataAdapter;
    private MindDataAdapter mMindDataAdapter;

    private DelegateInterface mDelegateInterface;

    public AttentionWrapAdapter(Context mContext, ArrayList<RecommendListDataMode> mRecommendListData, ArrayList<MindListDataMode> mMindListData) {
        this.mContext = mContext;
        this.mRecommendDataAdapter=new RecommendDataAdapter(mContext, mRecommendListData);
        this.mMindDataAdapter=new MindDataAdapter(mContext,mMindListData);
    }

    @Override
    public WrapperVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.RECOMMEND_LIST.ordinal()) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_attention_fragment_recommend_item, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.RECOMMEND_LIST);
        } else {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_attention_fragment_attention_item, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.ATTENTION_LIST);
        }
    }

    @Override
    public void onBindViewHolder(WrapperVH holder, final int position) {
        if (position == 0) {
            mDelegateInterface = new RecommendDelegateImplement();
            mDelegateInterface.handleTransaction(mContext, holder);

            if (holder.recommendRecyclerView.getAdapter() == null) {
                holder.recommendRecyclerView.setAdapter(mRecommendDataAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                holder.recommendRecyclerView.setLayoutManager(contentRvLayoutManager);
            }
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
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.RECOMMEND_LIST.ordinal();
        } else {
            return ITEM_TYPE.ATTENTION_LIST.ordinal();
        }
    }

    public class WrapperVH extends RecyclerView.ViewHolder {
        /*Header Recommend Sub RecyclerView*/
        public TextView recommendUpdateThemBtn;
        public RecyclerView recommendRecyclerView;

        /*Attention Item*/
        public RecyclerView mindRecyclerView;

        public WrapperVH(View view, ITEM_TYPE item_type) {
            super(view);
            if (item_type == ITEM_TYPE.RECOMMEND_LIST) {
                recommendUpdateThemBtn = view.findViewById(R.id.recommend_Attention_Recommend_Header_Update_Text);
                recommendRecyclerView = view.findViewById(R.id.recommend_Attention_Recommend_RecyclerView);
            } else {
                mindRecyclerView = view.findViewById(R.id.recommend_Attention_Mind_RecyclerView);
            }
        }
    }

    private enum ITEM_TYPE {
        RECOMMEND_LIST,
        ATTENTION_LIST
    }

    //define interface
    public interface DelegateInterface {
        /**
         * 委派ViewHolder处理事物
         */
        void handleTransaction(Context mContext, AttentionWrapAdapter.WrapperVH mHolder);
    }
}
