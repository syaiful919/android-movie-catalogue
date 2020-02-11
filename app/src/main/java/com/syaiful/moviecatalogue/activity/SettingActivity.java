package com.syaiful.moviecatalogue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.syaiful.moviecatalogue.R;
import com.syaiful.moviecatalogue.notification.AlarmReceiver;
import com.syaiful.moviecatalogue.preference.Pref;

public class SettingActivity extends AppCompatActivity {
    private Switch swReminder, swRelease;
    private AlarmReceiver alarmReceiver;
    private Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        LinearLayout changeLang = findViewById(R.id.change_lang);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLang();
            }
        });

        pref = new Pref(this);

        swReminder = findViewById(R.id.sw_reminder);
        if (pref.getReminderPref()) {
            swReminder.setChecked(true);
        }
        swReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setDailyReminder();
                } else {
                    cancelDailyReminder();
                }
            }
        });

        swRelease = findViewById(R.id.sw_release);
        if (pref.getReleasePref()) {
            swRelease.setChecked(true);
        }
        swRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setReleaseReminder();
                } else {
                    cancelReleaseReminder();
                }
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.setting));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        alarmReceiver = new AlarmReceiver();
    }

    private void changeLang() {
        Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        startActivity(mIntent);
    }

    private void setReleaseReminder() {
        String repeatTime = "08:00";
        alarmReceiver.setRepeatingAlarm(this,
                repeatTime);
        pref.setReleasePref(true);
        showToastActivated();
    }

    private void setDailyReminder() {
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.default_notification_channel_name));
        pref.setReminderpPref(true);
        showToastActivated();
    }

    private void cancelReleaseReminder() {
        alarmReceiver.cancelAlarm(this);
        pref.setReleasePref(false);
        showToastDisabled();
    }

    private void cancelDailyReminder() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.default_notification_channel_name));
        pref.setReminderpPref(false);
        showToastDisabled();
    }

    private void showToastActivated() {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.reminder_activated), Toast.LENGTH_SHORT).show();
    }

    private void showToastDisabled() {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.reminder_disabled), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
