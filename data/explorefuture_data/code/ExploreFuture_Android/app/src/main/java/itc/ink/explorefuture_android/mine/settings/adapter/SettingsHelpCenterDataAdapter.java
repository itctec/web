package itc.ink.explorefuture_android.mine.settings.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.mine.settings.mode.HelpCenterDataMode;
import itc.ink.explorefuture_android.mine.settings.mode.ReceiveAddressDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class SettingsHelpCenterDataAdapter extends RecyclerView.Adapter<SettingsHelpCenterDataAdapter.VH> {
    private final String LOG_TAG = "SettingsHelpCenterDataAdapter";
    private WeakReference<Context> mWeakContextReference;

    private ArrayList<HelpCenterDataMode> mReceiveAddressListData;

    public SettingsHelpCenterDataAdapter(Context mContext, ArrayList<HelpCenterDataMode> mReceiveAddressListData) {
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
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_settings_help_center_list_item, parent, false);
        return new VH(rootView);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        HelpCenterDataMode helpCenterListDataItem = mReceiveAddressListData.get(position);
        holder.faqItemTitle.setText(helpCenterListDataItem.getTitle());
        holder.itemView.setTag(helpCenterListDataItem.getId());
        holder.itemView.setOnClickListener(new HelpCenterItemClickListener());
    }

    @Override
    public int getItemCount() {
        return mReceiveAddressListData.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView faqItemTitle;

        public VH(View view) {
            super(view);
            faqItemTitle = view.findViewById(R.id.mine_Help_Center_ListItem_Title);
        }
    }


    class HelpCenterItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String id=(String)view.getTag();
            Toast.makeText(getContext(),id+"被点击",Toast.LENGTH_SHORT).show();
        }
    }

}
