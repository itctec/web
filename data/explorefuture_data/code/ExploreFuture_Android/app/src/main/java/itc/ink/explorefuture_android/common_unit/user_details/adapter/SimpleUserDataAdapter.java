package itc.ink.explorefuture_android.common_unit.user_details.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import itc.ink.explorefuture_android.common_unit.user_details.UserDetailsActivity;
import itc.ink.explorefuture_android.common_unit.user_details.mode.SimpleUserInfoDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class SimpleUserDataAdapter extends RecyclerView.Adapter<SimpleUserDataAdapter.VH> {
    private final static String LOG_TAG = ExploreFutureApplication.LOG_TAG + "Adapter";
    private WeakReference<Context> mWeakContextReference;
    private List<SimpleUserInfoDataMode> mData;

    public SimpleUserDataAdapter(Context mContext, List<SimpleUserInfoDataMode> mData) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_simple_user_info_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        SimpleUserInfoDataMode simpleUserInfoDataItem=mData.get(position);
        RequestOptions options = new RequestOptions()
                .signature(new ObjectKeyCanNull(simpleUserInfoDataItem.getHead_portrait_image_update_datetime()).getObject())
                .circleCrop();
        Glide.with(getContext()).load(simpleUserInfoDataItem.getHead_portrait_image_url()).apply(options).into(holder.headPortrait);
        holder.headPortrait.setOnClickListener(new UserInfoViewClickListener(simpleUserInfoDataItem));
        holder.nickname.setText(simpleUserInfoDataItem.getNickname());
        holder.nickname.setOnClickListener(new UserInfoViewClickListener(simpleUserInfoDataItem));
        holder.personalizedSignature.setText(simpleUserInfoDataItem.getStr_personalized_signature());
        holder.fansCount.setText(String.format(getContext().getResources().getString(R.string.mine_fans_count_text),simpleUserInfoDataItem.getStr_fans_count()));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class VH extends RecyclerView.ViewHolder {
        private ImageView headPortrait;
        private TextView nickname;
        private TextView personalizedSignature;
        private TextView fansCount;

        public VH(View view) {
            super(view);
            headPortrait = view.findViewById(R.id.simple_User_Info_ListItem_HeadPortrait);
            nickname = view.findViewById(R.id.simple_User_Info_ListItem_Nickname);
            personalizedSignature = view.findViewById(R.id.simple_User_Info_ListItem_Personalized_Signature);
            fansCount = view.findViewById(R.id.simple_User_Info_ListItem_Fans_Count);
        }
    }

    class UserInfoViewClickListener implements View.OnClickListener {
        private SimpleUserInfoDataMode simpleUserInfoData;

        public UserInfoViewClickListener(SimpleUserInfoDataMode simpleUserInfoData) {
            this.simpleUserInfoData = simpleUserInfoData;
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getContext(), UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.KEY_PERSON_INFO_ITEM,simpleUserInfoData);
            getContext().startActivity(intent);
        }
    }

}
