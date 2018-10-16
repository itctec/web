package itc.ink.explorefuture_android.mind.data_fragment.mode;

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


/**
 * Created by yangwenjiang on 2018/9/16.
 */

public class DefResDataLoader implements DataLoad.OutService {
    private static final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DefResDataLoader";
    private final String JSON_DATA_KEY_MIND="array_mind";

    @Override
    public boolean prepareData() {
        if(DataUpdateMode.MIND_JSON_DATA_STR==null||DataUpdateMode.MIND_JSON_DATA_STR.trim().equals("")){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Object loadMindData() {
        ArrayList<MindListDataMode> attentionDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.MIND_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray attentionDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_MIND);
        Gson gson = new Gson();
        attentionDataArray = gson.fromJson(attentionDataJsonArray, new TypeToken<List<MindListDataMode>>() {
        }.getType());

        return attentionDataArray;
    }
}
