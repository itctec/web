package itc.ink.explorefuture_android.app.utils.dataupdate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import itc.ink.explorefuture_android.app.activity.MainActivity;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.DataTimeUtil;
import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;
import itc.ink.explorefuture_android.app.utils.SharedPreferenceUtil;
import itc.ink.explorefuture_android.login.LoginStateInstance;

/**
 * Created by yangwenjiang on 2018/9/27.
 */

public class DataUpdateUtil {
    private static final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DataUpdateUtil";
    public static final int UPDATE_DATA_FINISH_MSG = 0x01;

    public static final String UPDATE_RESULT_FAILED = "UPDATE_FAILED";
    public static final String UPDATE_RESULT_NEWEST_ALREADY = "NEWEST_ALREADY";

    private static Context mContext;
    private List<DataUpdateMode> dataUpdateList;

    private ExecutorService threadPool;

    private Handler mHandler;

    public DataUpdateUtil(Context mContext, List<DataUpdateMode> dataUpdateList, Handler mHandler) {
        this.mContext = mContext;
        this.dataUpdateList = dataUpdateList;
        this.mHandler = mHandler;
    }

    public void updateData() {
        threadPool = Executors.newFixedThreadPool(dataUpdateList.size());

        //Get Server Data
        for (int i = 0; i < dataUpdateList.size(); i++) {
            threadPool.submit(new GetUpdateRunnable(dataUpdateList.get(i)));
        }

        //Check Update State
        threadPool.submit(new CheckUpdateState());
        //Shutdown ThreadPool
        threadPool.shutdown();
    }

    public static String updateData(Context mContext, DataUpdateMode dataUpdateMode) {
        DataUpdateUtil.mContext = mContext;
        return handleUpdate(dataUpdateMode);
    }

    private static String handleUpdate(DataUpdateMode dataUpdateMode) {
        String resultStr = null;

        //Get Update DateTime File From Server
        String updateDateTimeStr = getRemoteDataStr(dataUpdateMode.getUpdateDatetimeFileUrl());

        if (updateDateTimeStr == null) {
            dataUpdateMode.setCheckUpdateFinish(true);
            Log.d(LOG_TAG, "服务器数据获取失败！");
            resultStr = UPDATE_RESULT_FAILED;
            return resultStr;
        }

        //Json Phrase DateTime Str
        JsonReader jsonReader = new JsonReader(new StringReader(updateDateTimeStr));
        jsonReader.setLenient(true);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonReader);
        JsonObject rootObj = element.getAsJsonObject();
        JsonPrimitive serverUpdateDateTime = rootObj.getAsJsonPrimitive(dataUpdateMode.getDataNewestUpdateDateTimeKey());
        String serverDateTimeStr = null;
        if (serverUpdateDateTime != null) {
            serverDateTimeStr = serverUpdateDateTime.getAsString();
        } else {
            serverDateTimeStr = null;
        }
        String localDateTimeStr = SharedPreferenceUtil.getString(dataUpdateMode.getDataNewestUpdateDateTimeKey());

