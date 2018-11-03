package itc.ink.explorefuture_android.common_unit.person_details.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
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
import itc.ink.explorefuture_android.app.app_level.GlideCircleWithBorder;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.adapter.MindDataAdapter;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class PersonDetailsWrapAdapter extends RecyclerView.Adapter<PersonDetailsWrapAdapter.WrapperVH> {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DetailsAdapter";
    private WeakReference<Context> mWeakContextReference;

    private String id;
    private String str_nickname ="",
            str_personalized_signature="",
            str_fans_count ="",
            str_attention_count="",
            str_head_portrait_image_url="",
            str_head_portrait_image_update_datetime="",
            str_personal_cover_bg_image_url="",
            str_personal_cover_bg_image_update_datetime="";
    private ArrayList<MindListDataMode> mMindListData = new ArrayList<>();
    public MindDataAdapter mMindDataAdapter;


    public PersonDetailsWrapAdapter(Context mContext, String id) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.id = id;

        mMindDataAdapter = new MindDataAdapter(getContext(), mMindListData);

        //Get Comment Data
        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask();
        updateAsyncTask.execute();
    }

    private Context getContext() {
        if (mWeakContextReference.get() != null) {
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public WrapperVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.HEADER_ITEM.ordinal()) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_person_details_header_item, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.HEADER_ITEM);
        } else {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_unit_mind_recycler_layout, parent, false);
            return new WrapperVH(rootView, ITEM_TYPE.MIND_LIST);
        }
    }

    @Override
    public void onBindViewHolder(final WrapperVH holder, final int position) {
        if (position == 0) {
            holder.nicknameText.setText(str_nickname);
            holder.personalizedSignatureText.setText(str_personalized_signature);
            holder.fansCountText.setText(String.format(getContext().getResources().getString(R.string.mine_fans_count_text), str_fans_count));
            holder.attentionCountText.setText(String.format(getContext().getResources().getString(R.string.mine_attention_count_text), str_attention_count));

            RequestOptions options = new RequestOptions();
            options.signature(new ObjectKeyCanNull(str_personal_cover_bg_image_update_datetime).getObject());
            SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    holder.personInfoLayout.setBackground(resource);
                }
            };
            Glide.with(getContext()).load(str_personal_cover_bg_image_url).apply(options).into(simpleTarget);

            options.signature(new ObjectKeyCanNull(str_head_portrait_image_update_datetime).getObject());
            options.circleCrop();
            options.transform(new GlideCircleWithBorder(1, Color.parseColor("#ccffffff")));
            Glide.with(getContext()).load(str_head_portrait_image_url).apply(options).into(holder.headPortraitImage);
        }else{
            holder.mindRecyclerView.setFocusableInTouchMode(false);
            if (holder.mindRecyclerView.getAdapter() == null) {
                holder.mindRecyclerView.setAdapter(mMindDataAdapter);
                RecyclerView.LayoutManager contentRvLayoutManager = new LinearLayoutManager(getContext());
                holder.mindRecyclerView.setLayoutManager(contentRvLayoutManager);

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
            return ITEM_TYPE.HEADER_ITEM.ordinal();
        } else {
            return ITEM_TYPE.MIND_LIST.ordinal();
        }
    }

    public class WrapperVH extends RecyclerView.ViewHolder {
        /*MIND_ITEM*/
        private ConstraintLayout personInfoLayout;
        private ImageView headPortraitImage;
        private TextView nicknameText;
        private TextView personalizedSignatureText;
        private TextView fansCountText;
        private TextView attentionCountText;

        /*Attention Item*/
        public RecyclerView mindRecyclerView;

        public WrapperVH(View view, ITEM_TYPE item_type) {
            super(view);
            if (item_type == ITEM_TYPE.HEADER_ITEM) {
                personInfoLayout = view.findViewById(R.id.person_Details_Person_Info_Layout);
                headPortraitImage = view.findViewById(R.id.person_Details_HeadPortrait_Image);
                nicknameText = view.findViewById(R.id.person_Details_Nickname_Text);
                personalizedSignatureText = view.findViewById(R.id.person_Details_Personalized_Signature_Text);
                fansCountText = view.findViewById(R.id.person_Details_Fans_Count_Text);
                attentionCountText = view.findViewById(R.id.person_Details_Attention_Count_Text);
            } else {
                mindRecyclerView = view.findViewById(R.id.common_Unit_Mind_RecyclerView);
            }
        }
    }

    private enum ITEM_TYPE {
        HEADER_ITEM,
        MIND_LIST
    }

    class UpdateAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String remoteDataFileUrl = "http://www.itc.ink/data/explorefuture_data/account/0000000001/information/data_show_out.json";
            String resultStr = getRemoteData(remoteDataFileUrl);
            return resultStr;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                JsonReader jsonReader = new JsonReader(new StringReader(s));
                jsonReader.setLenient(true);
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(jsonReader);
                JsonObject rootObj = element.getAsJsonObject();
                JsonPrimitive jsonPrimitive_id = rootObj.getAsJsonPrimitive("id");
                String str_id = jsonPrimitive_id.getAsString();
                JsonPrimitive jsonPrimitive_nickname = rootObj.getAsJsonPrimitive("nickname");
                str_nickname = jsonPrimitive_nickname.getAsString();
                JsonPrimitive jsonPrimitive_personalized_signature = rootObj.getAsJsonPrimitive("personalized_signature");
                str_personalized_signature = jsonPrimitive_personalized_signature.getAsString();
                JsonPrimitive jsonPrimitive_fans_count = rootObj.getAsJsonPrimitive("fans_count");
                str_fans_count = jsonPrimitive_fans_count.getAsString();
                JsonPrimitive jsonPrimitive_attention_count = rootObj.getAsJsonPrimitive("attention_count");
                str_attention_count = jsonPrimitive_attention_count.getAsString();
                JsonPrimitive jsonPrimitive_head_portrait_image_url = rootObj.getAsJsonPrimitive("head_portrait_image_url");
                str_head_portrait_image_url = jsonPrimitive_head_portrait_image_url.getAsString();
                JsonPrimitive jsonPrimitive_head_portrait_image_update_datetime = rootObj.getAsJsonPrimitive("head_portrait_image_update_datetime");
                str_head_portrait_image_update_datetime = jsonPrimitive_head_portrait_image_update_datetime.getAsString();
                JsonPrimitive jsonPrimitive_personal_cover_bg_image_url = rootObj.getAsJsonPrimitive("personal_cover_bg_image_url");
                str_personal_cover_bg_image_url = jsonPrimitive_personal_cover_bg_image_url.getAsString();
                JsonPrimitive jsonPrimitive_personal_cover_bg_image_update_datetime = rootObj.getAsJsonPrimitive("personal_cover_bg_image_update_datetime");
                str_personal_cover_bg_image_update_datetime = jsonPrimitive_personal_cover_bg_image_update_datetime.getAsString();

                JsonArray mindDataJsonArray = rootObj.getAsJsonArray("array_mind");
                Gson gson = new Gson();
                ArrayList<MindListDataMode> mindDataArray = gson.fromJson(mindDataJsonArray, new TypeToken<List<MindListDataMode>>() {
                }.getType());

                mMindListData.addAll(mindDataArray);
                notifyDataSetChanged();
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
