package itc.ink.explorefuture_android.common_unit.search_bar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;
import itc.ink.explorefuture_android.common_unit.search_result.SearchResultActivity;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class SearchHistoryDataAdapter extends RecyclerView.Adapter<SearchHistoryDataAdapter.VH> {
    private final static String LOG_TAG = ExploreFutureApplication.LOG_TAG + "Adapter";
    private WeakReference<Context> mWeakContextReference;
    private List<String> mData;

    public SearchHistoryDataAdapter(Context mContext, List<String> mData) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_search_history_list_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.searchHistoryText.setText(mData.get(position));
        holder.itemView.setOnClickListener(new ItemClickListener(mData.get(position)));
        holder.searchHistoryIcon.setImageResource(R.drawable.vector_drawable_history_icon);
        holder.searchHistoryDividerLine.setVisibility(View.VISIBLE);
        if(position==(mData.size()-1)){
            holder.searchHistoryDividerLine.setVisibility(View.GONE);
            if(mData.size()==1){
                holder.searchHistoryIcon.setImageResource(R.drawable.vector_drawable_empty_icon);
            }else{
                holder.searchHistoryIcon.setImageResource(R.drawable.vector_drawable_garbage_icon);
                holder.itemView.setOnClickListener(new CleanHistoryClickListener());
            }

        }else{
            holder.itemView.setOnClickListener(new ItemClickListener(mData.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public static class VH extends RecyclerView.ViewHolder {
        private ImageView searchHistoryIcon;
        private TextView searchHistoryText;
        private View searchHistoryDividerLine;

        public VH(View view) {
            super(view);
            searchHistoryIcon = view.findViewById(R.id.search_History_Icon);
            searchHistoryText = view.findViewById(R.id.sort_Search_History_Text);
            searchHistoryDividerLine = view.findViewById(R.id.search_History_Divider_Line);
        }
    }

    class ItemClickListener implements View.OnClickListener{
        String contentStr="";

        public ItemClickListener(String contentStr) {
            this.contentStr = contentStr;
        }

        @Override
        public void onClick(View view) {
            Intent intent =new Intent(getContext(), SearchResultActivity.class);
            intent.putExtra(SearchResultActivity.KEY_SEARCH_TITLE,contentStr);
            getContext().startActivity(intent);
        }
    }

    class CleanHistoryClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            SQLiteDBHelper sqLiteDBHelper=new SQLiteDBHelper(getContext(),SQLiteDBHelper.DATABASE_FILE_NAME,SQLiteDBHelper.DATABASE_VERSION);
            String sqlStr="delete from tb_search_history";
            sqLiteDBHelper.getReadableDatabase().execSQL(sqlStr,new String[]{});
            sqLiteDBHelper.close();
            mData.clear();
            mData.add(getContext().getString(R.string.search_history_empty_text));
            notifyDataSetChanged();
        }
    }

}
