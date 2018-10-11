package itc.ink.explorefuture_android.common_unit.mind_recyclerview.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
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
import java.util.ArrayList;
import java.util.List;
import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.common_unit.video_view.VideoViewerActivity;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;

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

        String personId = mindListDataItem.getId().split("_")[0];
        holder.mindItemHeaderLayout.setOnClickListener(new AttentionItemHeaderLayoutClickListener(personId));

        RequestOptions options = new RequestOptions()
                .signature(new ObjectKeyCanNull(mindListDataItem.getHead_portrait_image_update_datetime()).getObject())
                .circleCrop();
        Glide.with(getContext()).load(mindListDataItem.getHead_portrait_image_url()).apply(options).into(holder.mindItemHeadPortrait);
        holder.mindItemName.setText(mindListDataItem.getName());
        holder.mindItemDatetime.setText(mindListDataItem.getDatetime());
        if (mindListDataItem.getContent_text().trim().equals("")) {
            holder.mindItemContentText.setText(R.string.recommend_mind_content_empty_text);
        } else {
            holder.mindItemContentText.setText(mindListDataItem.getContent_text());
        }
        holder.mindItemContentText.setOnClickListener(new AttentionItemContentTextClickListener(mindListDataItem.getId()));

        holder.mindItemContentMediaLayout.removeAllViews();
        if (mindListDataItem.getImage_url_list().size() > 0 && mindListDataItem.getImage_url_list().size() <= 9) {
            addPicToLayout(holder, mindListDataItem.getImage_url_list(), mindListDataItem.getContent_text());
        } else if (mindListDataItem.getImage_url_list().size() > 9) {
            addPicToLayout(holder, mindListDataItem.getImage_url_list().subList(0, 9), mindListDataItem.getContent_text());
        } else if (!(mindListDataItem.getVideo_url() == null || mindListDataItem.getVideo_url().trim().equals(""))) {
            addVideoToLayout(holder, mindListDataItem.getVideo_url(), mindListDataItem.getContent_text());
        }

        holder.mindItemAcceptNumText.setText(mindListDataItem.getAccept_num());
        holder.mindItemAcceptNumText.setOnClickListener(new AttentionItemAcceptNumTextClickListener(mindListDataItem.getId()));
        holder.mindItemCommentNumText.setText(mindListDataItem.getComment_num());
        holder.mindItemCommentNumText.setOnClickListener(new AttentionItemCommentNumTextClickListener(mindListDataItem.getId()));
        holder.mindItemRetransmissionNumText.setText(mindListDataItem.getRetransmission_num());
        holder.mindItemRetransmissionNumText.setOnClickListener(new AttentionItemRetransmissionNumTextClickListener(mindListDataItem.getId()));

        holder.mindItemDividerLine.setVisibility(View.VISIBLE);
        if (position == mMindListData.size()-1) {
            holder.mindItemDividerLine.setVisibility(View.GONE);
        }
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
        private TextView mindItemContentText;
        private ConstraintLayout mindItemContentMediaLayout;
        private TextView mindItemAcceptNumText;
        private TextView mindItemCommentNumText;
        private TextView mindItemRetransmissionNumText;
        private View mindItemDividerLine;

        public VH(View view) {
            super(view);
            mindItemHeaderLayout = view.findViewById(R.id.mind_ListItem_Header_Layout);
            mindItemHeadPortrait = view.findViewById(R.id.mind_ListItem_HeadPortrait);
            mindItemName = view.findViewById(R.id.mind_ListItem_Name);
            mindItemDatetime = view.findViewById(R.id.mind_ListItem_Datetime);
            mindItemContentText = view.findViewById(R.id.mind_ListItem_Content_Text);
            mindItemContentMediaLayout = view.findViewById(R.id.mind_ListItem_Content_Media_Layout);
            mindItemAcceptNumText = view.findViewById(R.id.mind_ListItem_Accept_Num);
            mindItemCommentNumText = view.findViewById(R.id.mind_ListItem_Comment_Num);
            mindItemRetransmissionNumText = view.findViewById(R.id.mind_ListItem_Retransmission_Num);
            mindItemDividerLine = view.findViewById(R.id.mind_ListItem_Divider_Line);
        }
    }

    class AttentionItemHeaderLayoutClickListener implements View.OnClickListener {
        private String PersonId = "";

        public AttentionItemHeaderLayoutClickListener(String PersonId) {
            this.PersonId = PersonId;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), PersonId + "被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class AttentionItemContentTextClickListener implements View.OnClickListener {
        private String ID = "";

        public AttentionItemContentTextClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), ID + "文本内容被点击", Toast.LENGTH_SHORT).show();
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
            //Toast.makeText(mContext, "视频内容被点击，URL->" + videoUrl, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), VideoViewerActivity.class);
            intent.putExtra(VideoViewerActivity.KEY_VIDEO_URL, videoUrl);
            intent.putExtra(VideoViewerActivity.KEY_CONTENT_TEXT, contentText);
            getContext().startActivity(intent);
        }
    }

    class AttentionItemAcceptNumTextClickListener implements View.OnClickListener {
        private String ID = "";

        public AttentionItemAcceptNumTextClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), ID + "赞被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class AttentionItemCommentNumTextClickListener implements View.OnClickListener {
        private String ID = "";

        public AttentionItemCommentNumTextClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), ID + "评论被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class AttentionItemRetransmissionNumTextClickListener implements View.OnClickListener {
        private String ID = "";

        public AttentionItemRetransmissionNumTextClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), ID + "转发被点击", Toast.LENGTH_SHORT).show();
        }
    }

}