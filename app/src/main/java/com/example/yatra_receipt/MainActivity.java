package com.example.yatra_receipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    MaterialButton receiptCreateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiptCreateBtn = findViewById(R.id.createRecieptBtn);

        // CREATE RECEIPT BUTTON CLICKED.
        receiptCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to user details form activity.
                startActivity(new Intent(MainActivity.this, AddUserDetails.class));
            }
        });

        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new MyRealmMigration())
                .build();
        Realm.setDefaultConfiguration(config);
        Realm realm = Realm.getDefaultInstance();

        //Fetch data from data class of all the users
        RealmResults<Data> dataList = realm.where(Data.class).findAll().sort("createdTime", Sort.DESCENDING);

        // Give the dataList to adapter and then set adapter for recycleView.
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter myAdapter = new MyAdapter(getApplicationContext(), dataList);
        recyclerView.setAdapter(myAdapter);

        dataList.addChangeListener(new RealmChangeListener<RealmResults<Data>>() {
            @Override
            public void onChange(RealmResults<Data> data) {
                myAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }

    private void showExitConfirmationDialog() {
        // Ask the user if they really want to leave the application or not.
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Exit Application");
        builder.setMessage("Are you sure you want to exit the application?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}