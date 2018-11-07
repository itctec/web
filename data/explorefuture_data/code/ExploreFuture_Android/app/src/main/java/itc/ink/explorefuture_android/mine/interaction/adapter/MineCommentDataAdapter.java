package itc.ink.explorefuture_android.mine.interaction.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
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
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.common_unit.mind_details.MindDetailsActivity;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.adapter.MindItemImageDataAdapter;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.common_unit.user_details.UserDetailsActivity;
import itc.ink.explorefuture_android.common_unit.user_details.mode.SimpleUserInfoDataMode;
import itc.ink.explorefuture_android.common_unit.video_view.VideoViewerActivity;
import itc.ink.explorefuture_android.mind.edit_activity.MindEditActivity;
import itc.ink.explorefuture_android.mine.interaction.mode.MineCommentListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class MineCommentDataAdapter extends RecyclerView.Adapter<MineCommentDataAdapter.VH> {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "AttentionAdapter";
    private WeakReference<Context> mWeakContextReference;

    private ArrayList<MineCommentListDataMode> mMineCommentListData;

    public MineCommentDataAdapter(Context mContext, ArrayList<MineCommentListDataMode> mMineCommentListData) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mMineCommentListData = mMineCommentListData;
    }

    private Context getContext() {
        if (mWeakContextReference.get() != null) {
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_interaction_comment_list_item, parent, false);
        return new VH(rootView);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        MineCommentListDataMode mineCommentListDataItem = mMineCommentListData.get(position);

        String userId = mineCommentListDataItem.getId().split("_")[0];
        String nickname = mineCommentListDataItem.getNickname();
        String datetime = mineCommentListDataItem.getDatetime();
        String headPortraitImageUrl = mineCommentListDataItem.getHead_portrait_image_url();
        String headPortraitImageUrlUpdateDatetime = mineCommentListDataItem.getHead_portrait_image_update_datetime();

        SimpleUserInfoDataMode simplePersonInfoData = new SimpleUserInfoDataMode(userId, nickname,
                null, null,
                headPortraitImageUrl,
                headPortraitImageUrlUpdateDatetime);
        holder.commentItemHeaderLayout.setOnClickListener(new MindItemHeaderLayoutClickListener(simplePersonInfoData));

        RequestOptions options = new RequestOptions();
        options.signature(new ObjectKeyCanNull(headPortraitImageUrlUpdateDatetime).getObject());
        options.circleCrop();
        Glide.with(getContext()).load(headPortraitImageUrl).apply(options).into(holder.commentItemHeadPortrait);
        holder.commentItemNickname.setText(nickname);
        holder.commentItemDatetime.setText(datetime);

        if (mineCommentListDataItem.getComment_text().trim().equals("")) {
            holder.commentItemCommentText.setText(R.string.recommend_mind_content_empty_text);
        } else {
            holder.commentItemCommentText.setText(mineCommentListDataItem.getComment_text());
        }

        holder.commentItemMindLayout.setTag(mineCommentListDataItem.getMind_item());
        holder.commentItemMindLayout.setOnClickListener(new MindItemContentClickListener());

        //holder.commentItemMindLayout.removeAllViews();
        if(mineCommentListDataItem.getMind_item().getImage_url_list().size()>0){
            holder.commentItemMindImage.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(mineCommentListDataItem.getMind_item().getImage_url_list().get(0)).into(holder.commentItemMindImage);
        }else{
            holder.commentItemMindImage.setVisibility(View.GONE);
        }
        holder.commentItemMindNickname.setText(mineCommentListDataItem.getMind_item().getName());
        holder.commentItemMindContentText.setText(mineCommentListDataItem.getMind_item().getContent_text());


        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.commentItemDividerLine.getLayoutParams();
        if (position == mMineCommentListData.size() - 1) {
            layoutParams.height = 0;
            holder.commentItemDividerLine.setBackgroundColor(Color.alpha(0));
        } else {
            layoutParams.height = dip2px(getContext(), 8);
            holder.commentItemDividerLine.setBackgroundColor(getContext().getColor(R.color.app_mind_content_item_divider_line_color));
        }
        holder.commentItemDividerLine.setLayoutParams(layoutParams);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public int getItemCount() {
        return mMineCommentListData.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private ConstraintLayout commentItemHeaderLayout;
        private ImageView commentItemHeadPortrait;
        private TextView commentItemNickname;
        private TextView commentItemDatetime;
        private TextView commentItemCommentText;
        private ConstraintLayout commentItemMindLayout;
        private ImageView commentItemMindImage;
        private TextView commentItemMindNickname;
        private TextView commentItemMindContentText;
        private View commentItemDividerLine;

        public VH(View view) {
            super(view);
            commentItemHeaderLayout = view.findViewById(R.id.mine_Interaction_Comment_ListItem_Header_Layout);
            commentItemHeadPortrait = view.findViewById(R.id.mine_Interaction_Comment_ListItem_HeadPortrait);
            commentItemNickname = view.findViewById(R.id.mine_Interaction_Comment_ListItem_Nickname);
            commentItemDatetime = view.findViewById(R.id.mine_Interaction_Comment_ListItem_Datetime);
            commentItemCommentText = view.findViewById(R.id.mine_Interaction_Comment_ListItem_Content_Text);
            commentItemMindLayout = view.findViewById(R.id.mine_Interaction_Comment_ListItem_Mind_Layout);
            commentItemMindImage = view.findViewById(R.id.mine_Interaction_Comment_ListItem_Mind_Image);
            commentItemMindNickname = view.findViewById(R.id.mine_Interaction_Comment_ListItem_Mind_Nickname);
            commentItemMindContentText = view.findViewById(R.id.mine_Interaction_Comment_ListItem_Mind_Content_Text);
            commentItemDividerLine = view.findViewById(R.id.mine_Interaction_Comment_ListItem_Divider_Line);
        }
    }

    class MindItemHeaderLayoutClickListener implements View.OnClickListener {
        private SimpleUserInfoDataMode simplePersonInfoData;

        public MindItemHeaderLayoutClickListener(SimpleUserInfoDataMode simplePersonInfoData) {
            this.simplePersonInfoData = simplePersonInfoData;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.KEY_PERSON_INFO_ITEM, simplePersonInfoData);
            getContext().startActivity(intent);

        }
    }

    class MindItemContentClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            MindListDataMode mindListDataItem = (MindListDataMode) view.getTag();
            Intent intent = new Intent(getContext(), MindDetailsActivity.class);
            intent.putExtra(MindDetailsActivity.KEY_MIND_ITEM, mindListDataItem);
            getContext().startActivity(intent);
        }
    }

}
