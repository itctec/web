package itc.ink.explorefuture_android.mine.settings.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.common_unit.mind_details.MindDetailsActivity;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.common_unit.user_details.UserDetailsActivity;
import itc.ink.explorefuture_android.common_unit.user_details.mode.SimpleUserInfoDataMode;
import itc.ink.explorefuture_android.mine.interaction.mode.MineCommentListDataMode;
import itc.ink.explorefuture_android.mine.settings.SettingsReceiveAddressActivity;
import itc.ink.explorefuture_android.mine.settings.mode.ReceiveAddressDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class SettingsReceiveAddressDataAdapter extends RecyclerView.Adapter<SettingsReceiveAddressDataAdapter.VH> {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "AttentionAdapter";
    private WeakReference<Context> mWeakContextReference;

    private ArrayList<ReceiveAddressDataMode> mReceiveAddressListData;

    public SettingsReceiveAddressDataAdapter(Context mContext, ArrayList<ReceiveAddressDataMode> mReceiveAddressListData) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mReceiveAddressListData = mReceiveAddressListData;
    }

    private Context getContext() {
        if (mWeakContextReference.get() != null) {
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_settings_receive_address_list_item, parent, false);
        return new VH(rootView);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        ReceiveAddressDataMode receiveAddressListDataItem = mReceiveAddressListData.get(position);
        holder.addressItemHeadPortrait.setText(receiveAddressListDataItem.getName().substring(0,1));
        holder.addressItemName.setText(receiveAddressListDataItem.getName());
        holder.addressItemPhoneNum.setText(receiveAddressListDataItem.getPhone_num());
        holder.addressItemAddress.setText(receiveAddressListDataItem.getReceive_address());
        if(receiveAddressListDataItem.getDef_address().equals("true")){
            holder.addressItemDefIcon.setVisibility(View.VISIBLE);
        }else{
            holder.addressItemDefIcon.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new AddressItemClickListener());
    }

    @Override
    public int getItemCount() {
        return mReceiveAddressListData.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView addressItemHeadPortrait;
        private TextView addressItemName;
        private TextView addressItemPhoneNum;
        private TextView addressItemAddress;
        private ImageView addressItemDefIcon;

        public VH(View view) {
            super(view);
            addressItemHeadPortrait = view.findViewById(R.id.mine_Settings_Receive_Address_ListItem_HeadPortrait);
            addressItemName = view.findViewById(R.id.mine_Settings_Receive_Address_ListItem_Name);
            addressItemPhoneNum = view.findViewById(R.id.mine_Settings_Receive_Address_ListItem_Phone_Number);
            addressItemAddress = view.findViewById(R.id.mine_Settings_Receive_Address_ListItem_Address);
            addressItemDefIcon = view.findViewById(R.id.mine_Settings_Receive_Address_ListItem_Def_Icon);
        }
    }


    class AddressItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(),"编辑地址",Toast.LENGTH_SHORT).show();
        }
    }

}
