package itc.ink.explorefuture_android.recommend.attention_fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.image_view.ImageViewerActivity;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class AttentionItemImageDataAdapter extends RecyclerView.Adapter<AttentionItemImageDataAdapter.VH> {
    private final static String LOG_TAG = ExploreFutureApplication.LOG_TAG + "Adapter";
    private Context mContext;
    private List<String> mData;
    private String mContentText;

    public AttentionItemImageDataAdapter(Context mContext, List<String> mData, String mContentText) {
        this.mContext = mContext;
        this.mData = mData;
        this.mContentText = mContentText;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_attention_fragment_attention_list_item_image_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        Glide.with(mContext).load(mData.get(position)).into(holder.listItemImageItem);
        holder.listItemImageItem.setOnClickListener(new ListItemImageItemClickListener(position));
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

    class ListItemImageItemClickListener implements View.OnClickListener {
        private int position = 0;

        public ListItemImageItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(mContext, "第"+position + "张图片被点击", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, ImageViewerActivity.class);
            intent.putStringArrayListExtra(ImageViewerActivity.KEY_IMAGE_URL_LIST, (ArrayList<String>) mData);
            intent.putExtra(ImageViewerActivity.KEY_CONTENT_TEXT,mContentText);
            intent.putExtra(ImageViewerActivity.KEY_CURRENT_IMAGE_POSITION, position);
            mContext.startActivity(intent);
        }
    }
}
