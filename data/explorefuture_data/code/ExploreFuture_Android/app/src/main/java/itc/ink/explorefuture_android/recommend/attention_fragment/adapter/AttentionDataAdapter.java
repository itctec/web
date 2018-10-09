package itc.ink.explorefuture_android.recommend.attention_fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.app_level.video_view.VideoViewerActivity;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.recommend.attention_fragment.adapter.implement.RecommendDelegateImplement;
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

    private DelegateInterface mDelegateInterface;

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
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_level_mind_list_item, parent, false);
            return new VH(rootView, ITEM_TYPE.ATTENTION_LIST);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (position == 0) {
            mDelegateInterface = new RecommendDelegateImplement();
            mDelegateInterface.handleTransaction(mContext, holder);

            if (holder.recommendRecyclerView.getAdapter() == null) {
                RecommendDataAdapter contentRvAdapter = new RecommendDataAdapter(mContext, mRecommendListData);
                holder.recommendRecyclerView.setAdapter(contentRvAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                holder.recommendRecyclerView.setLayoutManager(contentRvLayoutManager);
            }
        } else {
            AttentionListDataMode attentionListDataItem = mAttentionListData.get(position - 1);

            String personId = attentionListDataItem.getId().split("_")[0];
            holder.attentionItemHeaderLayout.setOnClickListener(new AttentionItemHeaderLayoutClickListener(personId));

            RequestOptions options = new RequestOptions()
                    .signature(new ObjectKeyCanNull(attentionListDataItem.getHead_portrait_image_update_datetime()).getObject())
                    .circleCrop();
            Glide.with(mContext).load(attentionListDataItem.getHead_portrait_image_url()).apply(options).into(holder.attentionItemHeadPortrait);
            holder.attentionItemName.setText(attentionListDataItem.getName());
            holder.attentionItemDatetime.setText(attentionListDataItem.getDatetime());
            if (attentionListDataItem.getContent_text().trim().equals("")) {
                holder.attentionItemContentText.setText(R.string.recommend_attention_content_empty_text);
            } else {
                holder.attentionItemContentText.setText(attentionListDataItem.getContent_text());
            }
            holder.attentionItemContentText.setOnClickListener(new AttentionItemContentTextClickListener(attentionListDataItem.getId()));

            holder.attentionItemContentMediaLayout.removeAllViews();
            if (attentionListDataItem.getImage_url_list().size() > 0 && attentionListDataItem.getImage_url_list().size() <= 9) {
                addPicToLayout(holder, attentionListDataItem.getImage_url_list(), attentionListDataItem.getContent_text());
            } else if (attentionListDataItem.getImage_url_list().size() > 9) {
                addPicToLayout(holder, attentionListDataItem.getImage_url_list().subList(0, 9), attentionListDataItem.getContent_text());
            } else if (!(attentionListDataItem.getVideo_url() == null || attentionListDataItem.getVideo_url().trim().equals(""))) {
                addVideoToLayout(holder, attentionListDataItem.getVideo_url(), attentionListDataItem.getContent_text());
            }

            holder.attentionItemAcceptNumText.setText(attentionListDataItem.getAccept_num());
            holder.attentionItemAcceptNumText.setOnClickListener(new AttentionItemAcceptNumTextClickListener(attentionListDataItem.getId()));
            holder.attentionItemCommentNumText.setText(attentionListDataItem.getComment_num());
            holder.attentionItemCommentNumText.setOnClickListener(new AttentionItemCommentNumTextClickListener(attentionListDataItem.getId()));
            holder.attentionItemRetransmissionNumText.setText(attentionListDataItem.getRetransmission_num());
            holder.attentionItemRetransmissionNumText.setOnClickListener(new AttentionItemRetransmissionNumTextClickListener(attentionListDataItem.getId()));

            holder.attentionItemDividerLine.setVisibility(View.VISIBLE);
            if (position == mAttentionListData.size()) {
                holder.attentionItemDividerLine.setVisibility(View.GONE);
            }
        }

    }

    private void addPicToLayout(VH holder, List<String> imageUrlList, String contentText) {
        RecyclerView imageRecyclerView = new RecyclerView(mContext);
        imageRecyclerView.setId(R.id.recommend_Attention_ListItem_Content_Media_Image_RecyclerView);
        AttentionItemImageDataAdapter contentRvAdapter = new AttentionItemImageDataAdapter(mContext, imageUrlList, contentText);
        imageRecyclerView.setAdapter(contentRvAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new GridLayoutManager(mContext, 3);
        imageRecyclerView.setLayoutManager(contentRvLayoutManager);
        imageRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        DividerItemDecoration dividerItemDecorationOne = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecorationOne.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.mind_image_divider_horizontal));
        imageRecyclerView.addItemDecoration(dividerItemDecorationOne);
        DividerItemDecoration dividerItemDecorationTwo = new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL);
        dividerItemDecorationTwo.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.mind_image_divider_vertical));
        imageRecyclerView.addItemDecoration(dividerItemDecorationTwo);
        holder.attentionItemContentMediaLayout.addView(imageRecyclerView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.constrainWidth(imageRecyclerView.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(imageRecyclerView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(imageRecyclerView.getId(), ConstraintSet.TOP, holder.attentionItemContentText.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(imageRecyclerView.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(imageRecyclerView.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);

        constraintSet.applyTo(holder.attentionItemContentMediaLayout);
    }

    private void addVideoToLayout(VH holder, String videoUrl, String contentText) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.app_level_mind_list_item_video_gif_list_item, null, false);
        rootView.setId(R.id.recommend_Attention_ListItem_Content_Media_Video_Gif);
        ImageView videoGifView = rootView.findViewById(R.id.recommend_Attention_ListItem_Video_Gif_Item);
        videoGifView.setOnClickListener(new VideoGifViewClickListener(videoUrl, contentText));
        Glide.with(mContext).load(videoUrl.replace(".mp4", ".gif")).into(videoGifView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.constrainWidth(rootView.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(rootView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(rootView.getId(), ConstraintSet.TOP, holder.attentionItemContentText.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(rootView.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(rootView.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);

        holder.attentionItemContentMediaLayout.addView(rootView);
        constraintSet.applyTo(holder.attentionItemContentMediaLayout);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
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
        /*Header Recommend Sub RecyclerView*/
        public TextView recommendUpdateThemBtn;
        public RecyclerView recommendRecyclerView;

        /*Attention Item*/
        private ConstraintLayout attentionItemHeaderLayout;
        private ImageView attentionItemHeadPortrait;
        private TextView attentionItemName;
        private TextView attentionItemDatetime;
        private TextView attentionItemContentText;
        private ConstraintLayout attentionItemContentMediaLayout;
        private TextView attentionItemAcceptNumText;
        private TextView attentionItemCommentNumText;
        private TextView attentionItemRetransmissionNumText;
        private View attentionItemDividerLine;


        public VH(View view, ITEM_TYPE item_type) {
            super(view);
            if (item_type == ITEM_TYPE.RECOMMEND_LIST) {
                recommendUpdateThemBtn = view.findViewById(R.id.recommend_Attention_Recommend_Header_Update_Text);
                recommendRecyclerView = view.findViewById(R.id.recommend_Attention_Recommend_RecyclerView);
            } else {
                attentionItemHeaderLayout = view.findViewById(R.id.recommend_Attention_ListItem_Header_Layout);
                attentionItemHeadPortrait = view.findViewById(R.id.recommend_Attention_ListItem_HeadPortrait);
                attentionItemName = view.findViewById(R.id.recommend_Attention_ListItem_Name);
                attentionItemDatetime = view.findViewById(R.id.recommend_Attention_ListItem_Datetime);
                attentionItemContentText = view.findViewById(R.id.recommend_Attention_ListItem_Content_Text);
                attentionItemContentMediaLayout = view.findViewById(R.id.recommend_Attention_ListItem_Content_Media_Layout);
                attentionItemAcceptNumText = view.findViewById(R.id.recommend_Attention_ListItem_Accept_Num);
                attentionItemCommentNumText = view.findViewById(R.id.recommend_Attention_ListItem_Comment_Num);
                attentionItemRetransmissionNumText = view.findViewById(R.id.recommend_Attention_ListItem_Retransmission_Num);
                attentionItemDividerLine = view.findViewById(R.id.recommend_Attention_ListItem_Divider_Line);
            }
        }
    }

    private enum ITEM_TYPE {
        RECOMMEND_LIST,
        ATTENTION_LIST
    }

    class AttentionItemHeaderLayoutClickListener implements View.OnClickListener {
        private String PersonId = "";

        public AttentionItemHeaderLayoutClickListener(String PersonId) {
            this.PersonId = PersonId;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, PersonId + "被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class AttentionItemContentTextClickListener implements View.OnClickListener {
        private String ID = "";

        public AttentionItemContentTextClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, ID + "文本内容被点击", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(mContext, VideoViewerActivity.class);
            intent.putExtra(VideoViewerActivity.KEY_VIDEO_URL, videoUrl);
            intent.putExtra(VideoViewerActivity.KEY_CONTENT_TEXT, contentText);
            mContext.startActivity(intent);
        }
    }

    class AttentionItemAcceptNumTextClickListener implements View.OnClickListener {
        private String ID = "";

        public AttentionItemAcceptNumTextClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, ID + "赞被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class AttentionItemCommentNumTextClickListener implements View.OnClickListener {
        private String ID = "";

        public AttentionItemCommentNumTextClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, ID + "评论被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class AttentionItemRetransmissionNumTextClickListener implements View.OnClickListener {
        private String ID = "";

        public AttentionItemRetransmissionNumTextClickListener(String ID) {
            this.ID = ID;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, ID + "转发被点击", Toast.LENGTH_SHORT).show();
        }
    }

    //define interface
    public interface DelegateInterface {
        /**
         * 委派ViewHolder处理事物
         */
        void handleTransaction(Context mContext, AttentionDataAdapter.VH mHolder);
    }
}
