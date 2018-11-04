package itc.ink.explorefuture_android.recommend.attention_fragment.adapter.adapter_recommend;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.common_unit.user_details.UserDetailsActivity;
import itc.ink.explorefuture_android.common_unit.user_details.mode.SimpleUserInfoDataMode;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_recommend.RecommendListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class RecommendDataAdapter extends RecyclerView.Adapter<RecommendDataAdapter.VH>{
    private final static String LOG_TAG = ExploreFutureApplication.LOG_TAG + "Adapter";
    private WeakReference<Context> mWeakContextReference;
    private List<RecommendListDataMode> mData;

    public RecommendDataAdapter(Context mContext, List<RecommendListDataMode> mData) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
    }

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_attention_fragment_recommend_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        final RecommendListDataMode recommendListDataItem = mData.get(position);

        holder.recommendItemLayout.setOnClickListener(new RecommendItemLayoutClickListener(recommendListDataItem));

        RequestOptions options = new RequestOptions()
                .signature(new ObjectKeyCanNull(recommendListDataItem.getImage_update_datetime()).getObject())
                .circleCrop();
        Glide.with(getContext()).load(recommendListDataItem.getImage_url()).apply(options).into(holder.recommendItemHeadPortrait);

        holder.recommendItemName.setText(recommendListDataItem.getName());
        holder.recommendItemDomain.setText(recommendListDataItem.getSummary());
        holder.recommendItemAddAttentionBtn.setOnClickListener(new RecommendItemAddAttentionBtnClickListener(recommendListDataItem.getId()));

        holder.itemDividerLine.setVisibility(View.VISIBLE);
        if(position==mData.size()-1){
            holder.itemDividerLine.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        private ConstraintLayout recommendItemLayout;
        private ImageView recommendItemHeadPortrait;
        private TextView recommendItemName;
        private TextView recommendItemDomain;
        private Button recommendItemAddAttentionBtn;
        private View itemDividerLine;

        public VH(View view) {
            super(view);
            recommendItemLayout=view.findViewById(R.id.recommend_Attention_Recommend_ListItem_Layout);
            recommendItemHeadPortrait = view.findViewById(R.id.recommend_Attention_Recommend_ListItem_HeadPortrait);
            recommendItemName=view.findViewById(R.id.recommend_Attention_Recommend_ListItem_Name);
            recommendItemDomain=view.findViewById(R.id.recommend_Attention_Recommend_ListItem_Domain);
            recommendItemAddAttentionBtn=view.findViewById(R.id.recommend_Attention_Recommend_ListItem_Add_Attention_Btn);
            itemDividerLine=view.findViewById(R.id.recommend_Attention_Recommend_ListItem_Vertical_Line_One);
        }
    }

    class RecommendItemLayoutClickListener implements View.OnClickListener{
        private RecommendListDataMode recommendListDataItem;

        public RecommendItemLayoutClickListener(RecommendListDataMode recommendListDataItem) {
            this.recommendListDataItem = recommendListDataItem;
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getContext(), UserDetailsActivity.class);
            SimpleUserInfoDataMode simplePersonInfoData=new SimpleUserInfoDataMode(recommendListDataItem.getId(),
                    recommendListDataItem.getName(),
                    null,null,
                    recommendListDataItem.getImage_url(),
                    recommendListDataItem.getImage_update_datetime());
            intent.putExtra(UserDetailsActivity.KEY_PERSON_INFO_ITEM,simplePersonInfoData);
            getContext().startActivity(intent);
        }
    }

    class RecommendItemAddAttentionBtnClickListener implements View.OnClickListener{
        private String ID="";

        public RecommendItemAddAttentionBtnClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"关注"+ID+"被点击",Toast.LENGTH_SHORT).show();
        }
    }
}
