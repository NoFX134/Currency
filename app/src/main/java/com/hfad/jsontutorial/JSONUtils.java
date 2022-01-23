package com.hfad.jsontutorial;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


//Класс для обратботки данных JSON
public class JSONUtils {
    protected static ArrayList<JSONObject> jsArr = new ArrayList<>();
    protected static String dateTitle;
    private static JSONObject jsonRoot;
    private static String JsonFromURL;
    protected static String JsonFromFile;
    protected final static String APP_PREFERENCES = "appsettings";
    protected final static String APP_PREFERENCES_JSON = "JSONCBR";
    protected final static String JSON_URL = "https://www.cbr-xml-daily.ru//archive//2022//01//11//daily_json.js";
    private static final String TAG = JSONUtils.class.getSimpleName();

    //создание ArrayList JSONobject c данными о валютах

    protected static void createValute() throws IOException, JSONException {
        if (JsonFromFile != null) {
            jsonRoot = new JSONObject(JsonFromFile);
            JSONObject valute = jsonRoot.getJSONObject("Valute");
            jsArr.clear();
            for (ValuteName val : ValuteName.values()) {
                jsArr.add(valute.getJSONObject(val.getValuteName()));
            }
        }
    }

    //создание даты из JSON

    protected static String createDate() {
        if (jsonRoot != null) {
            String dateAndTime = null;
            try {
                dateAndTime = jsonRoot.getString("Date");
            } catch (JSONException ex) {
                Log.e(TAG, "Исключение", ex);
            }
            String date = dateAndTime.substring(0, dateAndTime.indexOf('T'));
            Date dateD = null;
            try {
                dateD = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date);
            } catch (ParseException ex) {
                ex.printStackTrace();
                Log.e(TAG, "Исключение", ex);
            }
            return new SimpleDateFormat("dd MMMM yyyy", new Locale("ru")).format(dateD);
        }
       return "";
    }

    //Соединения с сайтом, скачивание и запись JSON в строку
    protected static void downloadAndWriteJson() {

        JsonTask task = new JsonTask();
        task.execute(JSON_URL);
        try {
            JsonFromURL = task.get();
        } catch (ExecutionException | InterruptedException ex) {
            ex.printStackTrace();
            Log.e(TAG, "Исключение", ex);
        }
    }

    //Сохранения данных с сайта в SharedPreferences
    protected static void writeJsonInFile() {
        SharedPreferences.Editor editor = MainActivity.mySharedPreferences.edit();
        editor.putString(APP_PREFERENCES_JSON, JsonFromURL);
        editor.apply();
    }

    //Запись данных с файла в строку
    protected static void JSONUtilsToString() {
        if (MainActivity.mySharedPreferences.contains(APP_PREFERENCES_JSON)) {
            JsonFromFile = MainActivity.mySharedPreferences.getString(APP_PREFERENCES_JSON, "");
        }
    }
}


