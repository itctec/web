package itc.ink.explorefuture_android.common_unit.topic_details.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
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
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.common_unit.mind_details.mode.CommentDataMode;
import itc.ink.explorefuture_android.common_unit.topic_details.mode.ViewPointDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class TopicViewPointDataAdapter extends RecyclerView.Adapter<TopicViewPointDataAdapter.VH> {
    private final static String LOG_TAG = "TopicViewPointDataAdapter";
    private WeakReference<Context> mWeakContextReference;
    private List<ViewPointDataMode> mData;

    private ItemViewClickListener itemViewClickListener=new ItemViewClickListener();

    public TopicViewPointDataAdapter(Context mContext, List<ViewPointDataMode> mData) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_topic_details_view_point_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        ViewPointDataMode viewPointDataItem=mData.get(position);
        holder.viewPointTitleText.setText(viewPointDataItem.getTitle());
        if(viewPointDataItem.getCover_image_url()!=null&&!viewPointDataItem.getCover_image_url().isEmpty()){
            holder.viewPointCoverImage.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .signature(new ObjectKeyCanNull(viewPointDataItem.getCover_image_update_datetime()).getObject());
            Glide.with(getContext()).load(viewPointDataItem.getCover_image_url()).apply(options).into(holder.viewPointCoverImage);
        }else{
            holder.viewPointCoverImage.setVisibility(View.GONE);
        }

        holder.viewPointContentText.setText(viewPointDataItem.getContent());
        holder.viewPointAcceptNumText.setText(viewPointDataItem.getAccept_num());
        holder.viewPointOpposeNumText.setText(viewPointDataItem.getOppose_num());
        holder.viewPointCommentNumText.setText(viewPointDataItem.getComment_num());
        holder.itemView.setTag(viewPointDataItem.getId());
        holder.itemView.setOnClickListener(itemViewClickListener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        private TextView viewPointTitleText;
        private ImageView viewPointCoverImage;
        private TextView viewPointContentText;
        private TextView viewPointAcceptNumText;
        private TextView viewPointOpposeNumText;
        private TextView viewPointCommentNumText;

        public VH(View view) {
            super(view);
            viewPointTitleText = view.findViewById(R.id.topic_View_Point_Title);
            viewPointCoverImage = view.findViewById(R.id.topic_View_Point_Cover_Image);
            viewPointContentText = view.findViewById(R.id.topic_View_Point_Content);
            viewPointAcceptNumText = view.findViewById(R.id.topic_View_Point_Accept_Count_Text);
            viewPointOpposeNumText = view.findViewById(R.id.topic_View_Point_Oppose_Count_Text);
            viewPointCommentNumText = view.findViewById(R.id.topic_View_Point_Comment_Count_Text);
        }
    }

    class ItemViewClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String id_Str=(String)view.getTag();
            Toast.makeText(getContext(),id_Str+"被点击",Toast.LENGTH_SHORT).show();
        }
    }

}
