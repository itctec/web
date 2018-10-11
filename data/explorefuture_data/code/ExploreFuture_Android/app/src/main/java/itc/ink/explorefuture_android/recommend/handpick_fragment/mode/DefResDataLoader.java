package itc.ink.explorefuture_android.recommend.handpick_fragment.mode;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

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
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.DataLoad;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_action.ActionListDataModel;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_action.ActionSubjectDataMode;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_banner.BannerDataMode;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_interest.InterestDataModel;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_product.ProductDataMode;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_solution.SolutionDataMode;


/**
 * Created by yangwenjiang on 2018/9/16.
 */

public class DefResDataLoader implements DataLoad.OutService {
    private static final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DefResDataLoader";
    private final String JSON_DATA_KEY_BANNER="array_banner";
    private final String JSON_DATA_KEY_SOLUTION="array_solution";
    private final String JSON_DATA_KEY_ACTION_SUBJECT="array_action_subject";
    private final String JSON_DATA_KEY_ACTION_LIST="array_action_list";
    private final String JSON_DATA_KEY_PRODUCT="array_product";
    private final String JSON_DATA_KEY_INTEREST_LIST="array_interest_list";

    @Override
    public boolean prepareData() {
        if(DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR==null||DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR.trim().equals("")){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Object loadBannerData() {
        ArrayList<BannerDataMode> bannerDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR));
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
    public Object loadSolutionData() {
        ArrayList<SolutionDataMode> solutionDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray solutionDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_SOLUTION);
        Gson gson = new Gson();
        solutionDataArray = gson.fromJson(solutionDataJsonArray, new TypeToken<List<SolutionDataMode>>() {
        }.getType());

        return solutionDataArray;
    }

    @Override
    public Object loadActionSubjectData() {
        ArrayList<ActionSubjectDataMode> actionSubjectDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray actionSubjectDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_ACTION_SUBJECT);
        Gson gson = new Gson();
        actionSubjectDataArray = gson.fromJson(actionSubjectDataJsonArray, new TypeToken<List<ActionSubjectDataMode>>() {
        }.getType());

        return actionSubjectDataArray;
    }

    @Override
    public Object loadActionListData() {
        ArrayList<ActionListDataModel> actionListDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray actionListDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_ACTION_LIST);
        Gson gson = new Gson();
        actionListDataArray = gson.fromJson(actionListDataJsonArray, new TypeToken<List<ActionListDataModel>>() {
        }.getType());

        return actionListDataArray;
    }

    @Override
    public Object loadProductData() {
        ArrayList<ProductDataMode> productDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray productDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_PRODUCT);
        Gson gson = new Gson();
        productDataArray = gson.fromJson(productDataJsonArray, new TypeToken<List<ProductDataMode>>() {
        }.getType());
        return productDataArray;
    }

    @Override
    public Object loadInterestData() {
        ArrayList<InterestDataModel> interestDataArray;
        ArrayList<InterestDataModel> interestDataArrays = new ArrayList<>();

        JsonReader jsonReader = new JsonReader(new StringReader(DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR));
        jsonReader.setLenient(true);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonReader);
        JsonObject rootObj = jsonElement.getAsJsonObject();
        JsonArray interestListDataJsonArray = rootObj.getAsJsonArray(JSON_DATA_KEY_INTEREST_LIST);
        Gson gson = new Gson();
        interestDataArray = gson.fromJson(interestListDataJsonArray, new TypeToken<List<InterestDataModel>>() {
        }.getType());

        for (int i = 0; i < 20; i++) {
            interestDataArrays.addAll(interestDataArray);
        }
        return interestDataArrays;
    }
}
