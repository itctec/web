package itc.ink.explorefuture_android.mind.edit_activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.common_unit.image_view.ImageViewerActivity;
import itc.ink.explorefuture_android.mind.edit_activity.mode.MindEditImageListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class MindEditImageDataAdapter extends RecyclerView.Adapter<MindEditImageDataAdapter.VH> {
    private final static String LOG_TAG = ExploreFutureApplication.LOG_TAG + "Adapter";
    private WeakReference<Context> mWeakContextReference;
    private List<MindEditImageListDataMode> mData;
    private String mContentText;

    private OutCallBack mOutCallBack;

    public MindEditImageDataAdapter(Context mContext, List<MindEditImageListDataMode> mData, String mContentText, OutCallBack mOutCallBack) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;
        this.mContentText = mContentText;
        this.mOutCallBack = mOutCallBack;

    }

    private Context getContext() {
        if (mWeakContextReference.get() != null) {
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mind_edit_activity_image_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        MindEditImageListDataMode mindEditImageListDataItem = mData.get(position);
        if (mindEditImageListDataItem.isImage()) {
            holder.listItemImageItem.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(mindEditImageListDataItem.getImage_url()).into(holder.listItemImageItem);
            holder.listItemImageItem.setOnClickListener(new ListItemImageItemClickListener(position));
            holder.listItemImageItemDeleteBtn.setVisibility(View.VISIBLE);
            holder.listItemImageItemDeleteBtn.setOnClickListener(new ListItemImageItemDeleteBtnClickListener(position));
        } else {
            if(position<9){
                holder.listItemImageItem.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(R.drawable.add_picture_btn_bg).into(holder.listItemImageItem);
                holder.listItemImageItem.setOnClickListener(new MindEditImageListDataAddBtnItemClickListener());
            }else{
                holder.listItemImageItem.setVisibility(View.GONE);
            }
            holder.listItemImageItemDeleteBtn.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        private ImageView listItemImageItem;
        private ImageView listItemImageItemDeleteBtn;

        public VH(View view) {
            super(view);
            listItemImageItem = view.findViewById(R.id.mind_Edit_ListItem_Image_Item);
            listItemImageItemDeleteBtn = view.findViewById(R.id.mind_Edit_ListItem_Image_Item_Delete_Btn);
        }
    }

    class ListItemImageItemClickListener implements View.OnClickListener {
        private int position = 0;

        public ListItemImageItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            ArrayList<String> imageUrlList = new ArrayList<>();
            for (MindEditImageListDataMode mindEditImageListDataItem : mData) {
                if (mindEditImageListDataItem.isImage()) {
                    imageUrlList.add(mindEditImageListDataItem.getImage_url());
                }
            }
            Intent intent = new Intent(getContext(), ImageViewerActivity.class);
            intent.putStringArrayListExtra(ImageViewerActivity.KEY_IMAGE_URL_LIST, imageUrlList);
            intent.putExtra(ImageViewerActivity.KEY_CONTENT_TEXT, mContentText);
            intent.putExtra(ImageViewerActivity.KEY_CURRENT_IMAGE_POSITION, position);
            getContext().startActivity(intent);
        }
    }

    class MindEditImageListDataAddBtnItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mOutCallBack.onAddClick();
        }
    }

    class ListItemImageItemDeleteBtnClickListener implements View.OnClickListener {
        private int position = 0;

        public ListItemImageItemDeleteBtnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(0, mData.size());
        }
    }

    public interface OutCallBack {
        void onAddClick();
    }
}
