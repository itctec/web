package itc.ink.explorefuture_android.find.mode;

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
import itc.ink.explorefuture_android.find.mode.mode_banner.BannerDataMode;


/**
 * Created by yangwenjiang on 2018/9/16.
 */

public class DefResDataLoader implements DataLoad.OutService {
    private static final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DefResDataLoader";
    private final String JSON_DATA_KEY_BANNER="array_banner";
    private final String JSON_DATA_KEY_HOT_SEARCH="array_hot_search";

    @Override
    public boolean prepareData() {
        if(DataUpdateMode.FIND_JSON_DATA_STR==null||DataUpdateMode.FIND_JSON_DATA_STR.trim().equals("")){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Object loadBannerData() {
        ArrayList<BannerDataMode> bannerDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.FIND_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray bannerDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_BANNER);
        Gson gson = new Gson();
        bannerDataArray = gson.fromJson(bannerDataJsonArray, new TypeToken<List<BannerDataMode>>() {
        }.getType());

        return bannerDataArray;
    }

    @Override
    public Object loadHotSearchData() {
        ArrayList<String> hotSearchDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.FIND_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray hotSearchDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_HOT_SEARCH);
        Gson gson = new Gson();
        hotSearchDataArray = gson.fromJson(hotSearchDataJsonArray, new TypeToken<List<String>>() {
        }.getType());

        return hotSearchDataArray;
    }
}
