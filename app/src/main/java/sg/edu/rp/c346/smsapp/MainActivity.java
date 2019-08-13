package sg.edu.rp.c346.smsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etTo, etContent;
    Button btnSend, btnSend2;
    BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        etTo = findViewById(R.id.etTo);
        etContent = findViewById(R.id.etContent);
        btnSend = findViewById(R.id.buttonSend);
        btnSend2 = findViewById(R.id.buttonSend2);
        br = new MessageReceiver();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br, filter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String num = etTo.getText().toString();
                String content = etContent.getText().toString();

                SmsManager smsManager = SmsManager.getDefault();

                if (num.contains(",")){

                    String[] numArray = etTo.getText().toString().split(", ");
                    for (int i = 0; i < numArray.length; i++){
                        smsManager.sendTextMessage(numArray[i], null, content, null, null);

                    }

                }else {

                    smsManager.sendTextMessage(num, null, content, null, null);

                }

                Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();

            }
        });

        btnSend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String num = etTo.getText().toString();
                String content = etContent.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + num));
                intent.putExtra("sms_body",content);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(br);
    }

    private void checkPermission() {

        int permissionSendSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        int permissionRecvSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }

    }

}
