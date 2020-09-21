package com.example.myapplication.SettingsMenu;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.UserInformation.UserInfo;

public class SettingsButtonHandler  implements PopupMenu.OnMenuItemClickListener {
    Context context;
    Activity activity;
    UserInfo userInfo;
    public SettingsButtonHandler(Context context, Activity activity, UserInfo userInfo) {
        this.context = context;
        this.activity =activity;
        this.userInfo = userInfo;
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
        return true;

    }
}
