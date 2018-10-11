package itc.ink.explorefuture_android.recommend.mind_fragment.mode;

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

import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.dataupdate.DataUpdateMode;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic.TopicListDataMode;


/**
 * Created by yangwenjiang on 2018/9/16.
 */

public class DefResDataLoader implements DataLoad.OutService {
    private static final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DefResDataLoader";
    private final String JSON_DATA_KEY_TOPIC="array_topic";
    private final String JSON_DATA_KEY_MIND="array_mind";

    @Override
    public boolean prepareData() {
        if((DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR==null||DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR.trim().equals(""))||
                (DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR==null||DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR.trim().equals(""))){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Object loadTopicData() {
        ArrayList<TopicListDataMode> topicDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray topicDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_TOPIC);
        Gson gson = new Gson();
        topicDataArray = gson.fromJson(topicDataJsonArray, new TypeToken<List<TopicListDataMode>>() {
        }.getType());

        return topicDataArray;
    }

    @Override
    public Object loadMindHottestData() {
        ArrayList<MindListDataMode> mindHottestDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray mindDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_MIND);
        Gson gson = new Gson();
        mindHottestDataArray = gson.fromJson(mindDataJsonArray, new TypeToken<List<MindListDataMode>>() {
        }.getType());

        return mindHottestDataArray;
    }

    @Override
    public Object loadMindNewestData() {
        ArrayList<MindListDataMode> mindNewestDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray mindDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_MIND);
        Gson gson = new Gson();
        mindNewestDataArray = gson.fromJson(mindDataJsonArray, new TypeToken<List<MindListDataMode>>() {
        }.getType());

        return mindNewestDataArray;
    }
}
