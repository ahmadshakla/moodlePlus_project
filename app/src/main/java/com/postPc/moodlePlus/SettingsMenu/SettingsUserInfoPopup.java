package com.postPc.moodlePlus.SettingsMenu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.postPc.moodlePlus.Constants;
import com.postPc.moodlePlus.R;
import com.postPc.moodlePlus.UserInformation.UserInfo;

import java.util.Objects;

public class SettingsUserInfoPopup extends AppCompatDialogFragment {
    UserInfo userInfo;
    public SettingsUserInfoPopup(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    private EditText phoneNum;

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.settings_userinfo_popup_layout, null);
        TextView name =view.findViewById(R.id.userName_popup);
        TextView email =view.findViewById(R.id.email_popup);
        TextView idNum =view.findViewById(R.id.idNum_popup);
        name.setText(name.getText() + userInfo.getFullname());
        email.setText(email.getText() + userInfo.getEmail());
        idNum.setText(idNum.getText() + userInfo.getIdnumber());
        builder.setView(view)
                .setTitle(Constants.USER_INFO)
                .setCancelable(true);
        return builder.create();
    }
}
