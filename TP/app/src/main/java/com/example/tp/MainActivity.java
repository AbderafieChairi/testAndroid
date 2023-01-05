package com.example.tp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int CALL_Perm;
    EditText phoneNumber;
    EditText url;

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i("LIFECYCLE ", getLocalClassName() + " : ici onActivityResult: ");
                    Intent data = result.getData();
                    String username = data.getStringExtra("username");
                    String password = data.getStringExtra("password");
                    if (username.equals("admin") && password.equals("admin")){
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                {Manifest.permission.CALL_PHONE}, CALL_Perm);
                        
                        String number = phoneNumber.getText().toString();

                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
                    }
                }
            });
    ActivityResultLauncher<Intent> checkSum = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i("LIFECYCLE ", getLocalClassName() + " : ici checksum: ");
                    Intent data = result.getData();
                    Integer challenge1 = Integer.parseInt(((EditText) findViewById(R.id.challenge1)).getText().toString());
                    Integer challenge2 = Integer.parseInt(((EditText) findViewById(R.id.challenge2)).getText().toString());
                    Integer sum = Integer.parseInt(data.getStringExtra("result"));
                    Log.i("LIFECYCLE ", challenge1+challenge2 + " : ici ch1+ch2: ");
                    Log.i("LIFECYCLE ", sum + " : ici sum: ");

                    if(challenge1+challenge2==sum){
                        String url_ = url.getText().toString();
                        if(url_.compareTo("")==0){
                            url_="https://www.emi.ac.ma";
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+url_));
                        startActivity(intent);
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.phoneNumber = (EditText) findViewById(R.id.call_inp)  ;
        this.url = (EditText) findViewById(R.id.serve_input)  ;

        ((Button) findViewById(R.id.call)).setOnClickListener(v->{
            this.callNumber(v);
        });
        ((Button) findViewById(R.id.serve)).setOnClickListener(v->{
            serveSite(v);
        });
    }

    public void persoActivity(View view){
        Intent myIntent = new Intent(this, PersoActivity.class);
        startActivity(myIntent);
    }
    public void serveSite(View view){
        Intent check = new Intent(MainActivity.this, Check.class);
        String challenge1 = ((EditText) findViewById(R.id.challenge1)).getText().toString();
        String challenge2 =((EditText) findViewById(R.id.challenge2)).getText().toString();
        check.putExtra("challenge1",challenge1);
        check.putExtra("challenge2",challenge2);
        checkSum.launch(check);
    }
    public void callNumber(View view){
        String x = phoneNumber.getText().toString();
        Log.i("LIFECYCLE number :",x);
        //((TextView) findViewById(R.id.textView5)).setText(x);
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        activityLauncher.launch(myIntent);
    }
}
//textView5