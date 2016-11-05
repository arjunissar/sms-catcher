package com.example.graphics.smsinterceptor;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECEIVE_SMS);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.RECEIVE_SMS
                                    ,Manifest.permission.READ_SMS},
                            1234);
                }
            }
        });

        findViewById(R.id.get_all).setOnClickListener(new View.OnClickListener() {

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            @Override
            public void onClick(View v) {
                List<String> sms = new ArrayList<String>();
                Uri uriSMSURI = Uri.parse("content://sms/inbox");
                Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);

                String string = new String();

                while (cur.moveToNext()) {
                    String address = cur.getString(cur.getColumnIndex("address"));
                    String body = cur.getString(cur.getColumnIndexOrThrow("body"));
                    sms.add("Number: " + address + " .Message: " + body);
                    string += "Number : "+address + "\nMessage : "+body+"\n\n\n";
                }

                editor.putString("existing_sms",string);
                editor.commit();
                editor.apply();
            }
        });

        findViewById(R.id.backup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Save in a file
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1234: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,"Granted!",Toast.LENGTH_SHORT).show();

                } else {

                }
                return;
            }
        }
    }
}
