package itc.ink.explorefuture_android.common_unit.topic_details.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import itc.ink.explorefuture_android.common_unit.mind_details.mode.CommentDataMode;
import itc.ink.explorefuture_android.common_unit.topic_details.mode.ViewPointDataMode;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic.TopicListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class TopicDetailsWrapAdapter extends RecyclerView.Adapter<TopicDetailsWrapAdapter.WrapperVH> {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DetailsAdapter";
    private WeakReference<Context> mWeakContextReference;

    private TopicListDataMode topicListDataItem;
    public ArrayList<ViewPointDataMode> viewPointDataArray=new ArrayList<>();
    public TopicViewPointDataAdapter mTopicViewPointDataAdapter;


    public TopicDetailsWrapAdapter(Context mContext, TopicListDataMode topicListDataItem) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.topicListDataItem=topicListDataItem;

        mTopicViewPointDataAdapter=new TopicViewPointDataAdapter(getContext(),topicListDataItem,viewPointDataArray);

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
        }else {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_topic_details_view_point_item, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.VIEW_POINT_LIST);
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
        } else {
            holder.viewPointRecyclerView.setFocusableInTouchMode(false);
            if (holder.viewPointRecyclerView.getAdapter() == null) {
                holder.viewPointRecyclerView.setAdapter(mTopicViewPointDataAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.VERTICAL);
                holder.viewPointRecyclerView.setLayoutManager(contentRvLayoutManager);

                DividerItemDecoration dividerItemDecorationOne = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                dividerItemDecorationOne.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.view_point_divider_horizontal));
                holder.viewPointRecyclerView.addItemDecoration(dividerItemDecorationOne);
                DividerItemDecoration dividerItemDecorationTwo = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
                dividerItemDecorationTwo.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.view_point_divider_vertical));
                holder.viewPointRecyclerView.addItemDecoration(dividerItemDecorationTwo);
            }
        }

    }



    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.TOPIC_ITEM.ordinal();
        } else {
            return ITEM_TYPE.VIEW_POINT_LIST.ordinal();
        }
    }

    public class WrapperVH extends RecyclerView.ViewHolder {
        /*MIND_ITEM*/
        private ImageView bannerImage;
        private TextView titleText;
        private TextView summaryText;

        /*Attention Item*/
        public RecyclerView viewPointRecyclerView;

        public WrapperVH(View view, ITEM_TYPE item_type) {
            super(view);
            if (item_type == ITEM_TYPE.TOPIC_ITEM) {
                bannerImage = view.findViewById(R.id.topic_Details_Banner_Image);
                titleText = view.findViewById(R.id.topic_Details_Title);
                summaryText = view.findViewById(R.id.topic_Details_Summary);
            } else {
                viewPointRecyclerView = view.findViewById(R.id.topic_Details_View_Point_RecyclerView);
            }
        }
    }

    private enum ITEM_TYPE {
        TOPIC_ITEM,
        VIEW_POINT_LIST
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
            String remoteDataFileUrl="http://www.itc.ink/data/explorefuture_data/app/recommend/mind/topic/data_view_point.json";
            String resultStr=getRemoteData(remoteDataFileUrl);
            return resultStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                ArrayList<ViewPointDataMode> dataArray;
                JsonReader jsonReader = new JsonReader(new StringReader(s));
                jsonReader.setLenient(true);
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(jsonReader);
                JsonObject rootObj = jsonElement.getAsJsonObject();
                JsonArray viewPointDataJsonArray = rootObj.getAsJsonArray("array_view_point");
                Gson gson = new Gson();
                dataArray = gson.fromJson(viewPointDataJsonArray, new TypeToken<List<ViewPointDataMode>>() {
                }.getType());

                viewPointDataArray.clear();
                for (ViewPointDataMode viewPointDataMode : dataArray) {
                    viewPointDataArray.add(viewPointDataMode);
                }
                mTopicViewPointDataAdapter.notifyDataSetChanged();
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
