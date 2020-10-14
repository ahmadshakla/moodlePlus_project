package com.postPc.moodlePlus.SettingsMenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.postPc.moodlePlus.Constants;
import com.postPc.moodlePlus.R;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsPopup extends AppCompatDialogFragment {
    PeriodicWorkRequest periodicWorkRequest;
    Activity activity;
    SharedPreferences sharedPreferences;
    public NotificationsPopup(PeriodicWorkRequest periodicWorkRequest, Activity activity) {
        this.periodicWorkRequest = periodicWorkRequest;
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(Constants.SHARED_PREFERENCE,MODE_PRIVATE);
    }

    private EditText phoneNum;

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.setting_notification_popup, null);
        Switch notiSwitch = view.findViewById(R.id.switch1);
        notiSwitch.setChecked(sharedPreferences.getBoolean("added work",false));
        notiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    WorkManager.getInstance(activity).enqueue(periodicWorkRequest);
                    sharedPreferences.edit().putBoolean("added work",true).apply();
                }
                else{
                    WorkManager.getInstance(activity).cancelWorkById(periodicWorkRequest.getId());
                    sharedPreferences.edit().putBoolean("added work",false).apply();
                }
            }
        });
        builder.setView(view)
                .setTitle("App Notifications")
                .setCancelable(true);
        return builder.create();
    }
}
