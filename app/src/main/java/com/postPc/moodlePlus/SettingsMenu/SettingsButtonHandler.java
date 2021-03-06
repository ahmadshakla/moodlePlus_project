package com.postPc.moodlePlus.SettingsMenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.fragment.app.FragmentActivity;
import androidx.work.PeriodicWorkRequest;

import com.postPc.moodlePlus.MainActivity;
import com.postPc.moodlePlus.R;
import com.postPc.moodlePlus.UserInformation.UserInfo;

public class SettingsButtonHandler  implements PopupMenu.OnMenuItemClickListener {
    Context context;
    Activity activity;
    UserInfo userInfo;
    PeriodicWorkRequest periodicWorkRequest;
    public SettingsButtonHandler(Context context, Activity activity, UserInfo userInfo,
                                 PeriodicWorkRequest periodicWorkRequest) {
        this.context = context;
        this.activity =activity;
        this.userInfo = userInfo;
        this.periodicWorkRequest = periodicWorkRequest;
    }

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.logOutPopup){
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            activity.finish();
        }
        else if(menuItem.getItemId() == R.id.userInfoPopup){
            SettingsUserInfoPopup smsPopup = new SettingsUserInfoPopup(userInfo);
            smsPopup.show(((FragmentActivity)activity).getSupportFragmentManager(), "example " +
                    "dialog");

        }
        else if (menuItem.getItemId() == R.id.notificationPopUp){
            NotificationsPopup notificationsPopup = new NotificationsPopup(periodicWorkRequest,
                    activity);
            notificationsPopup.show(((FragmentActivity)activity).getSupportFragmentManager(),
                    "example notification dialog");
        }
        return true;

    }
}
