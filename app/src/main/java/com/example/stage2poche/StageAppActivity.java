package com.example.stage2poche;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stage2poche.api.APIClient;
import com.example.stage2poche.api.APIService;

import java.util.HashMap;

/**
 * This is the base class for all activities in this app.
 * It provides a reference to the API interface.
 * It also provides a few utility methods.
 */
public class StageAppActivity extends AppCompatActivity {
    protected APIService apiInterface = null;

    public APIService getApiInterface() {
        return apiInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Chargement de l'API
        apiInterface = APIClient.getAPIService();
    }


    protected void clearAuthData() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(getString(R.string.token_key));
        editor.remove(getString(R.string.login_key));
        editor.remove(getString(R.string.login_id_path));
        editor.apply();
    }

    private SharedPreferences getSharedPreferences() {
        return getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }

    public String getToken() {
        return getSharedPreferences().getString(getString(R.string.token_key), "no token");
    }

    public String getLogin() {
        return getSharedPreferences().getString(getString(R.string.login_key), "no login");
    }

    public String getCompteId() {
        return getSharedPreferences().getString(getString(R.string.login_id_path), "");
    }

    protected void setCompteId(String id) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(getString(R.string.login_id_path), id);
        editor.apply();
    }
    protected void setAuthData(String login, String token) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(getString(R.string.token_key), token);
        editor.putString(getString(R.string.login_key), login);
        editor.apply();
    }

    /*
    protected void setEnumValues(HashMap<String, String> entries) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();

        for(String key : entries.keySet()) {
            editor.putString(key, entries.get(key));
        }

        editor.apply();
    }

    protected String getEnumValue(String key) {
        SharedPreferences prefs = getSharedPreferences();

        return prefs.getString(key, "");
    }
    */
}
