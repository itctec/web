package itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.adapter_action;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.util.List;
import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.HandPickWrapperAdapter;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_action.ActionListDataModel;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class ActionDataAdapter extends RecyclerView.Adapter<ActionDataAdapter.VH>{
    private WeakReference<Context> mWeakContextReference;
    private List<ActionListDataModel> mData;

    public ActionDataAdapter(Context mContext, List<ActionListDataModel> mData) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_handpick_fragment_action_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        final ActionListDataModel actionDataItem = mData.get(position);
        holder.actionTitleTextView.setText(actionDataItem.getTitle());
        holder.actionPublicityTextTextView.setText(actionDataItem.getPublicitytext());
        holder.actionDatetimeTextView.setText(actionDataItem.getDatetime());
        holder.actionSiteTextView.setText(actionDataItem.getSite());
        holder.actionSummaryTextView.setText(actionDataItem.getSummary());
        RequestOptions options = new RequestOptions()
                .signature(new ObjectKeyCanNull(actionDataItem.getImage_update_datetime()).getObject());
        Glide.with(getContext()).load(actionDataItem.getImageurl()).apply(options).into(holder.actionImageImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),actionDataItem.getTitle()+"被点击",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class VH extends HandPickWrapperAdapter.WrapperVH{
        public TextView actionTitleTextView;
        public TextView actionPublicityTextTextView;
        public TextView actionDatetimeTextView;
        public TextView actionSiteTextView;
        public TextView actionSummaryTextView;
        public ImageView actionImageImageView;
        public VH(View view) {
            super(view);
            actionTitleTextView = view.findViewById(R.id.recommend_Handpick_Action_ListItem_Title);
            actionPublicityTextTextView = view.findViewById(R.id.recommend_Handpick_Action_ListItem_PublicityText);
            actionDatetimeTextView = view.findViewById(R.id.recommend_Handpick_Action_ListItem_Datetime);
            actionSiteTextView = view.findViewById(R.id.recommend_Handpick_Action_ListItem_Site);
            actionSummaryTextView = view.findViewById(R.id.recommend_Handpick_Action_ListItem_Summary);
            actionImageImageView = view.findViewById(R.id.recommend_Handpick_Action_ListItem_Image);
        }
    }
}
