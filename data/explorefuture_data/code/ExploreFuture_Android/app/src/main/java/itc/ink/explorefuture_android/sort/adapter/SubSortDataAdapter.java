package itc.ink.explorefuture_android.sort.adapter;

import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.common_unit.image_view.ImageViewerActivity;
import itc.ink.explorefuture_android.sort.mode.mode_sort.SubSortListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class SubSortDataAdapter extends RecyclerView.Adapter<SubSortDataAdapter.VH> {
    private final static String LOG_TAG = ExploreFutureApplication.LOG_TAG + "Adapter";
    private WeakReference<Context> mWeakContextReference;
    private List<SubSortListDataMode> mData;

    public SubSortDataAdapter(Context mContext, List<SubSortListDataMode> mData) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_fragment_sub_sort_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        SubSortListDataMode subSortListDataMode=mData.get(position);

        holder.subSortLayout.setOnClickListener(new SubSortLayoutClickListener(subSortListDataMode.getSort_id()));
        holder.subSortTitleTextView.setText(subSortListDataMode.getSort_title());
        holder.subSortSummaryTextView.setText(subSortListDataMode.getSort_summary());

        RequestOptions options = new RequestOptions();
        options.signature(new ObjectKeyCanNull(subSortListDataMode.getLeft_image_update_datetime()).getObject());
        Glide.with(getContext()).load(subSortListDataMode.getLeft_image_url()).apply(options).into(holder.subSortLeftImageView);

        options.signature(new ObjectKeyCanNull(subSortListDataMode.getRight_image_update_datetime()).getObject());
        Glide.with(getContext()).load(subSortListDataMode.getRight_image_url()).apply(options).into(holder.subSortRightImageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        private ConstraintLayout subSortLayout;
        private TextView subSortTitleTextView;
        private TextView subSortSummaryTextView;
        private ImageView subSortLeftImageView;
        private ImageView subSortRightImageView;


        public VH(View view) {
            super(view);
            subSortLayout = view.findViewById(R.id.sort_Sub_Sort_Layout);
            subSortTitleTextView = view.findViewById(R.id.sort_Sub_Sort_Title);
            subSortSummaryTextView = view.findViewById(R.id.sort_Sub_Sort_Summary);
            subSortLeftImageView = view.findViewById(R.id.sort_Sub_Sort_Left_Image);
            subSortRightImageView = view.findViewById(R.id.sort_Sub_Sort_Right_Image);
        }
    }

    class SubSortLayoutClickListener implements View.OnClickListener{
        private String sort_id;

        public SubSortLayoutClickListener(String sort_id) {
            this.sort_id = sort_id;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),sort_id+"被点击",Toast.LENGTH_SHORT).show();
        }
    }

}