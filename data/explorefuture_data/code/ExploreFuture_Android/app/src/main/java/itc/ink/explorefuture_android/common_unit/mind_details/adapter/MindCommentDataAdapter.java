package itc.ink.explorefuture_android.common_unit.mind_details.adapter;

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

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.common_unit.mind_details.mode.CommentDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class MindCommentDataAdapter extends RecyclerView.Adapter<MindCommentDataAdapter.VH> {
    private final static String LOG_TAG = ExploreFutureApplication.LOG_TAG + "Adapter";
    private WeakReference<Context> mWeakContextReference;
    private List<CommentDataMode> mData;

    public MindCommentDataAdapter(Context mContext, List<CommentDataMode> mData) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_mind_details_comment_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        CommentDataMode commentDataItem=mData.get(position);
        RequestOptions options = new RequestOptions()
                .signature(new ObjectKeyCanNull(commentDataItem.getHead_portrait_image_update_datetime()).getObject())
                .circleCrop();
        Glide.with(getContext()).load(commentDataItem.getHead_portrait_image_url()).apply(options).into(holder.headPortrait);
        holder.nickname.setText(commentDataItem.getNickname());
        holder.commentContent.setText(commentDataItem.getComment());
        holder.commentDatetime.setText(commentDataItem.getComment_datetime());
        if(position==mData.size()-1){
            holder.dividerLine.setVisibility(View.GONE);
        }else{
            holder.dividerLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        private ImageView headPortrait;
        private TextView nickname;
        private TextView commentContent;
        private TextView commentDatetime;
        private View dividerLine;

        public VH(View view) {
            super(view);
            headPortrait = view.findViewById(R.id.comment_ListItem_HeadPortrait);
            nickname = view.findViewById(R.id.comment_ListItem_Nickname);
            commentContent = view.findViewById(R.id.comment_ListItem_Content);
            commentDatetime = view.findViewById(R.id.comment_ListItem_Datetime);
            dividerLine = view.findViewById(R.id.comment_ListItem_Content_Divider_Line);
        }
    }

}
