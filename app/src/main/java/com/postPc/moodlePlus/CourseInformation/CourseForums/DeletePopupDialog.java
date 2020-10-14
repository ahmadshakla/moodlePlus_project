package com.postPc.moodlePlus.CourseInformation.CourseForums;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DeletePopupDialog extends AppCompatDialogFragment {
    private DeletePopupDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete post")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.getAnswer(true);
                    }
                })
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.getAnswer(false);
                    }
                }).setMessage("are you sure you want to delete this post?");
        return builder.create();
    }

    public  interface DeletePopupDialogListener{
        boolean getAnswer(boolean answer);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener =( DeletePopupDialogListener)context;
    }
}
