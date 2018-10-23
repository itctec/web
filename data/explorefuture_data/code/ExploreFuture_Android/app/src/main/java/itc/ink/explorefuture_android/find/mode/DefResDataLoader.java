package itc.ink.explorefuture_android.find.mode;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private String FIND_JSON_DATA_STR="";

    @Override
    public boolean prepareData(Context mContext) {
        StringBuilder stringBuilder = new StringBuilder();

        File localDataFile = new File(mContext.getFilesDir(), DataUpdateMode.FIND_LOCAL_DATA_FILE_NAME);
        BufferedReader bufferedReader;
        if (localDataFile.exists()) {
            try {
                InputStream inputStream = new FileInputStream(localDataFile);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String catchStr;
                while ((catchStr = bufferedReader.readLine()) != null) {
                    stringBuilder.append(catchStr);
                }
                FIND_JSON_DATA_STR = stringBuilder.toString();

                inputStream.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(FIND_JSON_DATA_STR==null||FIND_JSON_DATA_STR.trim().equals("")){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Object loadBannerData() {
        ArrayList<BannerDataMode> bannerDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(FIND_JSON_DATA_STR));
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

        JsonReader jsonReader = new JsonReader(new StringReader(FIND_JSON_DATA_STR));
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
