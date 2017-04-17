package edu.neu.madcourse.entingwu.firebase;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import edu.neu.madcourse.entingwu.R;

public class FCMActivity extends AppCompatActivity {

    private static final String TAG = FCMActivity.class.getSimpleName();

    // Please add the server key from your firebase console in the follwoing format "key=<serverKey>"
    public static final String SERVER_KEY = "key=AAAAyUeo0PE:APA91bEf8-5uFFKVxuP7RsqI-zZhUZAoAIyY9eU5myfNq4nLrBTU7e8ECauhk8Iu6IFLCC3nWmiSqn6snWOnsTzS7LicevQpa2QAWw5IMeL5IIODcXNS0W7m4pziaWj3EdG7H5t0U73-";
    // This is the client registration token
    //public static final String CLIENT_REGISTRATION_TOKEN = "fnKGxpfxRE0:APA91bGbZOTT6qtBV5R5blumHCIkmxaOay_Km6PyA3ErmuBRRIZ9nuPtXjnpRQghLYcYQs9XarlomvsONCqbhnQVf2q0GLiS-zFEgJQpUk69_8qBACJ6k-yyJ2gsYgFcf6RsXI1ucHYG";
    // LG
    //public static final String CLIENT_REGISTRATION_TOKEN = "elbwp_ASNCI:APA91bHbphujgLbRZT5B8djXXBgDOiJGkKaTN8qU2VFyIJMEy1iAUIi0xlG4qvgKdQhd3KRDHofgUUzc58CoA53EP7Hs4DY-P_-4g4wAAtWPrsJdug5oCbZ4VVgI8gkGHxhM6vvUAklm";
    // ZTE
    public static final String CLIENT_REGISTRATION_TOKEN = "f50NPZp5AgU:APA91bHNNUuXHUAqY7K9CJezpdhT4XEVCWad8ZEiUZXCUr33HAjViDSbMEGcgkkF9roUb9SR_i0_PFBOh4m0UwHQG9o2fJzYVcJReWchEnMvwiejH28M5agYtelcg_YkTb0j3VGzwsek";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm);

        Button logTokenButton = (Button) findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                String token = FirebaseInstanceId.getInstance().getToken();

                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
                Toast.makeText(FCMActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pushNotification(View type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification();
            }
        }).start();
    }

    /**
     * Pushes a notification to a given device-- in particular, this device,
     * because that's what the instanceID token is defined to be.
     */
    private void pushNotification() {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        try {
            jNotification.put("title", "Google I/O 2016");
            jNotification.put("body", "But really this");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");

            // If sending to a single client
            jPayload.put("to", CLIENT_REGISTRATION_TOKEN);

            /*
            // If sending to multiple clients (must be more than 1 and less than 1000)
            JSONArray ja = new JSONArray();
            ja.put(CLIENT_REGISTRATION_TOKEN);
            // Add Other client tokens
            ja.put(FirebaseInstanceId.getInstance().getToken());
            jPayload.put("registration_ids", ja);
            */

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);


            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + resp);
                    Toast.makeText(FCMActivity.this,resp,Toast.LENGTH_LONG);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    public void subscribeToNews(View view){
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        // [END subscribe_topics]

        // Log and toast
        String msg = getString(R.string.msg_subscribed);
        Log.d(TAG, msg);
        Toast.makeText(FCMActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
