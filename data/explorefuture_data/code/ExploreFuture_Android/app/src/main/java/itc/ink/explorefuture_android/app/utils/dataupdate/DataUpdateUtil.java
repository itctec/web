package itc.ink.explorefuture_android.app.utils.dataupdate;

import android.content.Context;
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
import itc.ink.explorefuture_android.app.utils.SharedPreferenceUtil;

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

        //Prepare Local Data
        threadPool.submit(new PrepareLocalDataToCatch());

        //Get Server Data
        for (int i = 0; i < dataUpdateList.size(); i++) {
            threadPool.submit(new GetUpdateRunnable(dataUpdateList.get(i)));
        }

        //Check Update State
        threadPool.submit(new CheckUpdateState());
        //Shutdown ThreadPool
        threadPool.shutdown();
    }

    public static String updateData(DataUpdateMode dataUpdateMode) {
        return handleUpdate(dataUpdateMode);
    }

    private static String handleUpdate(DataUpdateMode dataUpdateMode) {
        String resultStr = null;

        //Get Update DateTime File From Server
        String updateDateTimeStr = getUpdateStrFromServer(dataUpdateMode.getUpdateDatetimeFileUrl());

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
        String serverDateTimeStr =null;
        if(serverUpdateDateTime!=null){
            serverDateTimeStr = serverUpdateDateTime.getAsString();
        }else{
            serverDateTimeStr =null;
        }
        String localDateTimeStr = SharedPreferenceUtil.getString(dataUpdateMode.getDataNewestUpdateDateTimeKey());

        if (serverDateTimeStr != null && !serverDateTimeStr.isEmpty() && localDateTimeStr != null && !localDateTimeStr.isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            int compareResult = DataTimeUtil.dateTimeCompare(serverDateTimeStr, localDateTimeStr, simpleDateFormat);
            switch (compareResult) {
                case 1:
                    SharedPreferenceUtil.putString(dataUpdateMode.getDataNewestUpdateDateTimeKey(), serverDateTimeStr);
                    //Update Data File
                    resultStr = getRemoteData(dataUpdateMode);
                    Log.d(LOG_TAG, "通过比较时间更新数据!");
                    break;
                default:
                    resultStr = UPDATE_RESULT_NEWEST_ALREADY;
                    dataUpdateMode.setCheckUpdateFinish(true);
                    Log.d(LOG_TAG, "当前已是最新数据!");
            }
        } else if (serverDateTimeStr != null && (localDateTimeStr == null||localDateTimeStr.isEmpty())) {
            SharedPreferenceUtil.putString(dataUpdateMode.getDataNewestUpdateDateTimeKey(), serverDateTimeStr);
            //Update Data File
            resultStr = getRemoteData(dataUpdateMode);
            Log.d(LOG_TAG, "首次更新数据!");
        } else {
            resultStr = UPDATE_RESULT_FAILED;
            dataUpdateMode.setCheckUpdateFinish(true);
            Log.d(LOG_TAG, "获取数据失败!");
        }
        return resultStr;
    }

    private static String getUpdateStrFromServer(String updateDatetimeFileUrl) {
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

    private static String getRemoteData(DataUpdateMode dataUpdateMode) {
        HttpURLConnection urlConnection = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL dataFileUrl = new URL(dataUpdateMode.getRemoteDataFileUrl());
            File saveFile = new File(mContext.getFilesDir(), dataUpdateMode.getLocalDataFileName());

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
                bufferedWriter.write(catchStr);
            }
            bufferedWriter.flush();
            updateDataCatch(dataUpdateMode.getLocalDataFileName(), stringBuilder.toString());
            Log.d(LOG_TAG, dataUpdateMode.getLocalDataFileName() + "数据保存成功！");

            bufferedReader.close();
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

    private static void updateDataCatch(String localDataFileName, String dataStr) {
        switch (localDataFileName) {
            case DataUpdateMode.RECOMMEND_HANDPICK_LOCAL_DATA_FILE_NAME:
                DataUpdateMode.RECOMMEND_HANDPICK_JSON_DATA_STR = dataStr;
                break;
            case DataUpdateMode.RECOMMEND_ATTENTION_LOCAL_DATA_FILE_NAME:
                DataUpdateMode.RECOMMEND_ATTENTION_JSON_DATA_STR = dataStr;
                break;
            case DataUpdateMode.RECOMMEND_MIND_HOTTEST_LOCAL_DATA_FILE_NAME:
                DataUpdateMode.RECOMMEND_MIND_HOTTEST_JSON_DATA_STR = dataStr;
                break;
            case DataUpdateMode.RECOMMEND_MIND_NEWEST_LOCAL_DATA_FILE_NAME:
                DataUpdateMode.RECOMMEND_MIND_NEWEST_JSON_DATA_STR = dataStr;
                break;
            case DataUpdateMode.SORT_ALL_LOCAL_DATA_FILE_NAME:
                DataUpdateMode.SORT_ALL_JSON_DATA_STR = dataStr;
                break;
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

    private class PrepareLocalDataToCatch implements Runnable {
        @Override
        public void run() {
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < dataUpdateList.size(); i++) {
                stringBuilder.delete(0, stringBuilder.length());
                DataUpdateMode dataUpdateMode = dataUpdateList.get(i);
                File localDataFile = new File(mContext.getFilesDir(), dataUpdateMode.getLocalDataFileName());
                BufferedReader bufferedReader;
                if(localDataFile.exists()){
                    try {
                        InputStream inputStream = new FileInputStream(localDataFile);
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                        String catchStr;
                        while ((catchStr = bufferedReader.readLine()) != null) {
                            stringBuilder.append(catchStr);
                        }

                        updateDataCatch(dataUpdateMode.getLocalDataFileName(), stringBuilder.toString());

                        inputStream.close();
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
