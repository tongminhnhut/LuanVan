package com.tongminhnhut.luanvan.Remote;

import com.tongminhnhut.luanvan.Model.MyResponse;
import com.tongminhnhut.luanvan.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAARxvd-q0:APA91bFJdAOfLDXBlkId_c6WMqfrMOwsGBMl9kukbGCkyEecMVKJ3377uf6fHT9bzc5RmsfSqq8VQBgdKmzsi5ldXwhwzdTgk-053FWB21geUfBa-_pf8SGyVOSRpyBJe5cQ2bn4CYKT"
    })
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
