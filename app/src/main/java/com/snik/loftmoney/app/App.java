package com.snik.loftmoney.app;

import android.app.Application;
import android.text.TextUtils;

import com.snik.loftmoney.BuildConfig;
import com.snik.loftmoney.api.Api;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class App extends Application {

    private static final String PREFERENCES_SESSION = "shared_preferences";
    private static final String AUTH_TOKEN = "auth_token";

    private Api api;


    @Override
    public void onCreate() {
        super.onCreate();
        HttpLoggingInterceptor.Level level = BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE;
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(level);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new AuthInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://android.loftschool.com/basic/v1/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    public Api getApi() {
        return api;
    }

    public void setAuthToken(String token) {
        getSharedPreferences(PREFERENCES_SESSION, MODE_PRIVATE)
                .edit()
                .putString(AUTH_TOKEN, token)
                .apply();
    }

    public String getAuthToken() {
        return getSharedPreferences(PREFERENCES_SESSION, MODE_PRIVATE).getString(AUTH_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(getAuthToken());
    }


    class AuthInterceptor implements Interceptor {


        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            HttpUrl url = originalRequest.url()
                    .newBuilder()
                    .addQueryParameter("auth-token", getAuthToken()).build();
            return chain.proceed(originalRequest.newBuilder().url(url).build());
        }
    }


}
