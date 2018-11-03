package itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.adapter_interest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.util.List;
import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.utils.UnitConversionUtil;
import itc.ink.explorefuture_android.common_unit.content_details.ContentDetailsActivity;
import itc.ink.explorefuture_android.common_unit.content_list.mode.ContentListDataMode;
import itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.HandPickWrapperAdapter;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_interest.InterestDataModel;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class InterestDataAdapter extends RecyclerView.Adapter<InterestDataAdapter.VH> {
    private WeakReference<Context> mWeakContextReference;
    private List<ContentListDataMode> mData;
    private ItemClickListener itemClickListener=new ItemClickListener();

    public InterestDataAdapter(Context mContext, List<ContentListDataMode> mData) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_content_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        ContentListDataMode contentDataItem=mData.get(position);
        RequestOptions options = new RequestOptions()
                .signature(new ObjectKeyCanNull(contentDataItem.getImage_update_datetime()).getObject());
        Glide.with(getContext()).load(contentDataItem.getImage_url()).apply(options).into(holder.listItemImage);
        holder.listItemTitle.setText(contentDataItem.getTitle());
        holder.listItemSummary.setText(contentDataItem.getSummary());
        addContentTag(holder,contentDataItem.getTag().split(" "));
        holder.listItemSupportNum.setText(contentDataItem.getSupport_num());
        if(contentDataItem.getPrice()!=null&&!contentDataItem.getPrice().isEmpty()){
            holder.listItemPrice.setVisibility(View.VISIBLE);
            holder.listItemPrice.setText(contentDataItem.getPrice());
        }else{
            holder.listItemPrice.setVisibility(View.GONE);
            holder.listItemPrice.setText("00.00");
        }

        holder.itemView.setTag(mData.get(position).getId());
        holder.itemView.setOnClickListener(itemClickListener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void addContentTag(VH holder, String[] tagArray){
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, UnitConversionUtil.dip2px(getContext(),12));
        layoutParams.rightMargin=UnitConversionUtil.dip2px(getContext(),10);
        layoutParams.gravity= Gravity.CENTER;
        holder.listItemTagLayout.removeAllViews();

        for(String tagStr:tagArray){
            TextView tagText=new TextView(getContext());
            tagText.setText(tagStr);
            tagText.setTextSize(7);
            tagText.setTextColor(Color.BLACK);
            tagText.setPadding(UnitConversionUtil.dip2px(getContext(),7),UnitConversionUtil.dip2px(getContext(),1),
                    UnitConversionUtil.dip2px(getContext(),7),UnitConversionUtil.dip2px(getContext(),1));
            tagText.setBackground(getContext().getResources().getDrawable(R.drawable.tag_text_bg,null));
            tagText.setLayoutParams(layoutParams);
            holder.listItemTagLayout.addView(tagText);
        }
    }

    public static class VH extends HandPickWrapperAdapter.WrapperVH {
        private ImageView listItemImage;
        private TextView listItemTitle;
        private TextView listItemSummary;
        private LinearLayout listItemTagLayout;
        private TextView listItemSupportNum;
        private TextView listItemPrice;

        public VH(View view) {
            super(view);
            listItemImage = view.findViewById(R.id.content_ListItem_Image);
            listItemTitle = view.findViewById(R.id.content_ListItem_Title);
            listItemSummary = view.findViewById(R.id.content_ListItem_Summary);
            listItemTagLayout = view.findViewById(R.id.content_ListItem_Tag_Layout);
            listItemSupportNum = view.findViewById(R.id.content_ListItem_SupportNum);
            listItemPrice = view.findViewById(R.id.content_ListItem_Price);
        }
    }

    class ItemClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String content_id = (String) view.getTag();
            Intent intent = new Intent(getContext(), ContentDetailsActivity.class);
            intent.putExtra(ContentDetailsActivity.KEY_CONTENT_ID, content_id);
            getContext().startActivity(intent);
        }
    }
}
