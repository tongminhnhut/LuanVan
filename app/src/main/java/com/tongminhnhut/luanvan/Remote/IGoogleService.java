package com.tongminhnhut.luanvan.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleService {
    @GET
    Call<String> getAddressToShip(@Url String url);
}
