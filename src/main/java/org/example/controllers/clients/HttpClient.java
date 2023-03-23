package org.example.controllers.clients;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpClient {

    public static String getRawResponseWithAuthentication(String url, String token) throws IOException {

        var reqBuilder = new Request.Builder().get()
                .url(url);
        if(token != null)
            reqBuilder.addHeader("Authorization", "Bearer " + token);

        var request = reqBuilder.build();
        var httpClient = new OkHttpClient();

        var res = httpClient.newBuilder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build()
                .newCall(request)
                .execute();

        if(res.isSuccessful())
            return res.body().string();
        else
            throw new IOException("Response was returned with message: " + res.message());
    }


    public static String getRawResponse(String url) throws IOException {
        return getRawResponseWithAuthentication(url, null);
    }
}
