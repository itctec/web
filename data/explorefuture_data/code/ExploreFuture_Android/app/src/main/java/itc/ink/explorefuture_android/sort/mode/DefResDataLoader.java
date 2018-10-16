package itc.ink.explorefuture_android.sort.mode;

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
import itc.ink.explorefuture_android.sort.mode.mode_sort.SortListDataMode;


/**
 * Created by yangwenjiang on 2018/9/16.
 */

public class DefResDataLoader implements DataLoad.OutService {
    private static final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DefResDataLoader";
    private final String JSON_DATA_KEY_ROOT="array_base_sort";

    @Override
    public boolean prepareData() {
        if(DataUpdateMode.SORT_ALL_JSON_DATA_STR==null||DataUpdateMode.SORT_ALL_JSON_DATA_STR.trim().equals("")){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Object loadSortData() {
        ArrayList<SortListDataMode> sortDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.SORT_ALL_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray sortDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_ROOT);
        Gson gson = new Gson();
        sortDataArray = gson.fromJson(sortDataJsonArray, new TypeToken<List<SortListDataMode>>() {
        }.getType());

        return sortDataArray;
    }
}
