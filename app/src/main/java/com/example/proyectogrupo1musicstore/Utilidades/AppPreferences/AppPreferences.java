package com.example.proyectogrupo1musicstore.Utilidades.AppPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_FIRST_TIME = "isFirstTime";
    private static final String KEY_USER_SCORE = "userScore";

    public static boolean isFirstTimeOpen(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_FIRST_TIME, true);
    }

    public static void setFirstTimeOpen(Context context, boolean isFirstTime) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_FIRST_TIME, isFirstTime);
        editor.apply();
    }

    public static int getUserScore(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(KEY_USER_SCORE, 0);
    }

    public static void setUserScore(Context context, int score) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_USER_SCORE, score);
        editor.apply();
    }

    public static void resetFirstTimePreferences(Context context) {
        setUserScore(context, 1);
        setFirstTimeOpen(context, true);
    }
}
