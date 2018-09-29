package itc.ink.explorefuture_android.app.utils.dataupdate;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import itc.ink.explorefuture_android.app.utils.SharedPreferenceUtil;

/**
 * Created by yangwenjiang on 2018/9/27.
 */

public class DataUpdateUtil {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "DataUpdateUtil";
    private final String UPDATE_DATETIME_FILE_URL = "http://www.itc.ink/data/explorefuture_data/app/data_newest_update_date_time.json";
    private final String RECOMMEND_HANDPICK_DATA_FILE_URL = "http://www.itc.ink/data/explorefuture_data/app/recommend/recommend_handpick_data.json";
    private final String SP_KEY_RECOMMEND_HANDPICK_DATA_NEWEST_UPDATE_DATE_TIME = "recommend_handpick_data_newest_update_date_time";
    private final String RECOMMEND_HANDPICK_DATA_FILE_NAME = "recommend_handpick_data.json";

    private Context mContext;
    private List<DataUpdateMode> dataUpdateList;

    private ExecutorService threadPool;

    public DataUpdateUtil(Context mContext, List<DataUpdateMode> dataUpdateList) {
        this.mContext = mContext;
        this.dataUpdateList = dataUpdateList;
    }

    public void updateData() {
        threadPool = Executors.newFixedThreadPool(dataUpdateList.size());

        for (int i=0;i<dataUpdateList.size();i++){
            threadPool.submit(new GetUpdateRunnable(dataUpdateList.get(i)));
        }

        //Check Update State
        threadPool.submit(new CheckUpdateState());
        //Shutdown ThreadPool
        threadPool.shutdown();
    }

    private class GetUpdateRunnable implements Runnable{
        private DataUpdateMode dataUpdateMode;

        public GetUpdateRunnable(DataUpdateMode dataUpdateMode) {
            this.dataUpdateMode = dataUpdateMode;
        }

        @Override
        public void run() {
            //Get Update DateTime File From Server
            handleUpdate();
        }

        private void handleUpdate() {
            //Get Update DateTime File From Server
            String updateDateTimeStr = getUpdateStrFromServer(dataUpdateMode.getUpdateDatetimeFileUrl());
            Log.d(LOG_TAG, "来自服务器的数据->"+updateDateTimeStr);
            if (updateDateTimeStr == null) {
                dataUpdateMode.setCheckUpdateFinish(true);
                Log.d(LOG_TAG, "服务器数据获取失败！");
                return;
            }

            //Json Phrase DateTime Str
            JsonReader jsonReader = new JsonReader(new StringReader(updateDateTimeStr));
            jsonReader.setLenient(true);
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(jsonReader);
            JsonObject rootObj = element.getAsJsonObject();
            JsonPrimitive serverUpdateDateTime = rootObj.getAsJsonPrimitive(dataUpdateMode.getDataNewestUpdateDateTimeKey());
            String serverDateTimeStr = serverUpdateDateTime.getAsString();
            String localDateTimeStr = SharedPreferenceUtil.getString(dataUpdateMode.getDataNewestUpdateDateTimeKey());

            if (serverDateTimeStr != null && localDateTimeStr != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                int compareResult = DataTimeUtil.dateTimeCompare(serverDateTimeStr, localDateTimeStr, simpleDateFormat);
                switch (compareResult) {
                    case 1:
                        SharedPreferenceUtil.putString(dataUpdateMode.getDataNewestUpdateDateTimeKey(), serverDateTimeStr);
                        //Update Data File
                        getDataFile(dataUpdateMode.getRemoteDataFileUrl(), dataUpdateMode.getLocalDataFileName());
                        Log.d(LOG_TAG, "通过比较更新时间!");
                        break;
                    default:
                        dataUpdateMode.setCheckUpdateFinish(true);
                        Log.d(LOG_TAG, "当前已是最新数据!");
                }
            } else if (serverDateTimeStr != null && localDateTimeStr == null) {
                SharedPreferenceUtil.putString(dataUpdateMode.getDataNewestUpdateDateTimeKey(), serverDateTimeStr);
                //Update Data File
                getDataFile(dataUpdateMode.getRemoteDataFileUrl(), dataUpdateMode.getLocalDataFileName());
                Log.d(LOG_TAG, "首次更新时间!");
            } else {
                dataUpdateMode.setCheckUpdateFinish(true);
                Log.d(LOG_TAG, "获取更新时间失败!");
            }
        }

        private String getUpdateStrFromServer(String updateDatetimeFileUrl) {
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

        private void getDataFile(String remoteDataFileUrl, String localDataFileName){
            HttpURLConnection urlConnection = null;
            try {
                URL dataFileUrl = new URL(remoteDataFileUrl);
                File saveFile = new File(mContext.getFilesDir(), localDataFileName);

                urlConnection = (HttpURLConnection) dataFileUrl.openConnection();
                urlConnection.setConnectTimeout(3 * 1000);
                urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                InputStream inputStream = urlConnection.getInputStream();
                OutputStream outputStream = new FileOutputStream(saveFile);

                byte[] buffer = new byte[32];
                int hasRead = 0;
                while ((hasRead = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, hasRead);
                }
                Log.d(LOG_TAG,"数据保存成功！");
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
        }
    }


    private class CheckUpdateState implements Runnable{
        private boolean allCheckUpdateFinished=true;
        public CheckUpdateState() {
        }

        @Override
        public void run() {
            while(true){
                Log.d(LOG_TAG,"循环检测任务完成情况");

                allCheckUpdateFinished=true;
                for (int i=0;i<dataUpdateList.size();i++){
                    if (dataUpdateList.get(i).isCheckUpdateFinish()==false){
                        allCheckUpdateFinished=false;
                    }
                }

                if(allCheckUpdateFinished){
                    Log.d(LOG_TAG,"数据更新已完成");

                    //Add Notify At Here
                    if (mContext instanceof MainActivity){
                        Log.d(LOG_TAG,"mContext is MainActivity");
                        MainActivity mainActivity=(MainActivity) mContext;
                        Message msg=mainActivity.mHandler.obtainMessage();
                        msg.what=mainActivity.UPDATE_DATA_FINISH_MSG;
                        mainActivity.mHandler.dispatchMessage(msg);
                    }
                    break;
                }

            }
        }
    }
}
