package itc.ink.explorefuture_android.common_unit.topic_details.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.common_unit.mind_details.adapter.CommentDataAdapter;
import itc.ink.explorefuture_android.common_unit.mind_details.mode.CommentDataMode;
import itc.ink.explorefuture_android.common_unit.topic_details.mode.ViewPointDataMode;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic.TopicListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class ViewPointDetailsWrapAdapter extends RecyclerView.Adapter<ViewPointDetailsWrapAdapter.WrapperVH> {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DetailsAdapter";
    private WeakReference<Context> mWeakContextReference;

    private TopicListDataMode topicListDataItem;
    private ViewPointDataMode viewPointListDataItem;
    public ArrayList<CommentDataMode> commentDataArray=new ArrayList<>();
    public CommentDataAdapter mMindCommentDataAdapter;


    public ViewPointDetailsWrapAdapter(Context mContext, TopicListDataMode topicListDataItem,ViewPointDataMode viewPointListDataItem) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.topicListDataItem=topicListDataItem;
        this.viewPointListDataItem=viewPointListDataItem;

        mMindCommentDataAdapter=new CommentDataAdapter(getContext(),commentDataArray);

        //Get Comment Data
        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(getContext());
        updateAsyncTask.execute();
    }

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public WrapperVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.TOPIC_ITEM.ordinal()) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_topic_details_header_item, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.TOPIC_ITEM);
        }else if (viewType == ITEM_TYPE.VIEW_POINT_ITEM.ordinal()) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_view_point_details_content_item, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.VIEW_POINT_ITEM);
        }else {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_mind_details_comment_item, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.COMMENT_LIST);
        }
    }

    @Override
    public void onBindViewHolder(WrapperVH holder, final int position) {
        if (position == 0) {
            RequestOptions options = new RequestOptions()
                    .signature(new ObjectKeyCanNull(topicListDataItem.getImage_update_datetime()).getObject());
            Glide.with(getContext()).load(topicListDataItem.getImage_url()).apply(options).into(holder.bannerImage);
            holder.titleText.setText(topicListDataItem.getTitle());
            holder.summaryText.setText(topicListDataItem.getSummary());
        } else if (position == 1) {
            holder.viewPointContentText.setText(viewPointListDataItem.getContent());
            if(viewPointListDataItem.getCover_image_url()!=null&&!viewPointListDataItem.getCover_image_url().isEmpty()){
                holder.viewPointContentImage.setVisibility(View.VISIBLE);
                RequestOptions options = new RequestOptions()
                        .signature(new ObjectKeyCanNull(viewPointListDataItem.getCover_image_update_datetime()).getObject());
                Glide.with(getContext()).load(viewPointListDataItem.getCover_image_url()).apply(options).into(holder.viewPointContentImage);
            }else{
                holder.viewPointContentImage.setVisibility(View.GONE);
            }
            holder.viewPointAuthorNameText.setText(String.format(getContext().getResources().getString(R.string.view_point_details_author_name_text),viewPointListDataItem.getView_point_author_nickname()));
            holder.viewPointReleaseDatetimeText.setText(String.format(getContext().getResources().getString(R.string.view_point_details_release_datetime_text),viewPointListDataItem.getRelease_datetime()));
        }else {
            holder.mindDetailsCommentCountText.setText(String.format(getContext().getResources().getString(R.string.mind_details_comment_count_text), viewPointListDataItem.getComment_num()));
            holder.commentRecyclerView.setFocusableInTouchMode(false);
            if (holder.commentRecyclerView.getAdapter() == null) {
                holder.commentRecyclerView.setAdapter(mMindCommentDataAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext());
                holder.commentRecyclerView.setLayoutManager(contentRvLayoutManager);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.TOPIC_ITEM.ordinal();
        } else if(position == 1){
            return ITEM_TYPE.VIEW_POINT_ITEM.ordinal();
        }else {
            return ITEM_TYPE.COMMENT_LIST.ordinal();
        }
    }

    public class WrapperVH extends RecyclerView.ViewHolder {
        /*Topic_ITEM*/
        private ImageView bannerImage;
        private TextView titleText;
        private TextView summaryText;

        /*View_Point_ITEM*/
        private TextView viewPointContentText;
        private ImageView viewPointContentImage;
        private TextView viewPointAuthorNameText;
        private TextView viewPointReleaseDatetimeText;

        /*Comment Item*/
        private TextView mindDetailsCommentCountText;
        public RecyclerView commentRecyclerView;

        public WrapperVH(View view, ITEM_TYPE item_type) {
            super(view);
            if (item_type == ITEM_TYPE.TOPIC_ITEM) {
                bannerImage = view.findViewById(R.id.topic_Details_Banner_Image);
                titleText = view.findViewById(R.id.topic_Details_Title);
                summaryText = view.findViewById(R.id.topic_Details_Summary);
            }else if (item_type == ITEM_TYPE.VIEW_POINT_ITEM) {
                viewPointContentText = view.findViewById(R.id.view_Point_Details_Content_Text);
                viewPointContentImage = view.findViewById(R.id.view_Point_Details_Content_Image);
                viewPointAuthorNameText = view.findViewById(R.id.view_Point_Details_Author_Name);
                viewPointReleaseDatetimeText = view.findViewById(R.id.view_Point_Details_Release_Datetime);
            } else {
                mindDetailsCommentCountText = view.findViewById(R.id.mind_Details_Comment_Count);
                commentRecyclerView = view.findViewById(R.id.mind_Details_Comment_RecyclerView);
            }
        }
    }

    private enum ITEM_TYPE {
        TOPIC_ITEM,
        VIEW_POINT_ITEM,
        COMMENT_LIST
    }

    class UpdateAsyncTask extends AsyncTask<Void, Void, String> {
        private WeakReference<Context> mWeakContextReference;

        UpdateAsyncTask(Context mContext) {
            this.mWeakContextReference = new WeakReference<>(mContext);
        }

        private Context getContext() {
            if(mWeakContextReference.get() != null){
                return mWeakContextReference.get();
            }
            return null;
        }

        @Override
        protected String doInBackground(Void... params) {
            String remoteDataFileUrl="http://www.itc.ink/data/explorefuture_data/app/recommend/mind/data_comment.json";
            String resultStr=getRemoteData(remoteDataFileUrl);
            return resultStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                ArrayList<CommentDataMode> dataArray;
                JsonReader jsonReader = new JsonReader(new StringReader(s));
                jsonReader.setLenient(true);
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(jsonReader);
                JsonObject rootObj = jsonElement.getAsJsonObject();
                JsonArray commentDataJsonArray = rootObj.getAsJsonArray("array_comment");
                Gson gson = new Gson();
                dataArray = gson.fromJson(commentDataJsonArray, new TypeToken<List<CommentDataMode>>() {
                }.getType());

                commentDataArray.clear();
                for (CommentDataMode commentDataMode : dataArray) {
                    commentDataArray.add(commentDataMode);
                }
                mMindCommentDataAdapter.notifyDataSetChanged();
            }
        }

        private String getRemoteData(String remoteDataFileUrl) {
            HttpURLConnection urlConnection = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL dataFileUrl = new URL(remoteDataFileUrl);

                urlConnection = (HttpURLConnection) dataFileUrl.openConnection();
                urlConnection.setConnectTimeout(3 * 1000);
                urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                InputStream inputStream = urlConnection.getInputStream();

                if (!urlConnection.getContentType().contains("json")) {
                    Log.d(LOG_TAG, "网络已被重定向，需确保网络通畅");
                    inputStream.close();
                    return null;
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String catchStr;
                while ((catchStr = bufferedReader.readLine()) != null) {
                    stringBuilder.append(catchStr);
                }

                bufferedReader.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return stringBuilder.toString();
        }

    }
}
