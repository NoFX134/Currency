package com.hfad.jsontutorial;

import android.util.Log;

import java.io.InputStream;
import java.io.Reader;
//Закрыте потоков
class IOUtils {
    private static final String TAG = IOUtils.class.getSimpleName();
    static void closeQuietly(InputStream in) {
        try {
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Исключение", ex);
        }
    }
    static void closeQuietly(Reader reader) {
        try {
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Исключение", ex);
        }
    }
}
