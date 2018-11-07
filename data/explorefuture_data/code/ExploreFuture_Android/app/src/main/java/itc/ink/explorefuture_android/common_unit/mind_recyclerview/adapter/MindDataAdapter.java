package itc.ink.explorefuture_android.common_unit.mind_recyclerview.adapter;

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
import itc.ink.explorefuture_android.common_unit.mind_details.MindDetailsActivity;
import itc.ink.explorefuture_android.common_unit.user_details.UserDetailsActivity;
import itc.ink.explorefuture_android.common_unit.user_details.mode.SimpleUserInfoDataMode;
import itc.ink.explorefuture_android.common_unit.video_view.VideoViewerActivity;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.mind.edit_activity.MindEditActivity;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class MindDataAdapter extends RecyclerView.Adapter<MindDataAdapter.VH> {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "AttentionAdapter";
    private WeakReference<Context> mWeakContextReference;

    private ArrayList<MindListDataMode> mMindListData;

    public MindDataAdapter(Context mContext, ArrayList<MindListDataMode> mMindListData) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mMindListData = mMindListData;
    }

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_mind_list_item, parent, false);
        return new VH(rootView);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        MindListDataMode mindListDataItem = mMindListData.get(position);

        String userId = "";
        String nickname = "";
        String datetime = "";
        String headPortraitImageUrl = "";
        String headPortraitImageUrlUpdateDatetime = "";
        if(mindListDataItem.getTransfer_id()!=null&&!mindListDataItem.getTransfer_id().isEmpty()){
            userId=mindListDataItem.getTransfer_id();
            nickname=mindListDataItem.getTransfer_nickname();
            datetime=mindListDataItem.getTransfer_datetime();
            headPortraitImageUrl=mindListDataItem.getTransfer_head_portrait_image_url();
            headPortraitImageUrlUpdateDatetime =mindListDataItem.getTransfer_head_portrait_image_update_datetime();
            holder.mindItemTransferIcon.setVisibility(View.VISIBLE);
        }else{
            userId = mindListDataItem.getId().split("_")[0];
            nickname=mindListDataItem.getName();
            datetime=mindListDataItem.getDatetime();
            headPortraitImageUrl=mindListDataItem.getHead_portrait_image_url();
            headPortraitImageUrlUpdateDatetime =mindListDataItem.getHead_portrait_image_update_datetime();
            holder.mindItemTransferIcon.setVisibility(View.GONE);
        }

        SimpleUserInfoDataMode simplePersonInfoData=new SimpleUserInfoDataMode(userId,nickname,
                null,null,
                headPortraitImageUrl,
                headPortraitImageUrlUpdateDatetime);
        holder.mindItemHeaderLayout.setOnClickListener(new MindItemHeaderLayoutClickListener(simplePersonInfoData));

        RequestOptions options = new RequestOptions()
                .signature(new ObjectKeyCanNull(headPortraitImageUrlUpdateDatetime).getObject())
                .circleCrop();
        Glide.with(getContext()).load(headPortraitImageUrl).apply(options).into(holder.mindItemHeadPortrait);
        holder.mindItemName.setText(nickname);
        holder.mindItemDatetime.setText(datetime);

        if(mindListDataItem.getCompact_state().equals(MindListDataMode.STATE_COMPACTED)){
            holder.mindItemCompactIcon.setVisibility(View.VISIBLE);
        }else {
            holder.mindItemCompactIcon.setVisibility(View.GONE);
        }

        if (mindListDataItem.getContent_text().trim().equals("")) {
            holder.mindItemContentText.setText(R.string.recommend_mind_content_empty_text);
        } else {
            holder.mindItemContentText.setText(mindListDataItem.getContent_text());
        }

        holder.mindItemContentText.setTag(mindListDataItem);
        holder.mindItemContentText.setOnClickListener(new MindItemContentTextClickListener());

        holder.mindItemContentMediaLayout.removeAllViews();
        if (mindListDataItem.getImage_url_list().size() > 0 && mindListDataItem.getImage_url_list().size() <= 9) {
            addPicToLayout(holder, mindListDataItem.getImage_url_list(), mindListDataItem.getContent_text());
        } else if (mindListDataItem.getImage_url_list().size() > 9) {
            addPicToLayout(holder, mindListDataItem.getImage_url_list().subList(0, 9), mindListDataItem.getContent_text());
        } else if (!(mindListDataItem.getVideo_url() == null || mindListDataItem.getVideo_url().trim().equals(""))) {
            addVideoToLayout(holder, mindListDataItem.getVideo_url(), mindListDataItem.getContent_text());
        }

        if(mindListDataItem.getRelease_state().equals(MindListDataMode.STATE_UN_RELEASE)){
            holder.mindItemAcceptNumText.setVisibility(View.GONE);
            holder.mindItemCommentNumText.setVisibility(View.GONE);
            holder.mindItemRetransmissionNumText.setVisibility(View.GONE);
            holder.mindItemReleaseBtn.setVisibility(View.VISIBLE);
            holder.mindItemReleaseBtn.setOnClickListener(new MindItemReleaseBtnClickListener(mindListDataItem.getId()));
        }else{
            holder.mindItemAcceptNumText.setVisibility(View.VISIBLE);
            holder.mindItemCommentNumText.setVisibility(View.VISIBLE);
            holder.mindItemRetransmissionNumText.setVisibility(View.VISIBLE);
            holder.mindItemReleaseBtn.setVisibility(View.GONE);

            holder.mindItemAcceptNumText.setText(mindListDataItem.getAccept_num());
            holder.mindItemAcceptNumText.setOnClickListener(new MindItemAcceptNumTextClickListener(mindListDataItem.getId()));
            holder.mindItemCommentNumText.setText(mindListDataItem.getComment_num());
            holder.mindItemCommentNumText.setOnClickListener(new MindItemCommentNumTextClickListener(mindListDataItem.getId()));
            holder.mindItemRetransmissionNumText.setText(mindListDataItem.getRetransmission_num());
            holder.mindItemRetransmissionNumText.setOnClickListener(new MindItemRetransmissionNumTextClickListener(mindListDataItem.getId()));
        }


        ConstraintLayout.LayoutParams layoutParams=(ConstraintLayout.LayoutParams)holder.mindItemDividerLine.getLayoutParams();
        if (position == mMindListData.size()-1) {
            layoutParams.height=0;
            holder.mindItemDividerLine.setBackgroundColor(Color.alpha(0));
        }else{
            layoutParams.height=dip2px(getContext(),8);
            holder.mindItemDividerLine.setBackgroundColor(getContext().getColor(R.color.app_mind_content_item_divider_line_color));
        }
        holder.mindItemDividerLine.setLayoutParams(layoutParams);
    }

    private int dip2px(Context context,float dpValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void addPicToLayout(VH holder, List<String> imageUrlList, String contentText) {
        RecyclerView imageRecyclerView = new RecyclerView(getContext());
        imageRecyclerView.setId(R.id.mind_ListItem_Content_Media_Image_RecyclerView);
        MindItemImageDataAdapter contentRvAdapter = new MindItemImageDataAdapter(getContext(), imageUrlList, contentText);
        imageRecyclerView.setAdapter(contentRvAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new GridLayoutManager(getContext(), 3);
        imageRecyclerView.setLayoutManager(contentRvLayoutManager);
        imageRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        imageRecyclerView.setFocusableInTouchMode(false);

        DividerItemDecoration dividerItemDecorationOne = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecorationOne.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.mind_image_divider_horizontal));
        imageRecyclerView.addItemDecoration(dividerItemDecorationOne);
        DividerItemDecoration dividerItemDecorationTwo = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecorationTwo.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.mind_image_divider_vertical));
        imageRecyclerView.addItemDecoration(dividerItemDecorationTwo);
        holder.mindItemContentMediaLayout.addView(imageRecyclerView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.constrainWidth(imageRecyclerView.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(imageRecyclerView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(imageRecyclerView.getId(), ConstraintSet.TOP, holder.mindItemContentText.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(imageRecyclerView.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(imageRecyclerView.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);

        constraintSet.applyTo(holder.mindItemContentMediaLayout);
    }

    private void addVideoToLayout(VH holder, String videoUrl, String contentText) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.common_unit_mind_list_item_video_gif_list_item, null, false);
        rootView.setId(R.id.mind_ListItem_Content_Media_Video_Gif);
        ImageView videoGifView = rootView.findViewById(R.id.mind_ListItem_Video_Gif_Item);
        videoGifView.setOnClickListener(new VideoGifViewClickListener(videoUrl, contentText));
        Glide.with(getContext()).load(videoUrl.replace(".mp4", ".gif")).into(videoGifView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.constrainWidth(rootView.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(rootView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(rootView.getId(), ConstraintSet.TOP, holder.mindItemContentText.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(rootView.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(rootView.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);

        holder.mindItemContentMediaLayout.addView(rootView);
        constraintSet.applyTo(holder.mindItemContentMediaLayout);
    }

    @Override
    public int getItemCount() {
        return mMindListData.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private ConstraintLayout mindItemHeaderLayout;
        private ImageView mindItemHeadPortrait;
        private TextView mindItemName;
        private TextView mindItemDatetime;
        private ImageView mindItemCompactIcon;
        private ImageView mindItemTransferIcon;
        private TextView mindItemContentText;
        private ConstraintLayout mindItemContentMediaLayout;
        private TextView mindItemAcceptNumText;
        private TextView mindItemCommentNumText;
        private TextView mindItemRetransmissionNumText;
        private Button mindItemReleaseBtn;
        private View mindItemDividerLine;

        public VH(View view) {
            super(view);
            mindItemHeaderLayout = view.findViewById(R.id.mind_ListItem_Header_Layout);
            mindItemHeadPortrait = view.findViewById(R.id.mind_ListItem_HeadPortrait);
            mindItemName = view.findViewById(R.id.mind_ListItem_Name);
            mindItemDatetime = view.findViewById(R.id.mind_ListItem_Datetime);
            mindItemCompactIcon = view.findViewById(R.id.mind_ListItem_ComPact_Icon);
            mindItemTransferIcon = view.findViewById(R.id.mind_ListItem_Transfer_Icon);
            mindItemContentText = view.findViewById(R.id.mind_ListItem_Content_Text);
            mindItemContentMediaLayout = view.findViewById(R.id.mind_ListItem_Content_Media_Layout);
            mindItemAcceptNumText = view.findViewById(R.id.mind_ListItem_Accept_Num);
            mindItemCommentNumText = view.findViewById(R.id.mind_ListItem_Comment_Num);
            mindItemRetransmissionNumText = view.findViewById(R.id.mind_ListItem_Retransmission_Num);
            mindItemReleaseBtn = view.findViewById(R.id.mind_ListItem_Release_Btn);
            mindItemDividerLine = view.findViewById(R.id.mind_ListItem_Divider_Line);
        }
    }

    class MindItemHeaderLayoutClickListener implements View.OnClickListener {
        private SimpleUserInfoDataMode simplePersonInfoData;

        public MindItemHeaderLayoutClickListener(SimpleUserInfoDataMode simplePersonInfoData) {
            this.simplePersonInfoData = simplePersonInfoData;
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getContext(), UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.KEY_PERSON_INFO_ITEM,simplePersonInfoData);
            getContext().startActivity(intent);

        }
    }

    class MindItemContentTextClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            MindListDataMode mindListDataItem=(MindListDataMode)view.getTag();
            if(mindListDataItem.getRelease_state().equals(MindListDataMode.STATE_UN_RELEASE)){
                Intent intent = new Intent(getContext(), MindEditActivity.class);
                intent.putExtra(MindDetailsActivity.KEY_MIND_ITEM,mindListDataItem);
                getContext().startActivity(intent);
            }else{
                Intent intent=new Intent(getContext(), MindDetailsActivity.class);
                intent.putExtra(MindDetailsActivity.KEY_MIND_ITEM,mindListDataItem);
                getContext().startActivity(intent);
            }
        }
    }

    class VideoGifViewClickListener implements View.OnClickListener {
        private String videoUrl = "";
        private String contentText = "";

        public VideoGifViewClickListener(String videoUrl, String contentText) {
            this.videoUrl = videoUrl;
            this.contentText = contentText;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), VideoViewerActivity.class);
            intent.putExtra(VideoViewerActivity.KEY_VIDEO_URL, videoUrl);
            intent.putExtra(VideoViewerActivity.KEY_CONTENT_TEXT, contentText);
            getContext().startActivity(intent);
        }
    }

    class MindItemAcceptNumTextClickListener implements View.OnClickListener {
        private String ID = "";

        public MindItemAcceptNumTextClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), ID + "赞被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class MindItemCommentNumTextClickListener implements View.OnClickListener {
        private String ID = "";

        public MindItemCommentNumTextClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), ID + "评论被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class MindItemRetransmissionNumTextClickListener implements View.OnClickListener {
        private String ID = "";

        public MindItemRetransmissionNumTextClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), ID + "转发被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class MindItemReleaseBtnClickListener implements View.OnClickListener{
        private String ID = "";

        public MindItemReleaseBtnClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "发布"+ID, Toast.LENGTH_SHORT).show();
        }
    }

}
