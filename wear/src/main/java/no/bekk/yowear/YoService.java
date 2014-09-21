package no.bekk.yowear;

import android.os.Message;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface YoService {
    @POST("/yo")
    void send(@Body Yo user, Callback<Yo> callback);
}