        if (serverDateTimeStr != null && !serverDateTimeStr.isEmpty() && localDateTimeStr != null && !localDateTimeStr.isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            int compareResult = DataTimeUtil.dateTimeCompare(serverDateTimeStr, localDateTimeStr, simpleDateFormat);
            switch (compareResult) {
                case 1:
                    //Update Data File
                    Log.d(LOG_TAG, dataUpdateMode.getLocalDataFileName() +"通过比较时间更新数据!");
                    resultStr = getRemoteDataFile(dataUpdateMode);
                    if (resultStr!=null&&!resultStr.isEmpty()){
                        SharedPreferenceUtil.putString(dataUpdateMode.getDataNewestUpdateDateTimeKey(), serverDateTimeStr);
                    }
                    break;
                default:
                    resultStr = UPDATE_RESULT_NEWEST_ALREADY;
                    dataUpdateMode.setCheckUpdateFinish(true);
                    Log.d(LOG_TAG, dataUpdateMode.getLocalDataFileName() +"当前已是最新数据!");
            }
        } else if (serverDateTimeStr != null && (localDateTimeStr == null || localDateTimeStr.isEmpty())) {
            //Update Data File
            Log.d(LOG_TAG, dataUpdateMode.getLocalDataFileName() +"首次更新数据!");
            resultStr = getRemoteDataFile(dataUpdateMode);
            if (resultStr!=null&&!resultStr.isEmpty()){
                SharedPreferenceUtil.putString(dataUpdateMode.getDataNewestUpdateDateTimeKey(), serverDateTimeStr);
            }
        } else {
            resultStr = UPDATE_RESULT_FAILED;
            dataUpdateMode.setCheckUpdateFinish(true);
            Log.d(LOG_TAG, dataUpdateMode.getLocalDataFileName() + "获取数据失败!");
        }
        return resultStr;
    }

    public static String getRemoteDataStr(String updateDatetimeFileUrl) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            URL updateFileUrl = new URL(updateDatetimeFileUrl);
            urlConnection = (HttpURLConnection) updateFileUrl.openConnection();
            urlConnection.setConnectTimeout(3 * 1000);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = urlConnection.getInputStream();

            if (!urlConnection.getContentType().contains("json")) {
                Log.d(LOG_TAG, "网络已被重定向，需确保网络通畅");
                inputStream.close();
                urlConnection.disconnect();
                return null;
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String lineStr;
            while ((lineStr = bufferedReader.readLine()) != null) {
                stringBuilder.append(lineStr);
            }
            inputStream.close();
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    private static String getRemoteDataFile(DataUpdateMode dataUpdateMode) {
        HttpURLConnection urlConnection = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL dataFileUrl = new URL(dataUpdateMode.getRemoteDataFileUrl());
            String filePath="/"+dataUpdateMode.getAccountID();
            File saveFile = new File(mContext.getFilesDir()+filePath, dataUpdateMode.getLocalDataFileName());
            File fileParent = saveFile.getParentFile();
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }
            urlConnection = (HttpURLConnection) dataFileUrl.openConnection();
            urlConnection.setConnectTimeout(3 * 1000);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = urlConnection.getInputStream();
            OutputStream outputStream = new FileOutputStream(saveFile);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String catchStr;
            while ((catchStr = bufferedReader.readLine()) != null) {
                stringBuilder.append(catchStr);
                if(!dataUpdateMode.getLocalDataFileName().equals(DataUpdateMode.MINE_LOCAL_DATA_FILE_NAME)){
                    bufferedWriter.write(catchStr);
                    bufferedWriter.flush();
                }
            }
            if((dataUpdateMode.getLocalDataFileName().equals(DataUpdateMode.MINE_LOCAL_DATA_FILE_NAME))&&
                    (!dataUpdateMode.getAccountID().trim().equals(""))&&
                    (dataUpdateMode.getAccountID().equals(LoginStateInstance.getInstance().getId()))){
                updateDatabase(stringBuilder.toString());
            }
            Log.d(LOG_TAG, dataUpdateMode.getLocalDataFileName() + "数据保存成功！");

            bufferedReader.close();
            bufferedWriter.close();
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        dataUpdateMode.setCheckUpdateFinish(true);
        return stringBuilder.toString();
    }

    private static void updateDatabase(String dataStr) {
        JsonReader jsonReader = new JsonReader(new StringReader(dataStr));
        jsonReader.setLenient(true);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonReader);
        JsonObject rootObj = element.getAsJsonObject();
        JsonPrimitive jsonPrimitive_id = rootObj.getAsJsonPrimitive("id");
        String str_id = jsonPrimitive_id.getAsString();
        JsonPrimitive jsonPrimitive_nickname = rootObj.getAsJsonPrimitive("nickname");
        String str_nickname = jsonPrimitive_nickname.getAsString();
        JsonPrimitive jsonPrimitive_personalized_signature = rootObj.getAsJsonPrimitive("personalized_signature");
        String str_personalized_signature = jsonPrimitive_personalized_signature.getAsString();
        JsonPrimitive jsonPrimitive_fans_count = rootObj.getAsJsonPrimitive("fans_count");
        String str_fans_count = jsonPrimitive_fans_count.getAsString();
        JsonPrimitive jsonPrimitive_attention_count = rootObj.getAsJsonPrimitive("attention_count");
        String str_attention_count = jsonPrimitive_attention_count.getAsString();
        JsonPrimitive jsonPrimitive_head_portrait_image_url = rootObj.getAsJsonPrimitive("head_portrait_image_url");
        String str_head_portrait_image_url = jsonPrimitive_head_portrait_image_url.getAsString();
        JsonPrimitive jsonPrimitive_head_portrait_image_update_datetime = rootObj.getAsJsonPrimitive("head_portrait_image_update_datetime");
        String str_head_portrait_image_update_datetime = jsonPrimitive_head_portrait_image_update_datetime.getAsString();
        JsonPrimitive jsonPrimitive_personal_cover_bg_image_url = rootObj.getAsJsonPrimitive("personal_cover_bg_image_url");
        String str_personal_cover_bg_image_url = jsonPrimitive_personal_cover_bg_image_url.getAsString();
        JsonPrimitive jsonPrimitive_personal_cover_bg_image_update_datetime = rootObj.getAsJsonPrimitive("personal_cover_bg_image_update_datetime");
        String str_personal_cover_bg_image_update_datetime = jsonPrimitive_personal_cover_bg_image_update_datetime.getAsString();


        if (str_id.equals(LoginStateInstance.getInstance().getId())) {
            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(mContext, SQLiteDBHelper.DATABASE_FILE_NAME, SQLiteDBHelper.DATABASE_VERSION);
            String sqlStr = "select * from tb_person_info where id=?";
            SQLiteDatabase sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(sqlStr, new String[]{str_id});

            ContentValues contentValues = new ContentValues();
            contentValues.put("nickname", str_nickname);
            contentValues.put("personalized_signature", str_personalized_signature);
            contentValues.put("fans_count", str_fans_count);
            contentValues.put("attention_count", str_attention_count);
            contentValues.put("head_portrait_image_url", str_head_portrait_image_url);
            contentValues.put("head_portrait_image_update_datetime", str_head_portrait_image_update_datetime);
            contentValues.put("personal_cover_bg_image_url", str_personal_cover_bg_image_url);
            contentValues.put("personal_cover_bg_image_update_datetime", str_personal_cover_bg_image_update_datetime);

            if (cursor.moveToNext()) {
                sqLiteDatabase.update("tb_person_info", contentValues, "id=?", new String[]{str_id});
            } else {
                contentValues.put("id", str_id);
                sqLiteDatabase.insert("tb_person_info", null, contentValues);
            }
            sqLiteDatabase.close();
            sqLiteDBHelper.close();
        } else {
            Log.d(LOG_TAG, "账号匹配错误");
        }
    }

    private class GetUpdateRunnable implements Runnable {
        private DataUpdateMode dataUpdateMode;

        public GetUpdateRunnable(DataUpdateMode dataUpdateMode) {
            this.dataUpdateMode = dataUpdateMode;
        }

        @Override
        public void run() {
            //Get Update DateTime File From Server
            handleUpdate(dataUpdateMode);
        }

    }


    private class CheckUpdateState implements Runnable {
        private boolean allCheckUpdateFinished = true;

        public CheckUpdateState() {
        }

        @Override
        public void run() {
            while (true) {
                allCheckUpdateFinished = true;
                for (int i = 0; i < dataUpdateList.size(); i++) {
                    if (dataUpdateList.get(i).isCheckUpdateFinish() == false) {
                        allCheckUpdateFinished = false;
                    }
                }

                if (allCheckUpdateFinished) {
                    Log.d(LOG_TAG, "数据更新已完成");

                    //Add Notify At Here
                    if (mHandler != null) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = UPDATE_DATA_FINISH_MSG;
                        mHandler.dispatchMessage(msg);
                        mHandler = null;
                    }

                    break;
                }

            }
        }
    }


}
