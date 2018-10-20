package itc.ink.explorefuture_android.find.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class HotSearchListDataAdapter extends RecyclerView.Adapter<HotSearchListDataAdapter.VH> {
    private final static String LOG_TAG = ExploreFutureApplication.LOG_TAG + "Adapter";
    private WeakReference<Context> mWeakContextReference;
    private List<String> mData;


    public HotSearchListDataAdapter(Context mContext, List<String> mData) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;

    }

    private Context getContext() {
        if (mWeakContextReference.get() != null) {
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_fragment_hot_search_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.hotSearchItemText.setText(mData.get(position));
        holder.hotSearchItemText.setOnClickListener(new HotSearchItemTextClickListener(mData.get(position)));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        private TextView hotSearchItemText;

        public VH(View view) {
            super(view);
            hotSearchItemText = view.findViewById(R.id.find_Hot_Search_Item);
        }
    }

    class HotSearchItemTextClickListener implements View.OnClickListener{
        private String hotText="";

        public HotSearchItemTextClickListener(String hotText) {
            this.hotText = hotText;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),hotText+"被点击",Toast.LENGTH_SHORT).show();
        }
    }

}
