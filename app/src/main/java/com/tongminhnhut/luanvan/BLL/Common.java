package com.tongminhnhut.luanvan.BLL;

import com.tongminhnhut.luanvan.Remote.APIService;
import com.tongminhnhut.luanvan.Remote.RetrofitClient;

public class Common {
    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService(){
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
