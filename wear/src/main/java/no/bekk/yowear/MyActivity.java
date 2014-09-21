package no.bekk.yowear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class MyActivity extends Activity {
    private static final int SPEECH_REQUEST_CODE = 0;
    public static final String URL = "http://api.justyo.co";

    private TextView mTextView;
    private RestAdapter restAdapter;
    private YoService yoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(URL)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        yoService = restAdapter.create(YoService.class);

        displaySpeechRecognizer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String firstAndBest = results.get(0);

            yo(firstAndBest);
        }
    }

    private void yo(String user) {
        Yo yo = new Yo();
        yo.setApi_token(ApiConfig.KEY);
        yo.setUsername(user);

        yoService.send(yo, new Callback<Yo>() {
            @Override
            public void success(Yo yo, Response response) {
                Log.i("Result", yo.getUsername() + " is yo'ed");
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }
}
