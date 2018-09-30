package itc.ink.explorefuture_android.recommend.attention_fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class AttentionItemImageDataAdapter extends RecyclerView.Adapter<AttentionItemImageDataAdapter.VH>{
    private final static String LOG_TAG = ExploreFutureApplication.LOG_TAG + "Adapter";
    private Context mContext;
    private List<String> mData;

    public AttentionItemImageDataAdapter(Context mContext, List<String> mData) {
        this.mContext=mContext;
        this.mData = mData;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_attention_fragment_attention_list_item_image_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        Glide.with(mContext).load(mData.get(position)).into(holder.listItemImageItem);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        private ImageView listItemImageItem;

        public VH(View view) {
            super(view);
            listItemImageItem = view.findViewById(R.id.recommend_Attention_ListItem_ImageItem);
        }
    }
}
