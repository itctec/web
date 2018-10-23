package itc.ink.explorefuture_android.mind.data_fragment.mode;

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
import itc.ink.explorefuture_android.common_unit.mind_recyclerview.mode.MindListDataMode;
import itc.ink.explorefuture_android.login.LoginStateInstance;


/**
 * Created by yangwenjiang on 2018/9/16.
 */

public class DefResDataLoader implements DataLoad.OutService {
    private static final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DefResDataLoader";
    private final String JSON_DATA_KEY_MIND="array_mind";

    private String MIND_JSON_DATA_STR="";

    @Override
    public boolean prepareData(Context mContext) {
        StringBuilder stringBuilder = new StringBuilder();
        String filePath="/"+ LoginStateInstance.getInstance().getId();
        File localDataFile = new File(mContext.getFilesDir()+filePath, DataUpdateMode.MIND_LOCAL_DATA_FILE_NAME);
        BufferedReader bufferedReader;
        if (localDataFile.exists()) {
            try {
                InputStream inputStream = new FileInputStream(localDataFile);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String catchStr;
                while ((catchStr = bufferedReader.readLine()) != null) {
                    stringBuilder.append(catchStr);
                }
                MIND_JSON_DATA_STR = stringBuilder.toString();

                inputStream.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(MIND_JSON_DATA_STR==null||MIND_JSON_DATA_STR.trim().equals("")){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Object loadMindData() {
        ArrayList<MindListDataMode> attentionDataArray;

        JsonReader jsonReader = new JsonReader(new StringReader(MIND_JSON_DATA_STR));
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
