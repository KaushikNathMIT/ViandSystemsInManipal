package com.kaushiknath.viandsystem;

import android.app.Application;

import org.kawanfw.sql.api.client.android.AceQLDBManager;

/**
 * Created by Kaushik Nath on 12-Mar-16.
 */
public class ViandSys extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        final String url = "jdbc:aceql:http://192.168.0.100:9090/ServerSqlManager";
        AceQLDBManager.initialize(url, "username", "password");
    }
}
