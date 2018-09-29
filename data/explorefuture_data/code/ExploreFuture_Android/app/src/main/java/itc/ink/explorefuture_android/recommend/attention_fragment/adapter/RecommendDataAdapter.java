package itc.ink.explorefuture_android.recommend.attention_fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_recommend.RecommendListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class RecommendDataAdapter extends RecyclerView.Adapter<RecommendDataAdapter.VH>{
    private final static String LOG_TAG = ExploreFutureApplication.LOG_TAG + "Adapter";
    private Context mContext;
    private List<RecommendListDataMode> mData;

    public RecommendDataAdapter(Context mContext, List<RecommendListDataMode> mData) {
        this.mContext=mContext;
        this.mData = mData;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_attention_fragment_recommend_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        final RecommendListDataMode recommendListDataItem = mData.get(position);
        holder.recommendItemName.setText(recommendListDataItem.getName());
        holder.recommendItemDomain.setText(recommendListDataItem.getSummary());

        RequestOptions options = new RequestOptions()
                .signature(new ObjectKeyCanNull(recommendListDataItem.getImage_update_datetime()).getObject())
                .circleCrop();

        Glide.with(mContext).load(recommendListDataItem.getImage_url()).apply(options).into(holder.recommendItemHeadPortrait);

        if(position==mData.size()-1){
            holder.itemDividerLine.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        private ImageView recommendItemHeadPortrait;
        private TextView recommendItemName;
        private TextView recommendItemDomain;
        private View itemDividerLine;

        public VH(View view) {
            super(view);
            recommendItemHeadPortrait = view.findViewById(R.id.recommend_Attention_Recommend_ListItem_HeadPortrait);
            recommendItemName=view.findViewById(R.id.recommend_Attention_Recommend_ListItem_Name);
            recommendItemDomain=view.findViewById(R.id.recommend_Attention_Recommend_ListItem_Domain);
            itemDividerLine=view.findViewById(R.id.recommend_Attention_Recommend_ListItem_Vertical_Line_One);
        }
    }
}
