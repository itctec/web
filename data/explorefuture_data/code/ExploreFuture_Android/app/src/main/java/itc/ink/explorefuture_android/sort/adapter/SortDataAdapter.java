package itc.ink.explorefuture_android.sort.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.SharedPreferenceUtil;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateUtil;
import itc.ink.explorefuture_android.common_unit.content_details.ContentDetailsActivity;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_recommend.RecommendListDataMode;
import itc.ink.explorefuture_android.sort.SortFragment;
import itc.ink.explorefuture_android.sort.mode.mode_sort.SortListDataMode;
import itc.ink.explorefuture_android.sort.mode.mode_sort.SubSortListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class SortDataAdapter extends RecyclerView.Adapter<SortDataAdapter.VH> {
    private final static String LOG_TAG = ExploreFutureApplication.LOG_TAG + "Adapter";
    private WeakReference<Context> mWeakContextReference;
    private List<SortListDataMode> mData;

    private OutCallBack mOutCallBack;


    public SortDataAdapter(Context mContext, List<SortListDataMode> mData, OutCallBack mOutCallBack) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mData = mData;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_fragment_sort_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        SortListDataMode sortListDataItem = mData.get(position);

        RequestOptions options = new RequestOptions();

        holder.sortLevelTitleTextView.setText(sortListDataItem.getSort_title());
        if(SortFragment.allTabNow){
            holder.sortLevelTitleMoreTextView.setVisibility(View.VISIBLE);
            holder.sortLevelTitleMoreTextView.setOnClickListener(new SortLevelTitleMoreTextViewClickListener(sortListDataItem.getSort_id()));
        }else {
            holder.sortLevelTitleMoreTextView.setVisibility(View.GONE);
        }

        SortRecommendProductClickListener sortRecommendProductClickListener =new SortRecommendProductClickListener();

        holder.sortRecommendLeftProductLayout.setTag(sortListDataItem.getProduct_left_id());
        holder.sortRecommendLeftProductLayout.setOnClickListener(sortRecommendProductClickListener);
        options.signature(new ObjectKeyCanNull(sortListDataItem.getProduct_left_image_update_datetime()).getObject());
        Glide.with(getContext()).load(sortListDataItem.getProduct_left_image_url()).apply(options).into(holder.sortRecommendLeftProductImageImageView);
        holder.sortRecommendLeftProductTitleTextView.setText(sortListDataItem.getProduct_left_title());
        holder.sortRecommendLeftProductSummaryTextView.setText(sortListDataItem.getProduct_left_summary());

        holder.sortRecommendRightProductLayout.setTag(sortListDataItem.getProduct_right_id());
        holder.sortRecommendRightProductLayout.setOnClickListener(sortRecommendProductClickListener);
        options.signature(new ObjectKeyCanNull(sortListDataItem.getProduct_right_image_update_datetime()).getObject());
        Glide.with(getContext()).load(sortListDataItem.getProduct_right_image_url()).apply(options).into(holder.sortRecommendRightProductImageImageView);
        holder.sortRecommendRightProductTitleTextView.setText(sortListDataItem.getProduct_right_title());
        holder.sortRecommendRightProductSummaryTextView.setText(sortListDataItem.getProduct_right_summary());

        SubSortDataAdapter subSortDataAdapter = new SubSortDataAdapter(getContext(), sortListDataItem.getArray_sub_sort());
        holder.sortSubRecyclerView.setAdapter(subSortDataAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new GridLayoutManager(getContext(), 2);
        holder.sortSubRecyclerView.setLayoutManager(contentRvLayoutManager);
        DividerItemDecoration dividerItemDecorationOne = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecorationOne.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.sort_divider_horizontal));
        holder.sortSubRecyclerView.addItemDecoration(dividerItemDecorationOne);
        DividerItemDecoration dividerItemDecorationTwo = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecorationTwo.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.sort_divider_vertical));
        holder.sortSubRecyclerView.addItemDecoration(dividerItemDecorationTwo);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class VH extends RecyclerView.ViewHolder {
        private TextView sortLevelTitleTextView;
        private TextView sortLevelTitleMoreTextView;

        private ConstraintLayout sortRecommendLeftProductLayout;
        private ImageView sortRecommendLeftProductImageImageView;
        private TextView sortRecommendLeftProductTitleTextView;
        private TextView sortRecommendLeftProductSummaryTextView;
        private ConstraintLayout sortRecommendRightProductLayout;
        private ImageView sortRecommendRightProductImageImageView;
        private TextView sortRecommendRightProductTitleTextView;
        private TextView sortRecommendRightProductSummaryTextView;

        private RecyclerView sortSubRecyclerView;

        public VH(View view) {
            super(view);
            sortLevelTitleTextView = view.findViewById(R.id.sort_Level_One_Title_Text);
            sortLevelTitleMoreTextView = view.findViewById(R.id.sort_Level_One_Title_More_Text);

            sortRecommendLeftProductLayout = view.findViewById(R.id.sort_Recommend_Left_Product_Layout);
            sortRecommendLeftProductImageImageView = view.findViewById(R.id.sort_Recommend_Left_Product_Image);
            sortRecommendLeftProductTitleTextView = view.findViewById(R.id.sort_Recommend_Left_Product_Title);
            sortRecommendLeftProductSummaryTextView = view.findViewById(R.id.sort_Recommend_Left_Product_Summary);
            sortRecommendRightProductLayout = view.findViewById(R.id.sort_Recommend_Right_Product_Layout);
            sortRecommendRightProductImageImageView = view.findViewById(R.id.sort_Recommend_Right_Product_Image);
            sortRecommendRightProductTitleTextView = view.findViewById(R.id.sort_Recommend_Right_Product_Title);
            sortRecommendRightProductSummaryTextView = view.findViewById(R.id.sort_Recommend_Right_Product_Summary);

            sortSubRecyclerView = view.findViewById(R.id.sort_Sub_RecyclerView);

        }
    }

    class SortLevelTitleMoreTextViewClickListener implements View.OnClickListener {
        private String sort_id = "";

        public SortLevelTitleMoreTextViewClickListener(String sort_id) {
            this.sort_id = sort_id;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), sort_id + "更多被点击", Toast.LENGTH_SHORT).show();
            mOutCallBack.onItemClick(sort_id);
        }
    }

    class SortRecommendProductClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String content_id = (String) view.getTag();
            Intent intent = new Intent(getContext(), ContentDetailsActivity.class);
            intent.putExtra(ContentDetailsActivity.KEY_CONTENT_ID, content_id);
            getContext().startActivity(intent);
        }
    }

    public interface OutCallBack {
        void onItemClick(String id);
    }

}
