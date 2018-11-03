package itc.ink.explorefuture_android.common_unit.mind_details.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.adapter.MindItemImageDataAdapter;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.common_unit.person_details.PersonDetailsActivity;
import itc.ink.explorefuture_android.common_unit.person_details.mode.SimplePersonInfoDataMode;
import itc.ink.explorefuture_android.common_unit.video_view.VideoViewerActivity;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class MindDetailsWrapAdapter extends RecyclerView.Adapter<MindDetailsWrapAdapter.WrapperVH> {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DetailsAdapter";
    private WeakReference<Context> mWeakContextReference;

    private MindListDataMode mindListDataItem;
    public ArrayList<CommentDataMode> commentDataArray=new ArrayList<>();
    public CommentDataAdapter mMindCommentDataAdapter;

    private MindItemCollectionBtnClickListener mindDetailsCollectionBtnClickListener=new MindItemCollectionBtnClickListener();

    public MindDetailsWrapAdapter(Context mContext, MindListDataMode mindListDataItem) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mindListDataItem=mindListDataItem;

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
        if (viewType == ITEM_TYPE.MIND_ITEM.ordinal()) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_mind_details_mind_item, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.MIND_ITEM);
        }else {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_mind_details_comment_item, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.COMMENT_LIST);
        }
    }

    @Override
    public void onBindViewHolder(WrapperVH holder, final int position) {
        if (position == 0) {
            String personId = mindListDataItem.getId().split("_")[0];
            SimplePersonInfoDataMode simplePersonInfoData=new SimplePersonInfoDataMode(personId,
                    mindListDataItem.getName(),
                    mindListDataItem.getHead_portrait_image_url(),
                    mindListDataItem.getHead_portrait_image_update_datetime());
            holder.mindDetailsHeaderLayout.setOnClickListener(new MindItemHeaderLayoutClickListener(simplePersonInfoData));

            RequestOptions options = new RequestOptions()
                    .signature(new ObjectKeyCanNull(mindListDataItem.getHead_portrait_image_update_datetime()).getObject())
                    .circleCrop();
            Glide.with(getContext()).load(mindListDataItem.getHead_portrait_image_url()).apply(options).into(holder.mindDetailsHeadPortrait);
            holder.mindDetailsName.setText(mindListDataItem.getName());
            holder.mindDetailsDatetime.setText(mindListDataItem.getDatetime());
            holder.mindDetailsCollectionBtn.setTag(mindListDataItem.getId());
            holder.mindDetailsCollectionBtn.setOnClickListener(mindDetailsCollectionBtnClickListener);

            if (mindListDataItem.getContent_text().trim().equals("")) {
                holder.mindDetailsContentText.setText(R.string.recommend_mind_content_empty_text);
            } else {
                holder.mindDetailsContentText.setText(mindListDataItem.getContent_text());
            }

            holder.mindDetailsContentText.setTag(mindListDataItem);

            holder.mindDetailsContentMediaLayout.removeAllViews();
            if (mindListDataItem.getImage_url_list().size() > 0 && mindListDataItem.getImage_url_list().size() <= 9) {
                addPicToLayout(holder, mindListDataItem.getImage_url_list(), mindListDataItem.getContent_text());
            } else if (mindListDataItem.getImage_url_list().size() > 9) {
                addPicToLayout(holder, mindListDataItem.getImage_url_list().subList(0, 9), mindListDataItem.getContent_text());
            } else if (!(mindListDataItem.getVideo_url() == null || mindListDataItem.getVideo_url().trim().equals(""))) {
                addVideoToLayout(holder, mindListDataItem.getVideo_url(), mindListDataItem.getContent_text());
            }
        } else {
            holder.mindDetailsCommentCountText.setText(String.format(getContext().getResources().getString(R.string.mind_details_comment_count_text), mindListDataItem.getComment_num()));
            holder.commentRecyclerView.setFocusableInTouchMode(false);
            if (holder.commentRecyclerView.getAdapter() == null) {
                holder.commentRecyclerView.setAdapter(mMindCommentDataAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext());
                holder.commentRecyclerView.setLayoutManager(contentRvLayoutManager);
            }
        }

    }

    private void addPicToLayout(WrapperVH holder, List<String> imageUrlList, String contentText) {
        RecyclerView imageRecyclerView = new RecyclerView(getContext());
        imageRecyclerView.setId(R.id.mind_ListItem_Content_Media_Image_RecyclerView);
        MindItemImageDataAdapter contentRvAdapter = new MindItemImageDataAdapter(getContext(), imageUrlList, contentText);
        imageRecyclerView.setAdapter(contentRvAdapter);
        RecyclerView.LayoutManager contentRvLayoutManager = new GridLayoutManager(getContext(), 3);
        imageRecyclerView.setLayoutManager(contentRvLayoutManager);
        imageRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        imageRecyclerView.setFocusableInTouchMode(false);

        DividerItemDecoration dividerItemDecorationOne = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecorationOne.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.mind_image_divider_horizontal));
        imageRecyclerView.addItemDecoration(dividerItemDecorationOne);
        DividerItemDecoration dividerItemDecorationTwo = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecorationTwo.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.mind_image_divider_vertical));
        imageRecyclerView.addItemDecoration(dividerItemDecorationTwo);
        holder.mindDetailsContentMediaLayout.addView(imageRecyclerView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.constrainWidth(imageRecyclerView.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(imageRecyclerView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(imageRecyclerView.getId(), ConstraintSet.TOP, holder.mindDetailsContentText.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(imageRecyclerView.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(imageRecyclerView.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);

        constraintSet.applyTo(holder.mindDetailsContentMediaLayout);
    }

    private void addVideoToLayout(WrapperVH holder, String videoUrl, String contentText) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.common_unit_mind_list_item_video_gif_list_item, null, false);
        rootView.setId(R.id.mind_ListItem_Content_Media_Video_Gif);
        ImageView videoGifView = rootView.findViewById(R.id.mind_ListItem_Video_Gif_Item);
        videoGifView.setOnClickListener(new VideoGifViewClickListener(videoUrl, contentText));
        Glide.with(getContext()).load(videoUrl.replace(".mp4", ".gif")).into(videoGifView);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.constrainWidth(rootView.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(rootView.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(rootView.getId(), ConstraintSet.TOP, holder.mindDetailsContentText.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(rootView.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        constraintSet.connect(rootView.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);

        holder.mindDetailsContentMediaLayout.addView(rootView);
        constraintSet.applyTo(holder.mindDetailsContentMediaLayout);
    }


    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.MIND_ITEM.ordinal();
        } else {
            return ITEM_TYPE.COMMENT_LIST.ordinal();
        }
    }

    public class WrapperVH extends RecyclerView.ViewHolder {
        /*MIND_ITEM*/
        private ConstraintLayout mindDetailsHeaderLayout;
        private ImageView mindDetailsHeadPortrait;
        private TextView mindDetailsName;
        private TextView mindDetailsDatetime;
        private ImageView mindDetailsCollectionBtn;
        private TextView mindDetailsContentText;
        private ConstraintLayout mindDetailsContentMediaLayout;

        /*Comment Item*/
        private TextView mindDetailsCommentCountText;
        public RecyclerView commentRecyclerView;

        public WrapperVH(View view, ITEM_TYPE item_type) {
            super(view);
            if (item_type == ITEM_TYPE.MIND_ITEM) {
                mindDetailsHeaderLayout = view.findViewById(R.id.mind_Details_Header_Layout);
                mindDetailsHeadPortrait = view.findViewById(R.id.mind_Details_HeadPortrait);
                mindDetailsName = view.findViewById(R.id.mind_Details_Name);
                mindDetailsDatetime = view.findViewById(R.id.mind_Details_Datetime);
                mindDetailsCollectionBtn = view.findViewById(R.id.mind_Details_Collection_Btn);
                mindDetailsContentText = view.findViewById(R.id.mind_Details_Content_Text);
                mindDetailsContentMediaLayout = view.findViewById(R.id.mind_Details_Content_Media_Layout);
            } else {
                mindDetailsCommentCountText = view.findViewById(R.id.mind_Details_Comment_Count);
                commentRecyclerView = view.findViewById(R.id.mind_Details_Comment_RecyclerView);
            }
        }
    }

    private enum ITEM_TYPE {
        MIND_ITEM,
        COMMENT_LIST
    }


    class MindItemHeaderLayoutClickListener implements View.OnClickListener {
        private SimplePersonInfoDataMode simplePersonInfoData;

        public MindItemHeaderLayoutClickListener(SimplePersonInfoDataMode simplePersonInfoData) {
            this.simplePersonInfoData = simplePersonInfoData;
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getContext(), PersonDetailsActivity.class);
            intent.putExtra(PersonDetailsActivity.KEY_PERSON_INFO_ITEM,simplePersonInfoData);
            getContext().startActivity(intent);
        }
    }

    class MindItemCollectionBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String ID=(String)view.getTag();
            Toast.makeText(getContext(), ID + "被收藏", Toast.LENGTH_SHORT).show();
        }
    }

    class VideoGifViewClickListener implements View.OnClickListener {
        private String videoUrl = "";
        private String contentText = "";

        public VideoGifViewClickListener(String videoUrl, String contentText) {
            this.videoUrl = videoUrl;
            this.contentText = contentText;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), VideoViewerActivity.class);
            intent.putExtra(VideoViewerActivity.KEY_VIDEO_URL, videoUrl);
            intent.putExtra(VideoViewerActivity.KEY_CONTENT_TEXT, contentText);
            getContext().startActivity(intent);
        }
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
                commentDataArray.addAll(dataArray);
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
