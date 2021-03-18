package com.muchlish.scan_ai.service;

import com.muchlish.scan_ai.activity.entity.Barcode;
import com.muchlish.scan_ai.activity.entity.MyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface BarcodeDataService {
    @GET("/barcodes/{barcode}")
    Call<MyResponse<Barcode>> findBarcode(@Header("authorization") String token,@Path("barcode") String barcode);
}
