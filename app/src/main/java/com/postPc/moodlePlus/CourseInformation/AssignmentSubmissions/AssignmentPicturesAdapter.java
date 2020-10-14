package com.postPc.moodlePlus.CourseInformation.AssignmentSubmissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postPc.moodlePlus.R;

import java.util.ArrayList;

public class AssignmentPicturesAdapter extends RecyclerView.Adapter<AssignmentPicturesAdapter.AssignmentPicturesHolder> {
    ArrayList<Bitmap> images;
    Activity activity;
    String token;
    public AssignmentPicturesAdapter(ArrayList<Bitmap> images,Activity activity,String token) {
        this.activity = activity;
        this.images = images;
        this.token =token;
    }


    @NonNull
    @Override
    public AssignmentPicturesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view_layout,
                parent, false);
        return new AssignmentPicturesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentPicturesHolder holder, int position) {
        Bitmap image = images.get(position);
        holder.imageView.setImageBitmap(image);
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder((activity));
                builder.setTitle("Delete Picture")
                        .setMessage("are you sure you want to delete this picture?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        images.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeRemoved(position, images.size());
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class AssignmentPicturesHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public AssignmentPicturesHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageInView);
        }
    }
}
