package com.fireblend.uitest.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.j256.ormlite.stmt.query.In;

public class PreferenceManager {
    private static final String PREF_FILE = "app.preferences";
    private static SharedPreferences mPreferences;

    private static final String ARG_TEXTSIZE = "arg.textSize";
    private static final String ARG_BGCOLOR = "arg.bgColor";
    private static final String ARG_DELETEVISIBLE = "arg.deleteVisible";
    private static final String ARG_ENABLEGRID = "arg.enableGrid";

    static SharedPreferences getSharedPreferences(Context context) {
        return mPreferences == null ? context.getSharedPreferences(PREF_FILE, context.MODE_PRIVATE) : mPreferences;
    }


    public static void savePreferences(Context context, Integer textSize, String bgColor, boolean deleteVisible, boolean enableGrid){

        SharedPreferences.Editor editor = PreferenceManager.getSharedPreferences(context).edit();

        editor.putInt(ARG_TEXTSIZE, textSize);
        editor.putString(ARG_BGCOLOR, bgColor);
        editor.putBoolean(ARG_DELETEVISIBLE, deleteVisible);
        editor.putBoolean(ARG_ENABLEGRID, enableGrid);
        editor.commit();
    }

    public static Integer getTextsizeFromPreferences(Context context){
        return PreferenceManager.getSharedPreferences(context).getInt(ARG_TEXTSIZE, 14);
    }

    public static String getBgColorFromPreferences(Context context){
        return PreferenceManager.getSharedPreferences(context).getString(ARG_BGCOLOR, "#3F51B5");
    }

    public static boolean getDeleteVisibilityFromPreferences(Context context){
        return PreferenceManager.getSharedPreferences(context).getBoolean(ARG_DELETEVISIBLE, false);
    }

    public static boolean getEnableGridFromPreferences(Context context){
        return PreferenceManager.getSharedPreferences(context).getBoolean(ARG_ENABLEGRID, false);
    }
}
