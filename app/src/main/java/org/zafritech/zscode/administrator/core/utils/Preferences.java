package org.zafritech.zscode.administrator.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private Context context;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public Preferences(Context context) {

        this.context = context;
        this.prefs = this.context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
        this.editor = this.prefs.edit();
    }

    public void setItem(String key, String value) {

        editor.putString(key, value);
        editor.commit();
    }

    public String getItem(String key) {

        return prefs.getString(key, null);
    }

    public void deleteItem(String key) {

        editor.remove(key);
        editor.commit();
    }
}
