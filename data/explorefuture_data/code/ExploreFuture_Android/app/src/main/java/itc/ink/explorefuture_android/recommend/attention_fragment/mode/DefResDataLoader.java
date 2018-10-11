package itc.ink.explorefuture_android.recommend.attention_fragment.mode;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.recommend.attention_fragment.mode.mode_recommend.RecommendListDataMode;


/**
 * Created by yangwenjiang on 2018/9/16.
 */

public class DefResDataLoader implements DataLoad.OutService {
    private static final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DefResDataLoader";
    private final String JSON_DATA_KEY_RECOMMEND="array_recommend";
    private final String JSON_DATA_KEY_ATTENTION="array_attention";

    @Override
    public boolean prepareData() {
        if(DataUpdateMode.RECOMMEND_ATTENTION_JSON_DATA_STR==null||DataUpdateMode.RECOMMEND_ATTENTION_JSON_DATA_STR.trim().equals("")){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Object loadRecommendData() {
        ArrayList<RecommendListDataMode> recommendDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.RECOMMEND_ATTENTION_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray recommendDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_RECOMMEND);
        Gson gson = new Gson();
        recommendDataArray = gson.fromJson(recommendDataJsonArray, new TypeToken<List<RecommendListDataMode>>() {
        }.getType());

        return recommendDataArray;
    }

    @Override
    public Object loadAttentionData() {
        ArrayList<MindListDataMode> attentionDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.RECOMMEND_ATTENTION_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray attentionDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_ATTENTION);
        Gson gson = new Gson();
        attentionDataArray = gson.fromJson(attentionDataJsonArray, new TypeToken<List<MindListDataMode>>() {
        }.getType());

        return attentionDataArray;
    }
}
