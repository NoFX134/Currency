package com.hfad.jsontutorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static ArrayList<Valutes> valutes = new ArrayList<>();
    private RecyclerView recyclerView;
    private ValuteAdapter adapter;
    protected NetworkInfo networkInfo;
    protected static SharedPreferences mySharedPreferences;
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySharedPreferences = getSharedPreferences(JSONUtils.APP_PREFERENCES, Context.MODE_PRIVATE);
        Button button = (Button) findViewById(R.id.buttonCurrensyConverter);
        button.setOnClickListener(this);
        try {
            setInitialData();
        } catch (JSONException | IOException ex) {
            ex.printStackTrace();
            Log.e(TAG, "Исключение", ex);
        }
        setTitle(getString(R.string.mainactivityTitle) + " " + JSONUtils.createDate());
        recyclerView = findViewById(R.id.list);
        adapter = new ValuteAdapter(this, valutes);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        Context context=this;
        dividerItemDecoration.setDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.divider_drawable, null));
        recyclerView.addItemDecoration(dividerItemDecoration);
        periodicUpdate();
    }

    //Создание кнопки обновить
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    //Обновление списка по кнопке

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean isNetworkOk = checkInternetConnection();
        if (id == R.id.action_refresh && isNetworkOk) {
            JSONUtils.downloadAndWriteJson();
            JSONUtils.writeJsonInFile();
            JSONUtils.JSONUtilsToString();
            try {
                JSONUtils.createValute();
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
                Log.e(TAG, "Исключение", ex);
            }
            valutes.clear();
            for (int i = 0; i < JSONUtils.jsArr.size(); i++) {
                try {
                    valutes.add(new Valutes(JSONUtils.jsArr.get(i).getString("CharCode"),
                            JSONUtils.jsArr.get(i).getInt("Nominal"),
                            JSONUtils.jsArr.get(i).getString("Name"),
                            JSONUtils.jsArr.get(i).getDouble("Value")));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    Log.e(TAG, "Исключение", ex);
                }
            }
            setTitle(getString(R.string.mainactivityTitle) + " " + JSONUtils.createDate());
            //adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }

        return super.onOptionsItemSelected(item);
    }

    //Переход на окно MAinActivity для кнопки ButtonCurrensyConverter
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonCurrensyConverter) {
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        }
    }

    //Инициализация данных и заполнения списка валют
    private void setInitialData() throws JSONException, IOException {
        boolean isNetWorkOK = checkInternetConnection();
        JSONUtils.JSONUtilsToString();
        if (JSONUtils.JsonFromFile == null && isNetWorkOK) {
            JSONUtils.downloadAndWriteJson();
            JSONUtils.writeJsonInFile();
            JSONUtils.JSONUtilsToString();
            JSONUtils.createValute();
            valutes.clear();
            for (int i = 0; i < JSONUtils.jsArr.size(); i++) {
                valutes.add(new Valutes(JSONUtils.jsArr.get(i).getString("CharCode"),
                        JSONUtils.jsArr.get(i).getInt("Nominal"),
                        JSONUtils.jsArr.get(i).getString("Name"),
                        JSONUtils.jsArr.get(i).getDouble("Value")));
            }
        } else {
            if (JSONUtils.JsonFromFile != null && JSONUtils.JsonFromFile.isEmpty() && isNetWorkOK) {
                JSONUtils.downloadAndWriteJson();
                JSONUtils.writeJsonInFile();
                JSONUtils.JSONUtilsToString();
                JSONUtils.createValute();
                valutes.clear();
                for (int i = 0; i < JSONUtils.jsArr.size(); i++) {
                    valutes.add(new Valutes(JSONUtils.jsArr.get(i).getString("CharCode"),
                            JSONUtils.jsArr.get(i).getInt("Nominal"),
                            JSONUtils.jsArr.get(i).getString("Name"),
                            JSONUtils.jsArr.get(i).getDouble("Value")));
                }
            } else if (JSONUtils.JsonFromFile != null) {
                JSONUtils.createValute();
                valutes.clear();
                for (int i = 0; i < JSONUtils.jsArr.size(); i++) {
                    valutes.add(new Valutes(JSONUtils.jsArr.get(i).getString("CharCode"),
                            JSONUtils.jsArr.get(i).getInt("Nominal"),
                            JSONUtils.jsArr.get(i).getString("Name"),
                            JSONUtils.jsArr.get(i).getDouble("Value")));
                }
            }
        }
    }

    //Проверка интернет соединения
    protected boolean checkInternetConnection() {
        ConnectivityManager connManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable();
    }

    //Периодическое обновление валюты раз в пять минут
    private void periodicUpdate() {
        Thread periodicUpdate = new Thread() {

            @Override
            public void run() {
                while (true) {
                    if (checkInternetConnection()) {
                        try {
                            Thread.sleep(300 * 1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                            Log.e(TAG, "Исключение", ex);

                        }
                        runOnUiThread(() -> {
                            {
                                if (checkInternetConnection()) {
                                    JSONUtils.downloadAndWriteJson();
                                    JSONUtils.writeJsonInFile();
                                    JSONUtils.JSONUtilsToString();
                                    try {
                                        JSONUtils.createValute();
                                    } catch (IOException | JSONException ex) {
                                        ex.printStackTrace();
                                        Log.e(TAG, "Исключение", ex);
                                    }
                                    valutes.clear();
                                    for (int i = 0; i < JSONUtils.jsArr.size(); i++) {
                                        try {
                                            valutes.add(new Valutes(JSONUtils.jsArr.get(i).getString("CharCode"),
                                                    JSONUtils.jsArr.get(i).getInt("Nominal"),
                                                    JSONUtils.jsArr.get(i).getString("Name"),
                                                    JSONUtils.jsArr.get(i).getDouble("Value")));
                                        } catch (JSONException ex) {
                                            ex.printStackTrace();
                                            Log.e(TAG, "Исключение", ex);
                                        }
                                    }
                                    setTitle(getString(R.string.mainactivityTitle) + " " + JSONUtils.createDate());
                                    //adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        });
                        super.run();
                    }
                }
            }
        };
        periodicUpdate.start();
    }
}




