package edu.neu.madcourse.entingwu;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.neu.madcourse.entingwu.R;

public class AboutMe extends AppCompatActivity {

    private static final int DEV_ID = 1;
    private static final String TAG = "AboutMe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        Log.i(TAG, "Creating the Activity now.");
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_about_me);
        TextView imeiTV = (TextView) findViewById(R.id.textView_phoneid);

        // Check the permission
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Granted Permission.");
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imeiTV.setText(tm.getDeviceId());
        } else {
            Log.d(TAG, "Request Permission.");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, DEV_ID);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case DEV_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Contacts-related task");
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    TextView imeiTV = (TextView) findViewById(R.id.textView_phoneid);
                    imeiTV.setText(tm.getDeviceId());
                } else {
                    Log.i(TAG, "Permission Denied");
                    // Permission Denied
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request.
        }
    }

}