package com.tongminhnhut.luanvan.BLL;

import com.tongminhnhut.luanvan.Remote.APIService;
import com.tongminhnhut.luanvan.Remote.IGoogleService;
import com.tongminhnhut.luanvan.Remote.RetrofitClient;
import com.tongminhnhut.luanvan.Remote.RetrofitGoogleApi;

public class Common {
    private static final String BASE_URL = "https://fcm.googleapis.com/";

    private static final String Google_URL = "https://maps.googleapis.com/";


    public static APIService getFCMService(){
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static IGoogleService getGoogleMapAPI(){
        return RetrofitGoogleApi.getGoogleClient(Google_URL).create(IGoogleService.class);
    }
}
