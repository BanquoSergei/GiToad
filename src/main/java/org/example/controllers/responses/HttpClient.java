package org.example.controllers.responses;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpClient {

    public static String getRawResponseWithAuthentication(String url, String token) throws IOException {

        var request = new Request.Builder().get()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .build();
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
