package com.example.lab_4.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class CommonService {
    private static CommonService instance;
    public final Context context;
    private Handler mHandler;

    public CommonService(Context context) {
        this.context = context;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static CommonService getInstance() {
        return instance;
    }

    public static CommonService createInstance(Context context) {
        if (instance == null) instance = new CommonService(context);
        return instance;
    }

    public void showToast(String message) {
        mHandler.post(() -> {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        });
    }

    public Handler getHandler() {
        return mHandler;
    }

    public static boolean checkInternet(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }
}
