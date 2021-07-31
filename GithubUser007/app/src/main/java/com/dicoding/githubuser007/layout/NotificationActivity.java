package com.dicoding.githubuser007.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dicoding.githubuser007.R;
import com.dicoding.githubuser007.adapter.NotificationFragment;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportFragmentManager().beginTransaction().add(R.id.holder_settings, new NotificationFragment()).commit();
    }
}