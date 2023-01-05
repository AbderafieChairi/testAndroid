package com.example.tp_partie_2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String phoneNumber_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ((Button) findViewById(R.id.btnContactID)).setOnClickListener(v->{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, 1);
        });
        ((Button) findViewById(R.id.btnDetailsContact)).setOnClickListener(v-> {
            this.getDetails(v);
        });
        ((Button) findViewById(R.id.btnCall)).setOnClickListener(v-> {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, 2);
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts/people"));
                activityLauncher.launch(intent);
            }
        }
        if (requestCode == 2){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + this.phoneNumber_));
                startActivity(intent);
            }
        }
    }

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i("LIFECYCLE ", getLocalClassName() + " : ici onActivityResult: ");
                    // Traitement des données de contact sélectionnées par l'utilisateur

                    if (result.getResultCode()== RESULT_OK){
                        Intent data = result.getData();
                        String contactUri = data.getDataString();
                        TextView textView = (TextView) findViewById(R.id.result);
                        textView.setText(contactUri);
                        ((Button) findViewById(R.id.btnDetailsContact)).setEnabled(true);


                    }else if (result.getResultCode() == RESULT_CANCELED) {
                        // Affichez un message indiquant que l'opération a été annulée
                        TextView textView = (TextView) findViewById(R.id.result);
                        textView.setText("Opération annulée");
                    }
                }
            });



    private void getDetails(View v){
        // Récupérez l'ID du contact à partir de l'URI du contact
        String contactUri = ((TextView) findViewById(R.id.result)).getText().toString();
        String contactID = getContactIDFromUri(contactUri);
        // Interrogez le ContentProvider de contacts
        Cursor cursor = getContentResolver().query(Uri.parse(contactUri), null, null, null, null);
        if (cursor.moveToFirst()) {
            // Récupérez le nom du contact
            int id1 = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String name="";
            if (id1!=-1){
                name = cursor.getString(id1);
            }

            // Affichez le nom du contact
            TextView textView = (TextView) findViewById(R.id.result);
            textView.setText("Name: " + name);
            // Récupérez le numéro de téléphone du contact
            Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                    new String[]{contactID},
                    null);
            if (cursorPhone.moveToFirst()) {

                String phoneNumber = "";
                int id2=cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                if(id2!=-1){
                    phoneNumber =  cursorPhone.getString(id2);
                    this.phoneNumber_ = cursorPhone.getString(id2);
                }
                // Affichez le numéro de téléphone du contact
                textView.append("\nPhone: " + phoneNumber);
            }
            cursorPhone.close();
        }
        cursor.close();
        ((Button) findViewById(R.id.btnCall)).setEnabled(true);
    }


    private String getContactIDFromUri(String contactUri) {
        String[] projection = new String[]{ContactsContract.Contacts._ID};
        Cursor cursor = getContentResolver().query(Uri.parse(contactUri), projection, null, null, null);
        if (cursor.moveToFirst()) {
            int contactIDIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            if (contactIDIndex != -1) {
                return cursor.getString(contactIDIndex);
            }
        }
        cursor.close();
        return null;
    }








}